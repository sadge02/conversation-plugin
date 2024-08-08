package minecraft.conversationPlugin.listeners.node;

import io.papermc.paper.event.player.AsyncChatEvent;
import minecraft.conversationPlugin.ConversationPlugin;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.input.InputNode;
import minecraft.conversationPlugin.components.nodes.types.NodeType;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.listeners.trigger.EntityTriggerListener;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

/**
 * Listener for player input.
 * <p>
 * Handles player input.
 */
public class InputListener implements Listener {

    private static final Logger logger = Logger.getLogger(EntityTriggerListener.class.getName());
    private static final int INPUT_TIMEOUT = 1; // Cooldown after input in seconds

    private final Plugin plugin;

    public InputListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerInput(AsyncChatEvent event) {

        if (event.getPlayer() instanceof Player player) {

            // Get player flags
            PlayerFlags playerFlags = ConversationPlugin.players.get(player.getName());

            if (playerFlags.getInInputNode()) {
                handlePlayerInput(event, player, playerFlags, playerFlags.getConversation());
            }
        }
    }

    /**
     * Handles the player input.
     *
     * @param event        Chat event
     * @param player       Player
     * @param flags        Player flags
     * @param conversation Conversation
     */
    private void handlePlayerInput(AsyncChatEvent event, Player player, PlayerFlags flags, Conversation conversation) {

        if (conversation == null) {
            return;
        }

        // Get player info
        PlayerInfo playerInfo = conversation.getPlayer(player.getName());

        if (playerInfo == null) {
            return;
        }
        // Player input
        String input = ((TextComponent) event.message()).content();

        // Get current node
        Node node = conversation.nodes().get(playerInfo.getCurrentNodeID());

        if (node == null || node.getNodeType() != NodeType.INPUT) {
            return;
        }
        // Cancel the message
        event.setCancelled(true);

        InputNode inputNode = (InputNode) node;

        playerInfo.addVariable(inputNode.getVariable(), input);

        logger.info(String.format("[CONVERSATION PLUGIN] Player %s input: %s", player.getName(), input));

        new BukkitRunnable() {
            @Override
            public void run() {

                flags.setInInputNode(false);

                Node node = conversation.nodes().get(playerInfo.getCurrentNodeID());

                if (!Node.finishedConversation(conversation, player)) {
                    node.finishNode(0, plugin, conversation, player, flags, playerInfo, node.getNextNode());
                }

                flags.setConversation(null);

            }
        }.runTaskLater(plugin, 20 * INPUT_TIMEOUT);
    }
}
