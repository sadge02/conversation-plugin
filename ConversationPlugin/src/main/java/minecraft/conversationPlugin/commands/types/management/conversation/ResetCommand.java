package minecraft.conversationPlugin.commands.types.management.conversation;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.commands.types.management.player.RemoveCommand;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

/**
 * <p>
 * Reset the conversation for the player without executing the node.
 * <br>
 * <p>
 * /conversation [player] [conversation] reset
 * <ul>
 *     <li>Restarts the conversation for the player.</li>
 *     <li>Does not execute the node.</li>
 * </ul>
 */
public class ResetCommand {

    /**
     * Execute the RESET command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] args, Logger logger) {

        if (args.length != CommandLength.RESET.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        String[] remove = new String[]{args[0], args[1], CommandArguments.REMOVE.value()};

        // Execute REMOVE command
        if (!RemoveCommand.execute(sender, conversation, player, remove, logger)) {
            return false;
        }

        String[] initiate = new String[]{args[0], args[1], CommandArguments.INITIATE.value()};

        // Execute INITIATE command
        if (!InitiateCommand.execute(sender, conversation, player, initiate, logger)) {
            return false;
        }

        return CommandController.notifyPlayer(sender, String.format("Conversation %s reset for player %s.", conversation.name(), player), logger);
    }
}
