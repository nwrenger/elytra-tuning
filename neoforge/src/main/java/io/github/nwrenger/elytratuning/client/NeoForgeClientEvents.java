package io.github.nwrenger.elytratuning.client;

import io.github.nwrenger.elytratuning.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class NeoForgeClientEvents {

    private NeoForgeClientEvents() {}

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        State.reset();
    }
}
