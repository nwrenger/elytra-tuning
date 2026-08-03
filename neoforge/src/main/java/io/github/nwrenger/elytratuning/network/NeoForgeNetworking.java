package io.github.nwrenger.elytratuning.network;

import io.github.nwrenger.elytratuning.client.State;
import io.github.nwrenger.elytratuning.config.Config;
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
            SyncConfigPayload.ID,
            SyncConfigPayload.CODEC,
            (payload, context) ->
                context.enqueueWork(
                    () -> State.config = Config.fromJson(payload.configJson())
                )
        );
    }
}
