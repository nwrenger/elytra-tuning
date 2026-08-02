package io.github.nwrenger.elytraspeedcap.mixin.client;

import io.github.nwrenger.elytraspeedcap.Common;
import io.github.nwrenger.elytraspeedcap.client.ClientState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityElytraSpeedMixin {

    @Inject(method = "updateFallFlyingMovement", at = @At("RETURN"), cancellable = true)
    private void elytraSpeedCap$capFallFlyingVelocity(
        Vec3 input,
        CallbackInfoReturnable<Vec3> callback
    ) {
        Vec3 capped = Common.capElytraVelocity(
            ClientState.maxSpeed,
            callback.getReturnValue()
        );
        if (capped != null) {
            callback.setReturnValue(capped);
        }
    }
}
