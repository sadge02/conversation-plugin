package minecraft.conversationPlugin.listeners.trigger;

import io.papermc.paper.event.player.PlayerPickItemEvent;
import minecraft.conversationPlugin.components.controller.ConversationController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.components.trigger.item.ItemTrigger;
import minecraft.conversationPlugin.components.trigger.item.types.ItemActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listener for item triggers.
 * <p>
 * Trigger node if player interacts with, picks up or drops specified items.
 */
public class ItemTriggerListener implements Listener {

    private static final Logger logger = Logger.getLogger(ItemTriggerListener.class.getName());

    private final Plugin plugin;

    public ItemTriggerListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerDropItem(PlayerDropItemEvent event) {

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
                    if (trigger.getType() == TriggerType.ITEM) {
                        handleDropItemTrigger(event, player, (ItemTrigger) trigger, conversation);
                    }
                } catch (Exception exception) {
                    logger.warning("Error in item trigger listener: " + exception.getMessage());
                }
            }
        }
    }

    @EventHandler
    public void playerPickupItem(PlayerPickItemEvent event) {

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
                    // Check if the trigger is an item trigger
                    if (trigger.getType() == TriggerType.ITEM) {
                        handlePickupItemTrigger(player, (ItemTrigger) trigger, conversation);
                    }
                } catch (Exception exception) {
                    logger.warning("Error in item trigger listener: " + exception.getMessage());
                }
            }
        }
    }

    /**
     * Triggers the node when player picks up item and has the required amount of it.
     *
     * @param player       Player
     * @param trigger      Item trigger information
     * @param conversation Conversation
     */
    private void handlePickupItemTrigger(Player player, ItemTrigger trigger, Conversation conversation) {

        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            // Check if the trigger is pickup trigger
            if (trigger.getItemTriggerType() == ItemActionType.PICKUP) {

                // Check if the player has the required amount of items
                if (ItemTrigger.getItemAmount(player, trigger.getName()) >= trigger.getAmount()) {

                    ItemTrigger.consumeItems(player, trigger.getName(), trigger.getConsume());

                    ConversationController.executeConversation(conversation, player.getName(), plugin);
                }
            }
        }
    }

    /**
     * Trigger the node when player drops specified item.
     *
     * @param player       Player
     * @param trigger      Item trigger information
     * @param conversation Conversation
     */
    private void handleDropItemTrigger(PlayerDropItemEvent event, Player player, ItemTrigger trigger, Conversation conversation) {

        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            // Check if the trigger is a drop or pickup trigger
            if (trigger.getItemTriggerType() == ItemActionType.DROP) {

                if (ItemTrigger.checkItemName(event.getItemDrop().getItemStack(), trigger.getName())) {

                    ConversationController.executeConversation(conversation, player.getName(), plugin);
                }
            }
        }
    }
}
