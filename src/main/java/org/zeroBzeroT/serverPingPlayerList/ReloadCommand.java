package org.zeroBzeroT.serverPingPlayerList;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * ReloadCommand
 */
public class ReloadCommand extends Command {
    private final Plugin plugin;

    public ReloadCommand(Plugin plugin) {
        super("spplreload");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender != ProxyServer.getInstance().getConsole()) {
            sender.sendMessage(new TextComponent("§cUnknown command. Type \"/help\" for help."));
            return;
        }

        // Config reload
        try {
            sender.sendMessage(new TextComponent("§3Reloading config..."));

            Config.getConfig(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}