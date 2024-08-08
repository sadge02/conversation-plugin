package minecraft.conversationPlugin.listeners.trigger;

import minecraft.conversationPlugin.components.commads.types.CommandExecutionType;
import minecraft.conversationPlugin.components.controller.ConversationController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.item.ConversationStick;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.components.trigger.entity.EntityTrigger;
import minecraft.conversationPlugin.components.trigger.entity.types.EntityActionType;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listener for entity triggers.
 * <p>
 * Trigger node if player interacts with or damages specified entity.
 */
public class EntityTriggerListener implements Listener {

    private static final Logger logger = Logger.getLogger(EntityTriggerListener.class.getName());

    private final Plugin plugin;

    public EntityTriggerListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void entityTrigger(EntityDamageByEntityEvent event) {

        // Check if the entity was damaged by a player
        if (event.getDamager() instanceof Player player) {

            List<Conversation> conversations = Conversation.getPlayerConversations(player.getName());

            // Go through all conversations the player is in
            for (Conversation conversation : conversations) {

                Node node = conversation.nodes().get(conversation.getPlayer(player.getName()).getCurrentNodeID());

                if (node == null) {
                    continue;
                }

                Trigger trigger = node.getTrigger();

                try {
                    // Check if the trigger is an entity trigger
                    if (trigger.getType() == TriggerType.ENTITY) {

                        handleEntityDamageTrigger(event, player, (EntityTrigger) trigger, conversation);
                    }
                } catch (Exception exception) {
                    logger.warning(String.format("[CONVERSATION PLUGIN] Failed to handle entity damage trigger for player %s.", player.getName()));
                }
            }
        }
    }

    @EventHandler
    public void entityTrigger(PlayerInteractEntityEvent event) {

        if (event.getPlayer() instanceof Player player) {

            if (event.getRightClicked() instanceof Interaction entity) {

                PlayerFlags playerFlags = PlayerFlags.getPlayerFlags(player.getName());

                if (playerFlags == null) {
                    return;
                }
                if (playerFlags.getInChoiceNode()) {

                    if (entity.hasMetadata("choice")) {

                        String command = entity.getMetadata("choice").getFirst().asString();

                        Conversation conversation = playerFlags.getConversation();

                        if (conversation == null) {
                            return;
                        }

                        player.getWorld().spawnParticle(Particle.POOF, entity.getLocation(), 10, 0.3, 0.3, 0.3, 0.1);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                playerFlags.clearEntities();
                                playerFlags.setInChoiceNode(false);
                                playerFlags.setConversation(null);
                                playerFlags.setInNode(false);

                                Node node = conversation.nodes().get(conversation.getPlayer(player.getName()).getCurrentNodeID());

                                node.runCommands(CommandExecutionType.END, plugin, player.getName());

                                // Move player to the next node
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("player", player.getName()));

                                PlayerInfo playerInfo = conversation.getPlayer(player.getName());

                                if (playerInfo == null) {
                                    return;
                                }

                                node = conversation.nodes().get(conversation.getPlayer(player.getName()).getCurrentNodeID());

                                if (node != null) {
                                    node.finishNode(0, plugin, conversation, player, playerFlags, playerInfo, node.getNode());
                                } else {
                                    Node.finishedConversation(conversation, player);

                                    PlayerFlags playerFlags = PlayerFlags.getPlayerFlags(player.getName());

                                    if (playerFlags != null) {
                                        playerFlags.setInNode(false);
                                        playerFlags.setInChoiceNode(false);
                                        playerFlags.setConversation(null);
                                        playerFlags.clearEntities();
                                    }
                                }
                            }
                        }.runTaskLater(plugin, 20L);
                    }
                    return;
                }
            }

            // Check if the player is holding a conversation stick
            if (ConversationStick.isConversationStick(player)) {
                ConversationStick.getEntityInformation(player, event);
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
                    // Check if the trigger is an entity trigger
                    if (trigger.getType() == TriggerType.ENTITY) {

                        handleEntityInteractTrigger(event, player, (EntityTrigger) trigger, conversation);
                    }
                } catch (Exception exception) {
                    logger.warning(String.format("[CONVERSATION PLUGIN] Failed to handle entity interact trigger for player %s.", player.getName()));
                }
            }
        }
    }

    /**
     * Triggers the node when player interacts with specified entity.
     *
     * @param event        Player interact entity event
     * @param player       Player
     * @param trigger      Entity trigger information
     * @param conversation Conversation
     */
    private void handleEntityInteractTrigger(PlayerInteractEntityEvent event, Player player, EntityTrigger trigger, Conversation conversation) {

        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            // Check if the trigger is of type INTERACT
            if (trigger.getEntityTriggerType() == EntityActionType.INTERACT) {

                // Check if the entity interacted with matches the trigger entity ID
                if (EntityTrigger.checkEntity(event.getRightClicked(), trigger.getEntityID())) {

                    ConversationController.executeConversation(conversation, player.getName(), plugin);
                }
            }
        }
    }

    /**
     * Triggers the node when player damages specified entity.
     *
     * @param event        Entity damage event
     * @param player       Player
     * @param trigger      Entity trigger information
     * @param conversation Conversation
     */
    private void handleEntityDamageTrigger(EntityDamageByEntityEvent event, Player player, EntityTrigger trigger, Conversation conversation) {

        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            // Check if the trigger is of type DAMAGE
            if (trigger.getEntityTriggerType() == EntityActionType.DAMAGE) {

                // Check if the damaged entity ID matches with the trigger entity ID
                if (EntityTrigger.checkEntity(event.getEntity(), trigger.getEntityID())) {

                    ConversationController.executeConversation(conversation, player.getName(), plugin);
                }
            }
        }
    }
}
