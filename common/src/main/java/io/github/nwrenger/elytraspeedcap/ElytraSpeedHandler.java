package io.github.nwrenger.elytraspeedcap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public final class ElytraSpeedHandler {

    private ElytraSpeedHandler() {}

    public static void tick(MinecraftServer server) {
        double maxSpeed = Common.getConfig().max_speed;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!player.isFallFlying()) {
                continue;
            }

            Vec3 capped = Common.capElytraVelocity(
                maxSpeed,
                player.getDeltaMovement()
            );
            if (capped == null) {
                continue;
            }

            player.setDeltaMovement(capped);
            player.needsSync = true;
            player.hurtMarked = true;
        }
    }
}
