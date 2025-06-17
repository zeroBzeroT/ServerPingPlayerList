package org.zeroBzeroT.serverPingPlayerList;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Config {
    private final Main main;
    private final Path configPath;
    private final Logger logger;
    private Map<String, Object> configValues;

    public Config(Main main, Path dataDirectory, Logger logger) {
        this.main = main;
        this.configPath = dataDirectory.resolve("config.yml");
        this.logger = logger;
        loadConfig();
    }

    private void loadConfig() {
        if (!Files.exists(configPath)) {
            createDefaultConfig();
        }

        try (InputStream inputStream = Files.newInputStream(configPath)) {
            Yaml yaml = new Yaml();
            configValues = yaml.load(inputStream);
            logger.info("Configuration loaded successfully.");
        } catch (IOException e) {
            logger.error("Failed to load configuration!", e);
        }
    }

    private void createDefaultConfig() {
        try {
            Files.createDirectories(configPath.getParent());

            Files.createFile(configPath);

            try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
                writer.write("# Created on " + main.getServer().getVersion().getName() + " " + main.getServer().getVersion().getVersion() + "\n");
                writer.write("versionName: \"LimeWire 5.5.16\"\n");
                writer.write("versionMinProtocol: 340 # 1.12.2\n");
                writer.write("setHoverInfo: true\n");
                writer.write("messageOfTheDayOverride: false\n");
                writer.write("messageOfTheDay: \"A Minecraft Server\"\n");
                writer.write("bStats: false\n");
                writer.write("useMainServer: false\n");
                writer.write("mainServer: \"main\"\n");
            }
            logger.info("Default config created.");
        } catch (IOException e) {
            logger.error("Failed to create default configuration!", e);
        }
    }

    public String getString(String key) {
        return configValues.getOrDefault(key, "").toString();
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(configValues.getOrDefault(key, "false").toString());
    }

    public Integer getInt(String key) {
        return Integer.parseInt(configValues.getOrDefault(key, "false").toString());
    }

    void logConfig() {
        logger.info("Version Name: {}", getString("versionName"));
        logger.info("Version Minimum Protocol: {}", getString("versionMinProtocol"));
        logger.info("Set Hover Info: {}", getString("setHoverInfo"));
        if (getBoolean("messageOfTheDayOverride")) {
            logger.info("Message Of The Day: {}", getString("messageOfTheDay"));
        }
        if (getBoolean("useMainServer")) {
            logger.info("Ping Pass-Through-Server: {}", getString("mainServer"));
        }
    }
}
