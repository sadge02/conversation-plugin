package minecraft.conversationPlugin.listeners.trigger;

import minecraft.conversationPlugin.ConversationPlugin;
import minecraft.conversationPlugin.components.controller.ConversationController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.components.trigger.location.LocationTrigger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listener for location triggers.
 * <p>
 * Trigger node if player enters specified location.
 */
public class LocationListener implements Listener {

    private static final Logger logger = Logger.getLogger(LocationListener.class.getName());

    private final Plugin plugin;

    public LocationListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void locationTrigger(PlayerMoveEvent event) {

        if (event.getPlayer() instanceof Player player) {

            if (ConversationPlugin.players.get(player.getName()).getInCutscene()) {
                event.setCancelled(true);
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
                    // Check if the trigger is a location trigger
                    if (trigger.getType() == TriggerType.LOCATION) {
                        handleLocationTrigger(player, (LocationTrigger) trigger, conversation);
                    }
                } catch (Exception exception) {
                    logger.warning("Error in location trigger listener: " + exception.getMessage());
                }
            }
        }
    }

    /**
     * Triggers the node when player enters radius of specified location.
     *
     * @param player          Player
     * @param locationTrigger Location trigger information
     * @param conversation    Conversation
     */
    private void handleLocationTrigger(Player player, LocationTrigger locationTrigger, Conversation conversation) {

        if (ConversationController.canExecuteConversation(conversation, player.getName())) {

            Location playerLocation = player.getLocation();

            Location triggerLocation = new Location(player.getWorld(), locationTrigger.getLocation().x(), locationTrigger.getLocation().y(), locationTrigger.getLocation().z());

            // Check if the player is within the trigger radius
            if (playerLocation.distance(triggerLocation) <= locationTrigger.getRadius()) {

                ConversationController.executeConversation(conversation, player.getName(), plugin);
            }
        }
    }
}
