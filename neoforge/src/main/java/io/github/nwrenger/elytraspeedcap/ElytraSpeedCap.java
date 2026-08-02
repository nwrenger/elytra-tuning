package io.github.nwrenger.elytraspeedcap;

import io.github.nwrenger.elytraspeedcap.network.NeoForgeNetworking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ElytraSpeedCap {

    public ElytraSpeedCap(IEventBus eventBus) {
        NeoForgeNetworking.register(eventBus);
        Common.init();
    }
}
