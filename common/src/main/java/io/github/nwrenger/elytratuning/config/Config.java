package io.github.nwrenger.elytratuning.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.github.nwrenger.elytratuning.Constants;
import io.github.nwrenger.elytratuning.platform.Services;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import org.jetbrains.annotations.Nullable;

public class Config {

    private static final File CONFIG_FILE = new File(
        Services.PLATFORM.getConfigDir().toFile(),
        "elytra-tuning.json"
    );

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create();

    public @Nullable Speed speed = new Speed();
    public @Nullable Rocket rocket = new Rocket();

    public static Config disabled() {
        Config config = new Config();
        config.speed = null;
        config.rocket = null;
        return config;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath())) {
            GSON.toJson(this, writer);
        } catch (IOException exception) {
            Constants.LOG.error(
                "[Elytra Tuning] IO error while trying to save config file",
                exception
            );
        }
    }

    public static @Nullable Config load() {
        if (!CONFIG_FILE.exists()) {
            Config newConfig = new Config();
            newConfig.save();
            return newConfig;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath())) {
            return GSON.fromJson(reader, Config.class);
        } catch (IOException exception) {
            Constants.LOG.error(
                "[Elytra Tuning] Unable to read config file, using default config",
                exception
            );
            return new Config();
        }
    }

    public static void validate(@Nullable Config config) {
        if (config == null) {
            throw new IllegalStateException(
                "[Elytra Tuning] Config must not be null"
            );
        }

        if (config.speed != null) {
            if (config.speed.calculation == null) {
                throw new IllegalStateException(
                    "[Elytra Tuning] `speed.calculation` must not be null"
                );
            }

            if (
                !Double.isFinite(config.speed.max) || config.speed.max <= 0.0D
            ) {
                throw new IllegalStateException(
                    "[Elytra Tuning] `speed.max` must be finite and greater than 0"
                );
            }
        }

        if (config.rocket != null) {
            if (
                !Double.isFinite(config.rocket.strength) ||
                config.rocket.strength < 0.0D
            ) {
                throw new IllegalStateException(
                    "[Elytra Tuning] `rocket.strength` must be finite and greater than or equal to 0"
                );
            }

            if (
                !Double.isFinite(config.rocket.duration) ||
                config.rocket.duration < 0.0D
            ) {
                throw new IllegalStateException(
                    "[Elytra Tuning] `rocket.duration` must be finite and greater than or equal to 0"
                );
            }
        }
    }

    public String toJson() {
        return GSON.toJson(this);
    }

    public static Config fromJson(String json) {
        try {
            Config config = GSON.fromJson(json, Config.class);
            validate(config);
            Constants.LOG.info("[Elytra Tuning] Applied server config");
            return config;
        } catch (JsonParseException | IllegalStateException exception) {
            Constants.LOG.error(
                "[Elytra Tuning] Unable to read synchronized config, disabling client-side behavior",
                exception
            );
            return disabled();
        }
    }
}
