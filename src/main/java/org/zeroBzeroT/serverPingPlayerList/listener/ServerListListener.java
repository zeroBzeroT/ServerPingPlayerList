package org.zeroBzeroT.serverPingPlayerList.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.zeroBzeroT.serverPingPlayerList.Config;
import org.zeroBzeroT.serverPingPlayerList.Main;

public final class ServerListListener implements Listener {

    private final transient Main plugin;

    public ServerListListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProxyPing(final ProxyPingEvent event) {
        final ServerPing response = event.getResponse();

        // Version
        ServerPing.Protocol version = response.getVersion();
        version.setName(Config.versionName);
        if (version.getProtocol() < Config.versionMinProtocol) version.setProtocol(Config.versionMinProtocol);
        response.setVersion(version);

        // message of the day
        if (Config.messageOfTheDayOverride) {
            response.setDescriptionComponent(
                    new TextComponent(ChatColor.translateAlternateColorCodes('&', Config.messageOfTheDay))
            );
        }

        // Server info player list
        if (Config.setHoverInfo) {
            final ServerPing.Players players = response.getPlayers();
            final ServerPing.PlayerInfo[] playerHoverInfo = this.plugin.getProxy().getPlayers().stream().map(player -> new ServerPing.PlayerInfo(player.getName(), player.getUniqueId())).toArray(ServerPing.PlayerInfo[]::new);
            players.setSample(playerHoverInfo);
        }

        event.setResponse(response);
    }
}
