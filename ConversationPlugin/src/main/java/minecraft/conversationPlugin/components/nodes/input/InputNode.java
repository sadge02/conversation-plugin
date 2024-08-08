package minecraft.conversationPlugin.components.nodes.input;

import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.text.Text;
import org.bukkit.entity.Player;

/**
 * Class for InputNode objects.
 */
public class InputNode extends Node {

    private final String text;
    private final String variable;

    public InputNode(Node node, String text, String variable) {
        super(node.getNode(), node.getNodeType(), node.getTrigger(), node.getRequirements(), node.getCommands(), node.getNextNode());
        this.text = text;
        this.variable = variable;
    }

    /**
     * Executes the InputNode.
     *
     * @param player       Player
     * @param conversation Conversation
     * @param playerFlags  PlayerFlags
     * @param playerInfo   PlayerInfo
     */
    public void executeInputNode(Player player, Conversation conversation, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        // Send chat message if it's not empty
        if (!text.isEmpty()) {
            player.sendMessage(Text.processStringMessage(text, playerInfo, player.getName()));
        }

        // Set the conversation and inInputNode flags
        playerFlags.setConversation(conversation);
        playerFlags.setInInputNode(true);
    }

    public String getText() {
        return text;
    }

    public String getVariable() {
        return variable;
    }
}
