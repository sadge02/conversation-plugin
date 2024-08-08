package minecraft.conversationPlugin.components.nodes.actionbar;

import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.text.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Class for ActionBarNode objects.
 */
public class ActionBarNode extends Node {

    private final String text;
    private final float duration;

    public ActionBarNode(Node node, String text, float duration) {
        super(node.getNode(), node.getNodeType(), node.getTrigger(), node.getRequirements(), node.getCommands(), node.getNextNode());
        this.text = text;
        this.duration = duration;
    }

    /**
     * This method executes an action bar node.
     *
     * @param player       Player
     * @param plugin       Plugin
     * @param conversation Conversation
     * @param playerFlags  PlayerFlags
     * @param playerInfo   PlayerInfo
     */
    public void executeActionBarNode(Player player, Plugin plugin, Conversation conversation, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        // Create and send an action bar message
        sendActionBar(player, Text.processStringMessage(text, playerInfo, player.getName()), duration, plugin);

        // Finish node
        finishNode(duration, plugin, conversation, player, playerFlags, playerInfo, getNextNode());
    }

    /**
     * This method sends an action bar message to a player.
     *
     * @param player   Player
     * @param message  Message to send
     * @param duration Duration of the action bar
     * @param plugin   Plugin
     */
    public void sendActionBar(Player player, String message, float duration, Plugin plugin) {

        // Refresh action bar message until duration is over
        new BukkitRunnable() {
            float timeLeft = duration;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    cancel();
                    return;
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
                timeLeft--;
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    public String getText() {
        return text;
    }

    public float getDuration() {
        return duration;
    }
}
