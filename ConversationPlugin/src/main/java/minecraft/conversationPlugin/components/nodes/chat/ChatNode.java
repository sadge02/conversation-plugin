package minecraft.conversationPlugin.components.nodes.chat;

import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.text.Text;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Class for Chat node.
 */
public class ChatNode extends Node {

    private final String text;

    public ChatNode(Node node, String text) {
        super(node.getNode(), node.getNodeType(), node.getTrigger(), node.getRequirements(), node.getCommands(), node.getNextNode());
        this.text = text;
    }

    /**
     * Executes the ChatNode.
     *
     * @param player       Player
     * @param plugin       Plugin
     * @param conversation Conversation
     * @param playerFlags  PlayerFlags
     * @param playerInfo   PlayerInfo
     */
    public void executeChatNode(Player player, Plugin plugin, Conversation conversation, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        // Send chat message
        player.sendMessage(Text.processTextComponentMessage(text, playerInfo, player.getName()));

        // Finish node
        finishNode(0, plugin, conversation, player, playerFlags, playerInfo, getNextNode());
    }

    public String getText() {
        return text;
    }
}
