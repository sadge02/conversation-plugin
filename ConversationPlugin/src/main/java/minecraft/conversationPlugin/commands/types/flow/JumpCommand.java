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
 * Jump to a specific node in the conversation for the player.
 * <br>
 * <p>
 * /conversation [player] [conversation] jump [node]
 * <ul>
 *     <li>Moves player to the node.</li>
 *     <li>Executes the node.</li>
 *     <li>Ignores requirements.</li>
 * </ul>
 * <p>
 * /conversation [player] [conversation] jump next
 * <ul>
 *     <li>Moves player to the next node.</li>
 *     <li>Executes the node.</li>
 *     <li>Ignores requirements.</li>
 * </ul>
 */
public class JumpCommand {

    /**
     * Execute the JUMP command.
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

        if (strings.length != CommandLength.JUMP.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        String[] set = new String[]{strings[0], strings[1], CommandArguments.SET.value(), strings[3]};

        // Execute the SET command
        if (!SetCommand.execute(sender, conversation, player, set, logger)) {
            return false;
        }

        ConversationController.executeConversation(conversation, player, plugin);

        return true;
    }
}
