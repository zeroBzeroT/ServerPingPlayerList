package org.zeroBzeroT.serverPingPlayerList.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.proxy.server.ServerPing.Players;
import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer;
import net.kyori.adventure.text.Component;
import org.zeroBzeroT.serverPingPlayerList.Config;
import org.zeroBzeroT.serverPingPlayerList.Main;

import java.util.List;
import java.util.stream.Collectors;

public final class ServerListListener {
    private final Main plugin;
    private Config config;

    public ServerListListener(Main plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Subscribe
    public void onProxyPing(final ProxyPingEvent event) {
        var pingResponseBuilder = event.getPing().asBuilder();

        // Version
        int protocol = pingResponseBuilder.getVersion().getProtocol();
        String name = config.getString("versionName");
        if (protocol < config.getInt("versionMinProtocol")) protocol = config.getInt("versionMinProtocol");
        pingResponseBuilder.version(new ServerPing.Version(protocol, name));

        // Message of the day
        if (config.getBoolean("messageOfTheDayOverride")) {
            pingResponseBuilder.description(Component.text(config.getString("messageOfTheDay")));
        }

        // Server info player list
        if (config.getBoolean("setHoverInfo")) {
            int onlinePlayersCount;
            List<SamplePlayer> sampleProfiles;

            if (config.getBoolean("useMainServer") && plugin.getMainPing() != null && plugin.getMainPing().getPlayers().isPresent()) {
                Players players = plugin.getMainPing().getPlayers().get();

                onlinePlayersCount = players.getOnline();
                sampleProfiles = players.getSample();
            } else {
                onlinePlayersCount = plugin.getServer().getPlayerCount();
                sampleProfiles = plugin.getServer().getAllPlayers().stream()
                        .map(player -> new SamplePlayer(player.getUsername(), player.getUniqueId()))
                        .collect(Collectors.toList());
            }

            pingResponseBuilder.onlinePlayers(onlinePlayersCount);

            SamplePlayer[] array = new SamplePlayer[sampleProfiles.size()];
            pingResponseBuilder.samplePlayers(sampleProfiles.toArray(array));
        }

        event.setPing(pingResponseBuilder.build());
    }
}