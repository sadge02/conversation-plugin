package minecraft.conversationPlugin.commands.types.flow;

import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.types.NodeType;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

/**
 * <p>
 * Move the player to the next node in the conversation.
 * <br>
 * <p>
 * /conversation [player] [conversation] next
 * <ul>
 *     <li>Moves player to the next node.</li>
 *     <li>Does not execute the node.</li>
 *     <li>Does not ignore requirements.</li>
 * </ul>
 */
public class NextCommand {

    /**
     * Execute the NEXT command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param strings      Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] strings, Logger logger) {

        if (strings.length != CommandLength.NEXT.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        Node node = conversation.getNode(conversation.getPlayer(player).getCurrentNodeID());

        if (node == null) {
            return CommandController.warnPlayer(sender, String.format("The conversation %s has ended or player %s doesn't have a current node.", conversation.name(), player), logger);
        }

        if (node.getNodeType() == NodeType.CHOICE) {
            return CommandController.warnPlayer(sender, "Choice node doesn't have a determined next node.", logger);
        }

        // Check requirements
        if (!node.checkRequirements(conversation.getPlayer(player).getRequirements())) {
            return CommandController.warnPlayer(sender, "Player doesn't have requirements to get to the next node.", logger);
        }

        if (!conversation.hasNode(node.getNextNode())) {
            return CommandController.warnPlayer(sender, String.format("Conversation %s ended or node %s doesn't exist.", conversation.name(), node), logger);
        }

        try {
            conversation.getPlayer(player).setCurrentNode(node.getNextNode(), conversation);
        } catch (Exception exception) {
            return CommandController.warnPlayer(sender, String.format("Error in setting the next node for player %s in conversation %s.", player, conversation.name()), logger);
        }

        return CommandController.notifyPlayer(sender, String.format("Set to node %s in conversation %s for player %s.", node.getNextNode(), conversation.name(), player), logger);
    }
}
