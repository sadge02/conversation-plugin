package minecraft.conversationPlugin.commands.types.flow;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.types.NodeType;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

/**
 * <p>
 * Move the player to a specific node in the conversation.
 * <br>
 * <p>
 * /conversation [player] [conversation] set [node]
 * <ul>
 *     <li>Moves player to the node.</li>
 *     <li>Does not execute the node.</li>
 *     <li>Ignores requirements.</li>
 * </ul>
 * <p>
 * /conversation [player] [conversation] set next
 * <ul>
 *     <li>Moves player to the next node.</li>
 *     <li>Does not execute the node.</li>
 *     <li>Ignores requirements.</li>
 * </ul>
 */
public class SetCommand {

    /**
     * Execute the SET command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param strings      Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] strings, Logger logger) {

        if (strings.length != CommandLength.SET.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        String nodeID = strings[3];

        if (CommandArguments.NEXT.value().equals(nodeID)) {

            try {
                Node node = conversation.getNode(conversation.getPlayer(player).getCurrentNodeID());
                if (node.getNodeType() == NodeType.CHOICE) {
                    return CommandController.warnPlayer(sender, "Choice node doesn't have a determined next node.", logger);
                }
                nodeID = node.getNextNode();
            } catch (Exception exception) {
                return CommandController.warnPlayer(sender, String.format("Conversation %s ended or node %s doesn't exist.", conversation.name(), nodeID), logger);
            }
        }

        if (!conversation.hasNode(nodeID)) {
            return CommandController.warnPlayer(sender, String.format("Conversation %s ended or node %s doesn't exist.", conversation.name(), nodeID), logger);
        }

        try {
            conversation.getPlayer(player).setCurrentNode(nodeID, conversation);
        } catch (Exception exception) {
            return CommandController.warnPlayer(sender, String.format("Error setting node %s in conversation %s for player %s.", nodeID, conversation.name(), player), logger);
        }

        return CommandController.notifyPlayer(sender, String.format("Current node has been set to %s in conversation %s for player %s.", nodeID, conversation.name(), player), logger);
    }
}
