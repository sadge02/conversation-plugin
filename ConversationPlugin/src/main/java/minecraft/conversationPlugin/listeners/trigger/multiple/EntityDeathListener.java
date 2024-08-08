package minecraft.conversationPlugin.listeners.trigger.multiple;

import minecraft.conversationPlugin.components.controller.ConversationController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.elimination.EliminationTrigger;
import minecraft.conversationPlugin.components.trigger.elimination.types.EliminationType;
import minecraft.conversationPlugin.components.trigger.entity.EntityTrigger;
import minecraft.conversationPlugin.components.trigger.entity.types.EntityActionType;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listener for entity death triggers.
 * <p>
 * Trigger conversation when entity is eliminated.
 */
public class EntityDeathListener implements Listener {

    private static final Logger logger = Logger.getLogger(EntityDeathListener.class.getName());

    private final Plugin plugin;

    public EntityDeathListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        // Check if the entity was killed by a player
        if (event.getEntity().getKiller() instanceof Player player) {

            List<Conversation> conversations = Conversation.getPlayerConversations(player.getName());

            // Go through all conversations the player is in
            for (Conversation conversation : conversations) {

                Node node = conversation.nodes().get(conversation.getPlayer(player.getName()).getCurrentNodeID());

                if (node == null) {
                    continue;
                }

                Trigger trigger = node.getTrigger();

                try {
                    // Check if the trigger is an entity or kill trigger
                    switch (trigger.getType()) {
                        case ENTITY ->
                                handleEntityEliminateTrigger(event, player, (EntityTrigger) trigger, conversation);
                        case ELIMINATE ->
                                handleEliminateTrigger(event, player, (EliminationTrigger) trigger, conversation);
                    }
                } catch (Exception exception) {
                    logger.warning(String.format("Failed to handle entity death trigger: %s", exception.getMessage()));
                }
            }
        }
    }

    /**
     * Triggers the node when specified entity is killed.
     *
     * @param event        Entity death event
     * @param player       Player
     * @param trigger      Entity trigger information
     * @param conversation Conversation
     */
    private void handleEntityEliminateTrigger(EntityDeathEvent event, Player player, EntityTrigger trigger, Conversation conversation) {

        // Check if the conversation can be run
        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            // Check if the trigger is of type ELIMINATE
            if (trigger.getEntityTriggerType() == EntityActionType.ELIMINATE) {

                // Check if the dead entity ID matches with the trigger entity ID
                if (EntityTrigger.checkEntity(event.getEntity(), trigger.getEntityID())) {
                    ConversationController.executeConversation(conversation, player.getName(), plugin);
                }
            }
        }
    }

    /**
     * Triggers the node when specified number of entities are eliminated.
     *
     * @param event        Entity death event
     * @param player       Player
     * @param trigger      Kill trigger information
     * @param conversation Conversation
     */
    private void handleEliminateTrigger(EntityDeathEvent event, Player player, EliminationTrigger trigger, Conversation conversation) {

        // Check if the conversation can run
        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            Entity entity = event.getEntity();

            // Check if the trigger is of type HOSTILE and decrement the amount left
            if (trigger.getEliminateTriggerType() == EliminationType.HOSTILE && entity instanceof Monster) {
                trigger.decreaseQuantityLeft(player.getName());
            }

            // Check if the trigger is of type PASSIVE and decrement the amount left
            if (trigger.getEliminateTriggerType() == EliminationType.PASSIVE && entity instanceof Animals) {
                trigger.decreaseQuantityLeft(player.getName());
            }

            // Check if the trigger is of type ANY and decrement the amount left
            if (trigger.getEliminateTriggerType() == EliminationType.ANY) {
                trigger.decreaseQuantityLeft(player.getName());
            }

            // Check if the amount left is 0 and trigger the conversation
            if (trigger.getQuantityLeft(player.getName()) <= 0) {
                ConversationController.executeConversation(conversation, player.getName(), plugin);
            }
        }
    }
}
