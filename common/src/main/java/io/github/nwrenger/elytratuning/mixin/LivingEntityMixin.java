package io.github.nwrenger.elytratuning.mixin;

import io.github.nwrenger.elytratuning.Common;
import io.github.nwrenger.elytratuning.client.State;
import io.github.nwrenger.elytratuning.config.Config;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyArg(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
            ordinal = 6
        ),
        index = 0
    )
    private Vec3 elytraTuning$capFallFlyingVelocity(Vec3 velocity) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.isFallFlying()) {
            return velocity;
        }

        Config config = entity.level().isClientSide()
            ? State.config
            : Common.getConfig();

        Vec3 capped = Common.capElytraVelocity(config, velocity);

        return capped == null ? velocity : capped;
    }

    @Inject(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
            ordinal = 6,
            shift = At.Shift.AFTER
        )
    )
    private void elytraTuning$syncFallFlyingVelocity(
        Vec3 input,
        CallbackInfo callback
    ) {
        if (
            (Object) this instanceof ServerPlayer player &&
            player.isFallFlying()
        ) {
            player.hasImpulse = true;
            player.hurtMarked = true;
        }
    }
}
