package io.github.nwrenger.elytratuning;

import io.github.nwrenger.elytratuning.config.Config;
import io.github.nwrenger.elytratuning.config.Speed;
import io.github.nwrenger.elytratuning.network.Packets;
import io.github.nwrenger.elytratuning.platform.Services;
import java.util.Objects;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Common {

    private static Config config;

    public static void init() {
        loadConfig();

        Services.PLATFORM.registerPlayerJoin(player ->
            Packets.syncConfig(player, getConfig())
        );

        Constants.LOG.info("[Elytra Tuning] Started successfully");
    }

    public static void loadConfig() {
        // Load and update state
        Config newConfig = Config.load();

        // Validate the config to ensure that all values are within acceptable ranges
        Config.validate(newConfig);

        // All is validated, so apply
        config = newConfig;
    }

    public static Config getConfig() {
        return Objects.requireNonNull(config);
    }

    @Nullable
    public static Vec3 capElytraVelocity(Config config, Vec3 velocity) {
        // Check if speed capping is enabled, otherwise return null to indicate no capping is needed
        Speed speedConfig = config.speed;
        if (speedConfig == null) {
            return null;
        }

        double maxSpeedPerTick = speedConfig.max / Constants.TICKS_PER_SECOND;

        // Get the speed based on the configured calculation method
        double speed = switch (speedConfig.calculation) {
            case HORIZONTAL -> Math.sqrt(
                velocity.x * velocity.x + velocity.z * velocity.z
            );
            case ABSOLUTE -> velocity.length();
        };

        // Check if the current speed exceeds the maximum allowed speed per tick
        // If it does not exceed, return null to indicate no capping is needed
        if (speed <= maxSpeedPerTick) {
            return null;
        }

        // Scale the velocity vector down to the maximum allowed speed while maintaining its direction
        double factor = maxSpeedPerTick / speed;
        Vec3 capped = velocity.scale(factor);

        Constants.LOG.debug(
            "[Elytra Tuning] Capping Elytra velocity from {} to {} (factor={}) oldVel={} newVel={} calc={}",
            speed,
            maxSpeedPerTick,
            factor,
            velocity,
            capped,
            speedConfig.calculation
        );

        return capped;
    }
}
