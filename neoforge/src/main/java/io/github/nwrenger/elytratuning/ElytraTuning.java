package io.github.nwrenger.elytratuning;

import io.github.nwrenger.elytratuning.network.NeoForgeNetworking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ElytraTuning {

    public ElytraTuning(IEventBus eventBus) {
        NeoForgeNetworking.register(eventBus);
        Common.init();
    }
}
