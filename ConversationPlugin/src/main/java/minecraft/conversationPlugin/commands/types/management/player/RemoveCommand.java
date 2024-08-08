package minecraft.conversationPlugin.commands.types.management.player;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

/**
 * <p>
 * Remove player, requirement of the player, or variable of the player from the conversation.
 * <br>
 * <p>
 * /conversation [player] [conversation] remove
 * <ul>
 *     <li>Removes player from the conversation.</li>
 * </ul>
 * <p>
 * /conversation [player] [conversation] remove requirement [requirement]
 * <ul>
 *     <li>Removes requirement from the player in the conversation.</li>
 * </ul>
 * <p>
 * /conversation [player] [conversation] remove variable [variable]
 * <ul>
 *     <li>Removes variable from the player in the conversation.</li>
 * </ul>
 */
public class RemoveCommand {

    /**
     * Execute the REMOVE command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param strings      Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] strings, Logger logger) {

        if (strings.length > CommandLength.REMOVE.length()) {

            if (strings[3].equals(CommandArguments.VARIABLE.value())) {
                return removeVariable(sender, conversation, player, strings, logger);
            }

            if (strings[3].equals(CommandArguments.REQUIREMENT.value())) {
                return removeRequirement(sender, conversation, player, strings, logger);
            }

            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (strings.length != CommandLength.REMOVE.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        return CommandController.removePlayer(conversation, player, sender, logger);
    }

    /**
     * Remove variable from the player in the conversation.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param strings      Command arguments
     * @param logger       Logger
     * @return true if the variable was removed successfully
     */
    private static boolean removeVariable(CommandSender sender, Conversation conversation, String player, String[] strings, Logger logger) {

        if (strings.length != CommandLength.REMOVE_VARIABLE.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        try {
            conversation.getPlayer(player).removeVariable(strings[4]);
        } catch (Exception exception) {
            return CommandController.warnPlayer(sender, String.format("Error removing variable %s in conversation %s for player %s.", strings[4], conversation.name(), player), logger);
        }

        return CommandController.notifyPlayer(sender, String.format("Variable %s in conversation %s removed for player %s.", strings[4], conversation.name(), player), logger);
    }

    /**
     * Remove requirement from the player in the conversation.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param strings      Command arguments
     * @param logger       Logger
     * @return true if the requirement was removed successfully
     */
    private static boolean removeRequirement(CommandSender sender, Conversation conversation, String player, String[] strings, Logger logger) {

        if (strings.length != CommandLength.REMOVE_REQUIREMENT.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        try {
            conversation.getPlayer(player).removeRequirement(strings[4]);
        } catch (Exception exception) {
            return CommandController.notifyPlayer(sender, String.format("Error removing requirement %s in conversation %s for player %s.", strings[4], conversation.name(), player), logger);
        }

        return CommandController.notifyPlayer(sender, String.format("Requirement %s in conversation %s removed for player %s.", strings[4], conversation.name(), player), logger);
    }
}
