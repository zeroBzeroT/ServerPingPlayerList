package de.deeprobin.playerlist;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

@SuppressWarnings("CanBeFinal")
public class Config {
    public static String versionName = "ZeroPaper 1.12.2+";
    public static int versionMinProtocol = 340;
    public static boolean setHoverInfo = true;

    /**
     * Loads a config file, and if it doesn't exist creates one
     *
     * @param plugin Bungee plugin
     */
    static void getConfig(Plugin plugin) throws Exception {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (configFile.exists()) {
            loadConfig(configFile);
        } else {
            try {
                InputStream in = plugin.getResourceAsStream("config.yml");
                Files.copy(in, configFile.toPath());
                loadConfig(configFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Load the config from the plugin data folder
     *
     * @param configFile Path to the configuration file
     */
    static void loadConfig(File configFile) throws IOException {
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

        Arrays.asList(Config.class.getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                field.set(Config.class, config.get(field.getName()));
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
        });
    }
}
