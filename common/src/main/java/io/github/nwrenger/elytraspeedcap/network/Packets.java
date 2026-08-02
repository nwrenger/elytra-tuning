package io.github.nwrenger.elytraspeedcap.network;

import io.github.nwrenger.elytraspeedcap.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NonNull;

public final class Packets {

    private Packets() {}

    public static void syncMaxSpeed(
        @NonNull ServerPlayer player,
        double maxSpeed
    ) {
        Services.PLATFORM.sendToClient(
            player,
            new SyncMaxSpeedPayload(maxSpeed)
        );
    }
}
