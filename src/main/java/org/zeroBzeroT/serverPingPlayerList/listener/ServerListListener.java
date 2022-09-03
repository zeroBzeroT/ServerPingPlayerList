package org.zeroBzeroT.serverPingPlayerList.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.zeroBzeroT.serverPingPlayerList.Config;
import org.zeroBzeroT.serverPingPlayerList.Main;
import org.zeroBzeroT.serverPingPlayerList.ServerInfoRetriever;

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
            ServerPing ping = null;

            if (Config.useMainServer) {
                try {
                    ping = ServerInfoRetriever.getServerPing(Config.mainServer);
                } catch (Exception e) {
                    plugin.log("onProxyPing", "Error retrieving server ping from '" + Config.mainServer + "': " + e.getClass().getSimpleName());
                }

                if (ping != null) {
                    final ServerPing.Players players = response.getPlayers();
                    players.setSample(ping.getPlayers().getSample());
                    players.setOnline(ping.getPlayers().getSample().length);
                }
            }

            if (ping == null || !Config.useMainServer) {
                final ServerPing.Players players = response.getPlayers();
                final ServerPing.PlayerInfo[] playerHoverInfo = this.plugin.getProxy().getPlayers().stream().map(player -> new ServerPing.PlayerInfo(player.getName(), player.getUniqueId())).toArray(ServerPing.PlayerInfo[]::new);
                players.setSample(playerHoverInfo);
                players.setOnline(this.plugin.getProxy().getPlayers().size());
            }
        }

        event.setResponse(response);
    }
}
