package io.github.nwrenger.elytratuning.network;

import io.github.nwrenger.elytratuning.Constants;
import io.github.nwrenger.elytratuning.client.State;
import io.github.nwrenger.elytratuning.config.Config;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class FabricNetworking {

    private FabricNetworking() {}

    public static void registerPayload() {
        PayloadTypeRegistry.playS2C().register(
            SyncConfigPayload.ID,
            SyncConfigPayload.CODEC
        );
    }

    public static void registerClientReceiver() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
            State.reset()
        );
        ClientPlayNetworking.registerGlobalReceiver(
            SyncConfigPayload.ID,
            (payload, context) ->
                context
                    .client()
                    .execute(
                        () ->
                            State.config = Config.fromJson(payload.configJson())
                    )
        );
    }
}
