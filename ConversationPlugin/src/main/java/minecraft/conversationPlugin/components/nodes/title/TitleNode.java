package minecraft.conversationPlugin.components.nodes.title;

import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.text.Text;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Class for TitleNode objects.
 */
public class TitleNode extends Node {

    private final float fadeIn;
    private final float fadeOut;
    private final float duration;
    private final String title;
    private final String subtitle;

    public TitleNode(Node node, float duration, String title, String subtitle, float fadeIn, float fadeOut) {
        super(node.getNode(), node.getNodeType(), node.getTrigger(), node.getRequirements(), node.getCommands(), node.getNextNode());
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.duration = duration;
        this.title = title;
        this.subtitle = subtitle;
    }

    /**
     * Executes the TitleNode.
     *
     * @param player       Player
     * @param plugin       Plugin
     * @param conversation Conversation
     * @param playerFlags  PlayerFlags
     * @param playerInfo   PlayerInfo
     */
    public void executeTitleNode(Player player, Plugin plugin, Conversation conversation, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        // Send title message
        sendTitle(player, Text.processStringMessage(title, playerInfo, player.getName()), Text.processStringMessage(subtitle, playerInfo, player.getName()), duration, fadeIn, fadeOut);

        // Finish node
        finishNode(duration, plugin, conversation, player, playerFlags, playerInfo, getNextNode());
    }

    /**
     * Sends a title to a player.
     *
     * @param player   Player
     * @param title    Title message
     * @param subtitle Subtitle message
     * @param duration Duration
     * @param fadeIn   Fade in duration
     * @param fadeOut  Fade out duration
     */
    public void sendTitle(Player player, String title, String subtitle, float duration, float fadeIn, float fadeOut) {

        player.sendTitle(title, subtitle, (int) fadeIn * 20, (int) duration * 20, (int) fadeOut * 20);
    }

    public float getFadeIn() {
        return fadeIn;
    }

    public float getFadeOut() {
        return fadeOut;
    }

    public float getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
