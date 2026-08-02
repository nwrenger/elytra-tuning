package io.github.nwrenger.elytraspeedcap;

import io.github.nwrenger.elytraspeedcap.network.FabricNetworking;
import net.fabricmc.api.ModInitializer;

public class ElytraSpeedCap implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricNetworking.registerPayload();
        Common.init();
    }
}
