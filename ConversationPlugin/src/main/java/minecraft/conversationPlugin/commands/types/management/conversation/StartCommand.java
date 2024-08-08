package minecraft.conversationPlugin.commands.types.management.conversation;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.controller.ConversationController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

/**
 * <p>
 * Start the conversation for the player and execute the node.
 * <br>
 * <p>
 * /conversation [player] [conversation] start
 * <ul>
 *     <li>Adds player to the conversation.</li>
 *     <li>Executes the node.</li>
 * </ul>
 */
public class StartCommand {

    /**
     * Execute the START command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param args         Command arguments
     * @param logger       Logger
     * @param plugin       Plugin
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] args, Logger logger, Plugin plugin) {

        if (args.length != CommandLength.START.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        String[] initiate = new String[]{args[0], args[1], CommandArguments.INITIATE.value()};

        // Execute INITIATE command
        if (!InitiateCommand.execute(sender, conversation, player, initiate, logger)) {
            return false;
        }

        ConversationController.executeConversation(conversation, player, plugin);

        return CommandController.notifyPlayer(sender, String.format("Conversation %s started for player %s.", conversation.name(), player), logger);
    }
}
