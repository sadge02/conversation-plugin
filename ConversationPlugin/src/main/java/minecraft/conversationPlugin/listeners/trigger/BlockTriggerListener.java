package minecraft.conversationPlugin.listeners.trigger;

import minecraft.conversationPlugin.components.controller.ConversationController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.components.trigger.block.BlockTrigger;
import minecraft.conversationPlugin.components.trigger.block.types.BlockActionType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listener for block triggers.
 * <p>
 * Trigger node if player places or breaks block in specified location.
 */
public class BlockTriggerListener implements Listener {

    private static final Logger logger = Logger.getLogger(BlockTriggerListener.class.getName());

    private final Plugin plugin;

    public BlockTriggerListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerBlockPlace(BlockPlaceEvent event) {

        if (event.getPlayer() instanceof Player player) {

            List<Conversation> conversations = Conversation.getPlayerConversations(player.getName());

            // Go through all conversations the player is in
            for (Conversation conversation : conversations) {

                Node node = conversation.nodes().get(conversation.getPlayer(player.getName()).getCurrentNodeID());

                if (node == null) {
                    continue;
                }

                Trigger trigger = node.getTrigger();

                try {
                    // Check if the trigger is a block trigger
                    if (trigger.getType() == TriggerType.BLOCK) {
                        handleBlockPlaceTrigger(event, player, (BlockTrigger) trigger, conversation);
                    }
                } catch (Exception exception) {
                    logger.warning(String.format("[CONVERSATION PLUGIN] Failed to handle block place trigger for player %s.", player.getName()));
                }
            }
        }
    }

    @EventHandler
    public void playerBlockBreak(BlockBreakEvent event) {

        if (event.getPlayer() instanceof Player player) {

            List<Conversation> conversations = Conversation.getPlayerConversations(player.getName());

            // Go through all conversations the player is in
            for (Conversation conversation : conversations) {

                Node node = conversation.nodes().get(conversation.getPlayer(player.getName()).getCurrentNodeID());

                if (node == null) {
                    continue;
                }

                Trigger trigger = node.getTrigger();

                try {
                    // Check if the trigger is a block trigger
                    if (trigger.getType() == TriggerType.BLOCK) {
                        handleBlockBreakTrigger(event, player, (BlockTrigger) trigger, conversation);
                    }
                } catch (Exception exception) {
                    logger.warning(String.format("[CONVERSATION PLUGIN] Failed to handle block break trigger for player %s.", player.getName()));
                }
            }
        }
    }

    /**
     * Triggers the node when player breaks specified block.
     *
     * @param event        Block break event
     * @param player       Player
     * @param trigger      Block trigger information
     * @param conversation Conversation
     */
    private void handleBlockBreakTrigger(BlockBreakEvent event, Player player, BlockTrigger trigger, Conversation conversation) {

        // Check if the conversation can run
        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            // Check if the trigger is a block break trigger
            if (trigger.getBlockTriggerType() == BlockActionType.BREAK) {

                Location triggerLocation = new Location(player.getWorld(), trigger.getBlockLocation().x(), trigger.getBlockLocation().y(), trigger.getBlockLocation().z());

                Location blockLocation = event.getBlock().getLocation();

                // Check if the block is in the trigger location
                if (blockLocation.equals(triggerLocation)) {

                    // Remove item drop
                    if (trigger.getRemove()) {
                        event.setDropItems(false);
                    }

                    ConversationController.executeConversation(conversation, player.getName(), plugin);
                }
            }
        }
    }

    /**
     * Triggers the node when player places block and removes block if specified.
     *
     * @param event        Block place event
     * @param player       Player
     * @param trigger      Block trigger information
     * @param conversation Conversation
     */
    private void handleBlockPlaceTrigger(BlockPlaceEvent event, Player player, BlockTrigger trigger, Conversation conversation) {

        // Check if the conversation can run
        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            // Check if the trigger is a block place trigger
            if (trigger.getBlockTriggerType() == BlockActionType.PLACE) {

                Location triggerLocation = new Location(player.getWorld(), trigger.getBlockLocation().x(), trigger.getBlockLocation().y(), trigger.getBlockLocation().z());

                Location blockLocation = event.getBlock().getLocation();

                // Check if the block is in the trigger location
                if (blockLocation.equals(triggerLocation)) {

                    // Stop block from being placed
                    if (trigger.getRemove()) {
                        event.setCancelled(true);
                    }

                    ConversationController.executeConversation(conversation, player.getName(), plugin);
                }
            }
        }
    }
}
