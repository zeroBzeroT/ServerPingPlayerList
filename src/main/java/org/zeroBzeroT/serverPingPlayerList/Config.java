package org.zeroBzeroT.serverPingPlayerList;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Config {
    private final Path configPath;
    private final Logger logger;
    private Map<String, Object> configValues;

    public Config(Path dataDirectory, Logger logger) {
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
            Files.createFile(configPath);
            try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
                writer.write("versionName: ZeroPaper 1.12.2+\n");
                writer.write("versionMinProtocol: 340\n");
                writer.write("setHoverInfo: true\n");
                writer.write("messageOfTheDayOverride: false\n");
                writer.write("messageOfTheDay: ''\n");
                writer.write("bStats: false\n");
                writer.write("useMainServer: false\n");
                writer.write("mainServer: 'main'\n");
            }
            logger.info("Default config created.");
        } catch (IOException e) {
            logger.error("Failed to create default configuration!", e);
        }
    }

    public String getValue(String key) {
        return configValues.getOrDefault(key, "").toString();
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(configValues.getOrDefault(key, "false").toString());
    }
}
