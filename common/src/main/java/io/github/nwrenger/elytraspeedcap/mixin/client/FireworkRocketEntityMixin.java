package io.github.nwrenger.elytraspeedcap.mixin.client;

import io.github.nwrenger.elytraspeedcap.Common;
import io.github.nwrenger.elytraspeedcap.client.ClientState;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {

    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
        ),
        index = 0
    )
    private Vec3 elytraSpeedCap$modifyRocketBoost(Vec3 vanillaBoost) {
        Vec3 capped = Common.capElytraVelocity(
            ClientState.maxSpeed,
            vanillaBoost
        );
        return capped == null ? vanillaBoost : capped;
    }
}
