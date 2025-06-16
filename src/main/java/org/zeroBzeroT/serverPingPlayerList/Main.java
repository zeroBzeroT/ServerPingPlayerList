package org.zeroBzeroT.serverPingPlayerList;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.scheduler.ScheduledTask;
import org.slf4j.Logger;
import org.zeroBzeroT.serverPingPlayerList.listener.ServerListListener;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Main {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private Config config;
    private ServerListListener serverListListener;

    private ScheduledTask pingTask;
    private volatile ServerPing mainPing; // Cached ping from main server, similar to old mainPing

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("ServerPingPlayerList is starting up...");

        config = new Config(dataDirectory, logger);

        // Register Event Listeners
        serverListListener = new ServerListListener(this, config);
        server.getEventManager().register(this, serverListListener);

        // Commands
        registerCommands();

        // Server Ping Update Task
        startPingTask();

        // Some output to the console ;)
        logConfig();

        // TODO Load Plugin Metrics
        if (config.getBoolean("bStats")) {
            // bStats is Velocity Metrics v2, so you would prolly need to set up differently if you want to support it.
            // so this is js a placeholder for your metrics setup if needed.
        }
    }

    private void registerCommands() {
        CommandManager commandManager = server.getCommandManager();
        commandManager.register("spplreload", new ReloadCommand(this, logger));
    }

    public ProxyServer getServer() {
        return server;
    }

    public ServerPing getMainPing() {
        return mainPing;
    }

    public void reloadConfig() {
        logger.info("Reloading configuration...");
        Config newConfig = new Config(dataDirectory, logger);
        this.config = newConfig;
        serverListListener.setConfig(newConfig);
        startPingTask();
    }

    public void startPingTask() {
        // cancel previous task if running
        if (pingTask != null) {
            pingTask.cancel();
        }

        mainPing = null;

        if (config.getBoolean("useMainServer")) {
            Optional<RegisteredServer> mainServer = server.getServer(config.getString("mainServer"));
            if (mainServer.isEmpty()) {
                logger.warn("Main server '{}' not found! Ping task will not start.", config.getString("mainServer"));
                return;
            }

            // Schedule repeating task to update mainPing every 5 seconds
            pingTask = server.getScheduler().buildTask(this, () -> mainServer.get().ping().thenAccept(ping -> {
                mainPing = ping;
                logger.debug("Cached ping updated for main server '{}'", config.getString("mainServer"));
            }).exceptionally(t -> {
                logger.error("Failed to ping main server '{}'", config.getString("mainServer"), t);
                return null;
            })).repeat(5, TimeUnit.SECONDS).schedule();
        }
    }

    private void logConfig() {
        logger.info("[config] Version Name: {}", config.getString("versionName"));
        logger.info("[config] Version Minimum Protocol: {}", config.getString("versionMinProtocol"));
        logger.info("[config] Set Hover Info: {}", config.getString("setHoverInfo"));
        if (config.getBoolean("messageOfTheDayOverride")) {
            logger.info("[config] Message Of The Day: {}", config.getString("messageOfTheDay"));
        }
        if (config.getBoolean("useMainServer")) {
            logger.info("[config] Ping Pass-Through-Server: {}", config.getString("mainServer"));
        }
    }
}