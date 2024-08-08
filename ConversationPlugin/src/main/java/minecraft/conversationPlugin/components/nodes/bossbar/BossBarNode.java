package minecraft.conversationPlugin.components.nodes.bossbar;

import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Class for BossBar node.
 */
public class BossBarNode extends Node {

    private final String text;
    private final float progress;
    private final float duration;

    public BossBarNode(Node node, String text, float progress, float duration) {
        super(node.getNode(), node.getNodeType(), node.getTrigger(), node.getRequirements(), node.getCommands(), node.getNextNode());
        this.text = text;
        this.progress = progress;
        this.duration = duration;
    }

    /**
     * Executes a BossBar node.
     *
     * @param player       Player
     * @param plugin       Plugin
     * @param conversation Conversation
     * @param playerFlags  PlayerFlags
     * @param playerInfo   PlayerInfo
     */
    public void executeBossBarNode(Player player, Plugin plugin, Conversation conversation, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        // Create and send a boss bar message
        sendBossBar(player, Text.processStringMessage(text, playerInfo, player.getName()), duration, plugin, progress);

        // Finish node
        finishNode(duration, plugin, conversation, player, playerFlags, playerInfo, getNextNode());
    }

    /**
     * Creates a BossBar and sends it to the player.
     *
     * @param player   Player
     * @param message  Message to display
     * @param duration Duration of the BossBar
     * @param plugin   Plugin
     * @param progress Progress of the BossBar
     */
    private void sendBossBar(Player player, String message, float duration, Plugin plugin, float progress) {

        // Create the BossBar
        BossBar bossBar = Bukkit.createBossBar(message, BarColor.WHITE, BarStyle.SOLID);
        bossBar.setProgress(progress);
        bossBar.addPlayer(player);

        // Remove the BossBar after the duration
        new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.removeAll();
                cancel();
            }
        }.runTaskLater(plugin, (int) (duration * 20L));
    }

    public float getProgress() {
        return progress;
    }

    public float getDuration() {
        return duration;
    }

    public String getText() {
        return text;
    }
}
