package org.zeroBzeroT.serverPingPlayerList;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.bstats.bungeecord.Metrics;
import org.zeroBzeroT.serverPingPlayerList.listener.ServerListListener;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class Main extends Plugin {
    public ServerPing mainPing = null;

    ScheduledTask pingTask = null;

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

        // Server Ping Update Task
        StartPingTask();

        // Commands
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand(this));

        // register the listener
        this.getProxy().getPluginManager().registerListener(this, new ServerListListener(this));

        // Some output to the console ;)
        log("config", "§3Version Name: §r" + ChatColor.translateAlternateColorCodes('&', Config.versionName));
        log("config", "§3Version Minimum Protocol: §r" + Config.versionMinProtocol);
        log("config", "§3Set Hover Info: §r" + Config.setHoverInfo);
        if (Config.messageOfTheDayOverride)
            log("config", "§3Message Of The Day: §r" + ChatColor.translateAlternateColorCodes('&', Config.messageOfTheDay));
        if (Config.useMainServer)
            log("config", "§3Ping Pass-Through-Server: §r" + Config.mainServer);

        // Load Plugin Metrics
        if (Config.bStats) {
            new Metrics(this, 16229);
        }
    }

    void StartPingTask() {
        mainPing = null;

        if (pingTask != null)
            pingTask.cancel();

        if (Config.useMainServer) {
            Callback<ServerPing> pingBack = (result, error) -> mainPing = result;

            pingTask = getProxy().getScheduler().schedule(this,
                    () -> ProxyServer.getInstance().getServerInfo(Config.mainServer).ping(pingBack),
                    5, 5, TimeUnit.SECONDS
            );
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (pingTask != null)
            pingTask.cancel();

        getProxy().getPluginManager().unregisterListeners(this);
    }

    public void log(String module, String message) {
        getLogger().info("§a[" + module + "] §e" + message + "§r");
    }
}
