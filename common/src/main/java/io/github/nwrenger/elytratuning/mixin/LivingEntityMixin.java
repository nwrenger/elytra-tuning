package io.github.nwrenger.elytratuning.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.nwrenger.elytratuning.Common;
import io.github.nwrenger.elytratuning.client.State;
import io.github.nwrenger.elytratuning.config.Config;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @WrapOperation(
        method = "travelFallFlying",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
        ),
        require = 1,
        allow = 1
    )
    private void elytraTuning$capFallFlyingVelocity(
        LivingEntity entity,
        Vec3 input,
        Operation<Void> original
    ) {
        Config config = entity.level().isClientSide()
            ? State.config
            : Common.getConfig();

        Vec3 capped = Common.capElytraVelocity(config, input);
        if (capped == null) {
            original.call(entity, input);
            return;
        }

        original.call(entity, capped);

        if (entity instanceof ServerPlayer player) {
            // Force sync the player's velocity if it was capped
            player.needsSync = true;
            player.hurtMarked = true;
        }
    }
}
