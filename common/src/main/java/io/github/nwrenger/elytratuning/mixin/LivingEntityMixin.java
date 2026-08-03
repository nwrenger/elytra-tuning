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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(
        method = "updateFallFlyingMovement",
        at = @At("RETURN"),
        cancellable = true
    )
    private void elytraTuning$capFallFlyingVelocity(
        Vec3 input,
        CallbackInfoReturnable<Vec3> callback
    ) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Config config = entity.level().isClientSide()
            ? State.config
            : Common.getConfig();

        Vec3 capped = Common.capElytraVelocity(
            config,
            callback.getReturnValue()
        );
        if (capped != null) {
            callback.setReturnValue(capped);
        }

        if (entity instanceof ServerPlayer player) {
            player.needsSync = true;
            player.hurtMarked = true;
        }
    }
}
