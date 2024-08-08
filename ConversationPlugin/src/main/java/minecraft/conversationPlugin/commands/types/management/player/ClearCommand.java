package minecraft.conversationPlugin.commands.types.management.player;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

/**
 * <p>
 * Clear variables or requirements from the player in the conversation.
 * <br>
 * <p>
 * /conversation [player] [conversation] clear variables
 * <ul>
 *     <li>Clears variables from the player in the conversation.</li>
 * </ul>
 * <p>
 * /conversation [player] [conversation] clear requirements
 * <ul>
 *     <li>Clears requirements from the player in the conversation.</li>
 * </ul>
 */
public class ClearCommand {

    /**
     * Execute the CLEAR command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] args, Logger logger) {

        if (args.length > CommandLength.CLEAR.length()) {

            if (args[3].equals(CommandArguments.VARIABLES.value())) {
                return clearVariables(sender, conversation, player, args, logger);
            }

            if (args[3].equals(CommandArguments.REQUIREMENTS.value())) {
                return clearRequirements(sender, conversation, player, args, logger);
            }
        }

        return CommandController.warnPlayer(sender, "Invalid command.", logger);
    }

    /**
     * Clear variables from the player in the conversation.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the variables were cleared successfully
     */
    private static boolean clearVariables(CommandSender sender, Conversation conversation, String player, String[] args, Logger logger) {

        if (args.length != CommandLength.CLEAR_VARIABLES.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        try {
            conversation.getPlayer(player).clearVariables();
        } catch (Exception exception) {
            return CommandController.warnPlayer(sender, String.format("Error clearing variables in conversation %s for player %s.", conversation.name(), player), logger);
        }

        return CommandController.notifyPlayer(sender, String.format("Player %s's variables in conversation %s cleared.", player, conversation.name()), logger);
    }

    /**
     * Clear requirements from the player in the conversation.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the requirements were cleared successfully
     */
    private static boolean clearRequirements(CommandSender sender, Conversation conversation, String player, String[] args, Logger logger) {

        if (args.length != CommandLength.CLEAR_REQUIREMENTS.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        try {
            conversation.getPlayer(player).clearRequirements();
        } catch (Exception exception) {
            return CommandController.warnPlayer(sender, String.format("Error clearing requirements in conversation %s for player %s.", conversation.name(), player), logger);
        }

        return CommandController.notifyPlayer(sender, String.format("Player %s's requirements in conversation %s cleared.", player, conversation.name()), logger);
    }
}
