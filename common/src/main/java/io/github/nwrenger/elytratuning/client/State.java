package io.github.nwrenger.elytratuning.client;

import io.github.nwrenger.elytratuning.config.Config;

public final class State {

    private State() {}

    public static volatile Config config = Config.disabled();

    public static void reset() {
        config = Config.disabled();
    }
}
