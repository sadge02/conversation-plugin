package minecraft.conversationPlugin.listeners.trigger.multiple;

import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.controller.ConversationController;
import minecraft.conversationPlugin.components.item.ConversationStick;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.block.BlockTrigger;
import minecraft.conversationPlugin.components.trigger.block.types.BlockActionType;
import minecraft.conversationPlugin.components.trigger.item.ItemTrigger;
import minecraft.conversationPlugin.components.trigger.item.types.ItemActionType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listener for interact triggers.
 * <p>
 * Trigger conversation when player interacts with block, item or else.
 */
public class InteractListener implements Listener {

    private static final Logger logger = Logger.getLogger(InteractListener.class.getName());

    private final Plugin plugin;

    public InteractListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {

        if (event.getPlayer() instanceof Player player) {

            // Check if the player is holding a conversation stick
            if (ConversationStick.isConversationStick(player) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ConversationStick.getBlockInformation(player, event);
                return;
            }

            List<Conversation> conversations = Conversation.getPlayerConversations(player.getName());

            // Go through all conversations the player is in
            for (Conversation conversation : conversations) {

                Node node = conversation.nodes().get(conversation.getPlayer(player.getName()).getCurrentNodeID());

                if (node == null) {
                    continue;
                }

                Trigger trigger = node.getTrigger();

                try {
                    // Check if the trigger is an interact, block or item trigger
                    switch (trigger.getType()) {
                        case INTERACT -> handleInteract(player, conversation);
                        case BLOCK -> handleBlockInteract(event, player, (BlockTrigger) trigger, conversation);
                        case ITEM -> handleItemInteract(player, (ItemTrigger) trigger, conversation);
                    }
                } catch (Exception exception) {
                    logger.warning(String.format("Failed to handle trigger: %s", exception.getMessage()));
                }
            }
        }
    }

    /**
     * Triggers the node when player interacts with anything.
     *
     * @param player       Player
     * @param conversation Conversation
     */
    private void handleInteract(Player player, Conversation conversation) {

        // Check if the conversation can run
        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            ConversationController.executeConversation(conversation, player.getName(), plugin);
        }
    }

    /**
     * Triggers the node when player interacts with a specified block.
     *
     * @param event        Player interact event
     * @param player       Player
     * @param trigger      Block trigger information
     * @param conversation Conversation
     */
    private void handleBlockInteract(PlayerInteractEvent event, Player player, BlockTrigger trigger, Conversation conversation) {

        // Check if the conversation can run
        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            // Check if the player interacted with the block
            if (event.getClickedBlock() != null && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {

                if (trigger.getBlockTriggerType() == BlockActionType.INTERACT) {

                    Location triggerLocation = new Location(player.getWorld(), trigger.getBlockLocation().x(), trigger.getBlockLocation().y(), trigger.getBlockLocation().z());

                    Location blockLocation = event.getClickedBlock().getLocation();

                    // Check if the block is in the trigger location
                    if (blockLocation.equals(triggerLocation)) {

                        // Remove block
                        if (trigger.getRemove()) {
                            event.getClickedBlock().setType(Material.AIR);
                        }

                        ConversationController.executeConversation(conversation, player.getName(), plugin);
                    }
                }
            }
        }
    }

    /**
     * Triggers the node when player interacts with a specified item.
     *
     * @param player       Player
     * @param trigger      Item trigger information
     * @param conversation Conversation
     */
    private void handleItemInteract(Player player, ItemTrigger trigger, Conversation conversation) {

        // Check if the conversation can run
        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            // Check if the trigger is a use trigger
            if (trigger.getItemTriggerType() == ItemActionType.USE) {

                ItemStack item = player.getInventory().getItemInMainHand();

                if (ItemTrigger.checkItemName(item, trigger.getName())) {

                    if (trigger.getConsume() < item.getAmount()) {

                        item.setAmount(item.getAmount() - trigger.getConsume());

                        ConversationController.executeConversation(conversation, player.getName(), plugin);
                    }
                }
            }
        }
    }
}
