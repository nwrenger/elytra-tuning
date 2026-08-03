package io.github.nwrenger.elytratuning.network;

import io.github.nwrenger.elytratuning.config.Config;
import io.github.nwrenger.elytratuning.platform.Services;
import net.minecraft.server.level.ServerPlayer;

public final class Packets {

    private Packets() {}

    public static void syncConfig(ServerPlayer player, Config config) {
        Services.PLATFORM.sendToClient(
            player,
            new SyncConfigPayload(config.toJson())
        );
    }
}
