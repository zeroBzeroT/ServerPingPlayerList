package org.zeroBzeroT.serverPingPlayerList;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;

public class ReloadCommand implements SimpleCommand {
    private final Main plugin;
    private final Logger logger;

    public ReloadCommand(Main plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();

        // Check if the command is run from the console
        if (!(sender instanceof ConsoleCommandSource)) {
            sender.sendMessage(Component.text("This command can only be run from the console.", NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.text("Reloading config...", NamedTextColor.AQUA));
        try {
            plugin.reloadConfig();
            sender.sendMessage(Component.text("Configuration reloaded successfully!", NamedTextColor.GREEN));
        } catch (Exception e) {
            logger.error("Error while reloading config!", e);
            sender.sendMessage(Component.text("An error occurred while reloading configuration.", NamedTextColor.RED));
        }
    }
}