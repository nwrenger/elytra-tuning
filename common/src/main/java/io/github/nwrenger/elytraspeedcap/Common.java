package io.github.nwrenger.elytraspeedcap;

import io.github.nwrenger.elytraspeedcap.network.Packets;
import io.github.nwrenger.elytraspeedcap.platform.Services;
import java.util.Objects;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Common {

    private static Config config;

    public static void init() {
        config = Config.load();

        Services.PLATFORM.registerEndServerTick(ElytraSpeedHandler::tick);
        Services.PLATFORM.registerPlayerJoin(player ->
            Packets.syncMaxSpeed(player, getConfig().max_speed)
        );

        Constants.LOG.info("[Elytra Speed Cap] Started successfully");
    }

    public static Config getConfig() {
        return Objects.requireNonNull(config);
    }

    @Nullable
    public static Vec3 capElytraVelocity(double maxSpeed, Vec3 velocity) {
        double maxSpeedPerTick = Config.intoMaxSpeedPerTick(maxSpeed);
        if (maxSpeedPerTick <= 0.0D) {
            return null;
        }

        double horizontal = Math.sqrt(
            velocity.x * velocity.x + velocity.z * velocity.z
        );
        if (horizontal <= maxSpeedPerTick) {
            return null;
        }

        double factor = maxSpeedPerTick / horizontal;
        Vec3 capped = velocity.scale(factor);

        Constants.LOG.debug(
            "[Elytra Speed Cap] Capping Elytra velocity from {} to {} (factor={}) oldVel={} newVel={}",
            horizontal,
            maxSpeedPerTick,
            factor,
            velocity,
            capped
        );

        return capped;
    }
}
