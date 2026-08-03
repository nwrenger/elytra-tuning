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

    public static final double TICKS_PER_SECOND = 20.0D;

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

    public static Config load() {
        if (!CONFIG_FILE.exists()) {
            Config newConfig = new Config();
            newConfig.save();
            return newConfig;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath())) {
            Config config = GSON.fromJson(reader, Config.class);
            return config != null ? config : new Config();
        } catch (IOException | JsonParseException exception) {
            Constants.LOG.error(
                "[Elytra Tuning] Unable to read config file, using default config",
                exception
            );
            return new Config();
        }
    }

    public String toJson() {
        return GSON.toJson(this);
    }

    public static Config fromJson(String json) {
        try {
            Config config = GSON.fromJson(json, Config.class);
            return config != null ? config : disabled();
        } catch (JsonParseException exception) {
            Constants.LOG.error(
                "[Elytra Tuning] Unable to read synchronized config, disabling client-side behavior",
                exception
            );
            return disabled();
        }
    }
}
