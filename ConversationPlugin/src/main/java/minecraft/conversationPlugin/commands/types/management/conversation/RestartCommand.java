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
 * Restart the conversation for the player and execute the node.
 * <br>
 * <p>
 * /conversation [player] [conversation] restart
 * <ul>
 *     <li>Restarts the conversation for the player.</li>
 *     <li>Executes the node.</li>
 * </ul>
 */
public class RestartCommand {

    /**
     * Execute the RESTART command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] args, Logger logger) {

        if (args.length != CommandLength.RESTART.length()) {
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

        String[] start = new String[]{args[0], args[1], CommandArguments.START.value()};

        // Execute START command
        if (!InitiateCommand.execute(sender, conversation, player, start, logger)) {
            return false;
        }

        return CommandController.notifyPlayer(sender, String.format("Conversation %s restarted for player %s.", conversation.name(), player), logger);
    }
}
