package io.github.nwrenger.elytraspeedcap;

import io.github.nwrenger.elytraspeedcap.network.FabricNetworking;
import net.fabricmc.api.ClientModInitializer;

public class ElytraSpeedCapClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricNetworking.registerClientReceiver();
    }
}
