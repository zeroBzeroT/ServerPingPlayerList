package de.deeprobin.playerlist;

import de.deeprobin.playerlist.listener.ServerListListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public final class Main extends Plugin {
    @Override
    public void onEnable() {
        super.onEnable();

        String folder = getDataFolder().getPath();

        // Create path directories if not existent
        //noinspection ResultOfMethodCallIgnored
        new File(folder).mkdirs();

        // Config
        try {
            Config.getConfig(this);
        } catch (Exception e) {
            e.printStackTrace();
            onDisable();
            return;
        }

        // register the listener
        this.getProxy().getPluginManager().registerListener(this, new ServerListListener(this));

        // Some output to the console ;)
        log("§3Version Name: §r" + ChatColor.translateAlternateColorCodes('&', Config.versionName));
        log("§3Version Minimum Protocol: §r" + Config.versionMinProtocol);
        log("§3Set Hover Info: §r" + Config.setHoverInfo);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        getProxy().getPluginManager().unregisterListeners(this);
    }

    public void log(String message) {
        getLogger().info("§e" + message + "§r");
    }
}
