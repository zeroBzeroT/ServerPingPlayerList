package de.deeprobin.playerlist.listener;

import de.deeprobin.playerlist.BungeePlugin;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class ServerListListener implements Listener {

    private final transient BungeePlugin plugin;

    public ServerListListener(BungeePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void handlePing(final ProxyPingEvent event){
        final ServerPing response = event.getResponse();
        final ServerPing.Players players = response.getPlayers();
        final ServerPing.PlayerInfo[] playerInfo = this.plugin.getProxy().getPlayers().stream().map(player -> new ServerPing.PlayerInfo(player.getName(), player.getUniqueId())).toArray(ServerPing.PlayerInfo[]::new);
        players.setSample(playerInfo);
        response.setPlayers(players);
        event.setResponse(response);
    }

}
