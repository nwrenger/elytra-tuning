package io.github.nwrenger.elytratuning;

import io.github.nwrenger.elytratuning.network.FabricNetworking;
import net.fabricmc.api.ClientModInitializer;

public class ElytraTuningClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricNetworking.registerClientReceiver();
    }
}
