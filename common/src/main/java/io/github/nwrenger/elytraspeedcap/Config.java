package io.github.nwrenger.elytraspeedcap;

import com.google.gson.GsonBuilder;
import io.github.nwrenger.elytraspeedcap.platform.Services;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

public class Config {

    public static final double TICKS_PER_SECOND = 20.0D;

    private static final File CONFIG_FILE = new File(
        Services.PLATFORM.getConfigDir().toFile(),
        "elytra-speed-cap.json"
    );

    public double max_speed = 60.0D;

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath())) {
            new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
        } catch (IOException exception) {
            Constants.LOG.error(
                "[Elytra Speed Cap] IO error while trying to save config file",
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
            return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .fromJson(reader, Config.class);
        } catch (IOException exception) {
            Constants.LOG.error(
                "[Elytra Speed Cap] Unable to read config file, using default config",
                exception
            );
            return new Config();
        }
    }

    public static double intoMaxSpeedPerTick(double maxSpeed) {
        return maxSpeed / TICKS_PER_SECOND;
    }
}
