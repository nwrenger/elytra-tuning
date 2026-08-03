package io.github.nwrenger.elytratuning.mixin;

import io.github.nwrenger.elytratuning.Common;
import io.github.nwrenger.elytratuning.client.State;
import io.github.nwrenger.elytratuning.config.Config;
import io.github.nwrenger.elytratuning.config.Rocket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {

    @Shadow
    private LivingEntity attachedToEntity;

    @Shadow
    private int lifetime;

    @Shadow
    private int life;

    @Inject(method = "tick", at = @At("HEAD"))
    private void elytraTuning$expireAfterLanding(CallbackInfo callback) {
        LivingEntity entity = this.attachedToEntity;

        // When the rocket is attached to an entity that is not flying,
        // we want to expire the rocket immediately after it lands.
        if (entity != null && !entity.isFallFlying()) {
            this.lifetime = Math.min(this.lifetime, this.life);
        }
    }

    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
        ),
        index = 0
    )
    private Vec3 elytraTuning$modifyRocketBoost(Vec3 vanillaBoost) {
        LivingEntity entity = this.attachedToEntity;

        if (entity == null || !entity.isFallFlying()) {
            return vanillaBoost;
        }

        Config config = entity.level().isClientSide()
            ? State.config
            : Common.getConfig();

        Vec3 adjustedVelocity = vanillaBoost;

        Rocket rocketConfig = config.rocket;
        if (rocketConfig != null) {
            double multiplier = rocketConfig.strength;

            // If the multiplier is less than or equal to 1, we want to scale the
            // vanilla boost by the multiplier. Otherwise, we want to add extra
            // acceleration in the direction the player is looking.
            if (multiplier <= 1.0D) {
                Vec3 oldVelocity = entity.getDeltaMovement();
                Vec3 vanillaAcceleration = vanillaBoost.subtract(oldVelocity);
                adjustedVelocity = oldVelocity.add(
                    vanillaAcceleration.scale(multiplier)
                );
            } else {
                Vec3 extraAcceleration = entity
                    .getLookAngle()
                    .scale(0.1D * (multiplier - 1.0D));
                adjustedVelocity = vanillaBoost.add(extraAcceleration);
            }
        }

        Vec3 capped = Common.capElytraVelocity(config, adjustedVelocity);
        return capped == null ? adjustedVelocity : capped;
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
            shift = At.Shift.AFTER
        )
    )
    private void elytraTuning$syncRocketBoost(CallbackInfo callback) {
        if (
            !(this.attachedToEntity instanceof ServerPlayer player) ||
            !player.isFallFlying()
        ) {
            return;
        }

        // Force syncing the player's velocity
        player.hasImpulse = true;
        player.hurtMarked = true;
    }

    @Inject(
        method = "<init>(" +
            "Lnet/minecraft/world/level/Level;" +
            "Lnet/minecraft/world/item/ItemStack;" +
            "Lnet/minecraft/world/entity/LivingEntity;" +
            ")V",
        at = @At("TAIL")
    )
    private void elytraTuning$modifyBoostDuration(
        Level level,
        ItemStack stack,
        LivingEntity attachedEntity,
        CallbackInfo ci
    ) {
        if (!attachedEntity.isFallFlying()) {
            return;
        }

        Config config = level.isClientSide()
            ? State.config
            : Common.getConfig();

        Rocket rocketConfig = config.rocket;
        if (rocketConfig == null) {
            return;
        }

        // Apply the duration multiplier to the rocket's lifetime
        long adjustedLifetime = Math.round(
            this.lifetime * rocketConfig.duration
        );
        this.lifetime = (int) Math.min(Integer.MAX_VALUE, adjustedLifetime);
    }
}
