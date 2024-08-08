package minecraft.conversationPlugin.commands.types.flow;

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
 * Continue the conversation for the player.
 * <br>
 * <p>
 * /conversation [player] [conversation] continue
 * <ul>
 *     <li>Moves player to the next node.</li>
 *     <li>Executes the node.</li>
 *     <li>Does not ignore requirements.</li>
 * </ul>
 */
public class ContinueCommand {

    /**
     * Execute the CONTINUE command.
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

        if (strings.length != CommandLength.CONTINUE.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        String[] next = new String[]{strings[0], strings[1], CommandArguments.NEXT.value()};

        // Execute the NEXT command
        if (!NextCommand.execute(sender, conversation, player, next, logger)) {
            return false;
        }

        ConversationController.executeConversation(conversation, player, plugin);

        return true;
    }
}
