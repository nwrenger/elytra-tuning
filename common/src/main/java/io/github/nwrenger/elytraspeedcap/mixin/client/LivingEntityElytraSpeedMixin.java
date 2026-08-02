package io.github.nwrenger.elytraspeedcap.mixin.client;

import io.github.nwrenger.elytraspeedcap.Common;
import io.github.nwrenger.elytraspeedcap.client.ClientState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityElytraSpeedMixin {

    @Inject(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
            ordinal = 0
        ),
        cancellable = true
    )
    private void elytraSpeedCap$capFallFlyingVelocity(Vec3 input, CallbackInfo callback) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.isFallFlying()) {
            return;
        }

        Vec3 capped = Common.capElytraVelocity(
            ClientState.maxSpeed,
            entity.getDeltaMovement()
        );
        if (capped != null) {
            entity.setDeltaMovement(capped);
            callback.cancel();
        }
    }
}
