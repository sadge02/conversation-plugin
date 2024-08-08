package minecraft.conversationPlugin.commands.types.flow;

import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.controller.ConversationController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

/**
 * <p>
 * Execute the node.
 * <br>
 * <p>
 * /conversation [player] [conversation] run
 * <ul>
 *     <li>Executes the node.</li>
 * </ul>
 */
public class RunCommand {

    /**
     * Execute the RUN command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param strings      Command arguments
     * @param logger       Logger
     * @param plugin       Plugin
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] strings, Logger logger, Plugin plugin) {

        if (strings.length != CommandLength.RUN.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        ConversationController.executeConversation(conversation, player, plugin);

        return CommandController.notifyPlayer(sender, String.format("Conversation %s has been executed for player %s.", conversation.name(), player), logger);
    }
}
