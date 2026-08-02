package io.github.nwrenger.elytraspeedcap.network;

import io.github.nwrenger.elytraspeedcap.client.ClientState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NeoForgeNetworking {

    private NeoForgeNetworking() {}

    public static void register(IEventBus eventBus) {
        eventBus.addListener(NeoForgeNetworking::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1").optional();
        registrar.playToClient(
            SyncMaxSpeedPayload.ID,
            SyncMaxSpeedPayload.CODEC,
            (payload, context) ->
                context.enqueueWork(() ->
                    ClientState.maxSpeed = payload.maxSpeed()
                )
        );
    }
}
