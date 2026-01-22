package net.gopa.mc.whooshwhoosh;

import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

public final class Configuration {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "whooshwhoosh.json");

    public static Configuration load() {
        Configuration configuration = new Configuration();
        if (!CONFIG_FILE.exists()) {
            save(configuration);
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath())) {
            configuration = (new GsonBuilder().setPrettyPrinting().create()).fromJson(reader, Configuration.class);
        } catch (IOException e) {
        }

        return configuration;
    }

    public static void save(Configuration config) {
        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath())) {
            (new GsonBuilder().setPrettyPrinting().create()).toJson(config, writer);
        } catch (IOException e) {
        }
    }
}
