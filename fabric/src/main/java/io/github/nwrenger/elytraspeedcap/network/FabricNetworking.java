package io.github.nwrenger.elytraspeedcap.network;

import io.github.nwrenger.elytraspeedcap.client.ClientState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class FabricNetworking {

    private FabricNetworking() {}

    public static void registerPayload() {
        PayloadTypeRegistry.playS2C().register(
            SyncMaxSpeedPayload.ID,
            SyncMaxSpeedPayload.CODEC
        );
    }

    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(
            SyncMaxSpeedPayload.ID,
            (payload, context) ->
                context.client().execute(() ->
                    ClientState.maxSpeed = payload.maxSpeed()
                )
        );
    }
}
