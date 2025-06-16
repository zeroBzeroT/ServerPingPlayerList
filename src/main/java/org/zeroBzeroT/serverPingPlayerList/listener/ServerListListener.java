package org.zeroBzeroT.serverPingPlayerList.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;
import org.zeroBzeroT.serverPingPlayerList.Config;
import org.zeroBzeroT.serverPingPlayerList.Main;

import java.util.concurrent.TimeUnit;


public final class ServerListListener {
    private final Main plugin;
    private final Logger logger;
    private Config config;
    private volatile ServerPing cachedPing;


    public ServerListListener(Main plugin, Logger logger, Config config) {
        this.plugin = plugin;
        this.logger = logger;
        this.config = config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void startCachingPing() {
        plugin.getServer().getScheduler().buildTask(plugin, () -> plugin.getServer().getServer(config.getValue("mainServer")).ifPresent(server -> {
            server.ping().thenAccept(ping -> cachedPing = ping);
        })).repeat(5, TimeUnit.SECONDS).schedule();
    }

    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        var pingResponseBuilder = event.getPing().asBuilder();
        pingResponseBuilder.version(event.getPing().getVersion());

        if (config.getBoolean("messageOfTheDayOverride")) {
            pingResponseBuilder.description(Component.text(config.getValue("messageOfTheDay")));
        }

        // THIS IS WHERE I GOT ERRORS!

        //if (config.getBoolean("setHoverInfo")) {
        //    if (config.getBoolean("useMainServer") && cachedPing != null) {
        //        pingResponseBuilder.onlinePlayers(cachedPing.getPlayersOnline());
        //        pingResponseBuilder.samplePlayers(cachedPing.getPlayers().getSample());
        //    } else {
        //        pingResponseBuilder.samplePlayers(plugin.getServer().getAllPlayers().stream()
        //                .map(player -> new com.velocitypowered.api.proxy.player.Profile(player.getUniqueId(), player.getUsername()))
        //                .toArray(com.velocitypowered.api.proxy.player.Profile[]::new));
        //        pingResponseBuilder.onlinePlayers(plugin.getServer().getPlayerCount());
        //    }
        //}

        event.setPing(pingResponseBuilder.build());
    }
}