package minecraft.conversationPlugin.listeners.conversation;

import minecraft.conversationPlugin.ConversationPlugin;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Logger;

/**
 * Listener for player connection events. Adds/removes players from the conversation management map.
 */
public class PlayerConnectionListener implements Listener {

    private static final Logger logger = Logger.getLogger(PlayerConnectionListener.class.getName());

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        try {
            // Add player to the conversation management map
            ConversationPlugin.players.putIfAbsent(event.getPlayer().getName(), new PlayerFlags());
            logger.info(String.format("[CONVERSATION PLUGIN] Successfully loaded player %s.", event.getPlayer().getName()));
        } catch (Exception exception) {
            logger.warning(String.format("[CONVERSATION PLUGIN] Failed to load player %s.", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        try {
            // Remove player from the conversation management map and clear entities
            ConversationPlugin.players.get(event.getPlayer().getName()).clearEntities();
            ConversationPlugin.players.remove(event.getPlayer().getName());
            logger.info(String.format("[CONVERSATION PLUGIN] Successfully removed player %s.", event.getPlayer().getName()));
        } catch (Exception exception) {
            logger.warning(String.format("[CONVERSATION PLUGIN] Failed to remove player %s.", event.getPlayer().getName()));
        }
    }
}
