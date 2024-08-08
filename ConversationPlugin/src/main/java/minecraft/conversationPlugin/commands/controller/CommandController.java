package minecraft.conversationPlugin.commands.controller;

import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Controller for command management.
 */
public class CommandController {

    /**
     * Notify the command sender and log the message.
     *
     * @param sender  Command sender
     * @param message Message to send
     * @param logger  Logger
     * @return true
     */
    public static boolean notifyPlayer(CommandSender sender, String message, Logger logger) {

        if (sender instanceof Player player) {
            player.sendMessage(Component.text(message).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true));
        }

        logger.info(String.format("[CONVERSATION PLUGIN] %s", message));

        return true;
    }

    /**
     * Warn the command sender and log the message.
     *
     * @param sender  Command sender
     * @param message Message to send
     * @param logger  Logger
     * @return false
     */
    public static boolean warnPlayer(CommandSender sender, String message, Logger logger) {

        if (sender instanceof Player player) {
            player.sendMessage(Component.text(message).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, true));
        }

        logger.warning(String.format("[CONVERSATION PLUGIN] %s", message));

        return false;
    }

    /**
     * Add player to the conversation, notify or warn the command sender and log the message.
     *
     * @param conversation Conversation
     * @param player       Player to add
     * @param sender       Command sender
     * @param logger       Logger
     * @return true if the player was added to the conversation, false otherwise
     */
    public static boolean addPlayer(Conversation conversation, String player, CommandSender sender, Logger logger) {

        if (!conversation.hasPlayer(player)) {

            try {
                conversation.addPlayer(player, new PlayerInfo(player, new ArrayList<>(), new HashMap<>(), conversation.startNodeID()));
            } catch (Exception exception) {
                return CommandController.warnPlayer(sender, String.format("Error adding player %s to the conversation %s.", player, conversation.name()), logger);
            }
        } else {
            return CommandController.warnPlayer(sender, String.format("Player %s is already in the conversation %s.", player, conversation.name()), logger);
        }

        return CommandController.notifyPlayer(sender, String.format("Player %s has been added to the conversation %s.", player, conversation.name()), logger);
    }

    /**
     * Check if the player is in the conversation, notify the command sender and log the message.
     *
     * @param conversation Conversation
     * @param player       Player to check
     * @param sender       Command sender
     * @param logger       Logger
     * @return true if the player is in the conversation, false otherwise
     */
    public static boolean checkPlayer(Conversation conversation, String player, CommandSender sender, Logger logger) {

        if (!conversation.hasPlayer(player)) {
            return CommandController.warnPlayer(sender, String.format("Player %s is not in the conversation %s.", player, conversation.name()), logger);
        }

        return true;
    }

    /**
     * Remove a player from the conversation, notify or warn the command sender and log the message.
     *
     * @param conversation Conversation
     * @param player       Player to remove from the conversation
     * @param sender       Command sender
     * @param logger       Logger
     * @return true if the player was removed from the conversation, false otherwise
     */
    public static boolean removePlayer(Conversation conversation, String player, CommandSender sender, Logger logger) {

        if (checkPlayer(conversation, player, sender, logger)) {

            try {
                conversation.removePlayer(player);
            } catch (Exception exception) {
                return CommandController.warnPlayer(sender, String.format("Error removing player %s from conversation %s.", player, conversation.name()), logger);
            }

            return CommandController.notifyPlayer(sender, String.format("Conversation %s removed for player %s.", conversation.name(), player), logger);
        }

        return false;
    }
}
