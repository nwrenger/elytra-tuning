package io.github.nwrenger.elytratuning;

import io.github.nwrenger.elytratuning.network.FabricNetworking;
import net.fabricmc.api.ModInitializer;

public class ElytraTuning implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricNetworking.registerPayload();
        Common.init();
    }
}
