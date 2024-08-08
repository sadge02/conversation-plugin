package minecraft.conversationPlugin.components.item;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Handles the Conversation tool.
 */
public class ConversationStick {

    private static final Logger logger = Logger.getLogger(ConversationStick.class.getName());

    /**
     * Checks if the player is holding a conversation tool.
     *
     * @param player Player to check
     * @return true if the player is holding a Conversation Stick, false otherwise.
     */
    public static boolean isConversationStick(Player player) {

        ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();

        try {
            if (itemMeta != null && itemMeta.hasDisplayName()) {

                // Get the plain text display name
                String plainTextDisplayName = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(itemMeta.displayName()));

                return player.getInventory().getItemInMainHand().getType() == Material.STICK && plainTextDisplayName.equals("conversation tool");
            }
        } catch (NullPointerException exception) {
            logger.warning("ItemMeta or displayName is null");
        }
        return false;
    }

    /**
     * Gets the block information when the player right-clicks a block with the conversation tool.
     *
     * @param player Player that right-clicked the block
     * @param event  Event information
     */
    public static void getBlockInformation(Player player, PlayerInteractEvent event) {

        Block clickedBlock = event.getClickedBlock();

        // Check if the player right-clicked a block
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock != null) {

            int x = clickedBlock.getLocation().getBlockX();
            int y = clickedBlock.getLocation().getBlockY();
            int z = clickedBlock.getLocation().getBlockZ();
            String coords = String.format("X: %d Y: %d Z: %d", x, y, z);

            sendFormattedMessage(player, ChatColor.BOLD + "BLOCK " + coords, coords, "Click to copy coordinates");
        }
    }

    /**
     * Gets the entity information when the player right-clicks an entity with the conversation tool.
     *
     * @param player Player that right-clicked the entity
     * @param event  Event information
     */
    public static void getEntityInformation(Player player, PlayerInteractEntityEvent event) {

        String entity = String.valueOf(event.getRightClicked().getUniqueId());

        sendFormattedMessage(player, ChatColor.BOLD + "Entity ID: " + entity, entity, "Click to copy entity ID");
    }

    /**
     * Method to send formatted messages with click and hover events.
     *
     * @param player        Player to send the message to
     * @param messageText   Main text of the message
     * @param clipboardText Text to copy to the clipboard on click
     * @param hoverText     Text shown when hovering over the message
     */
    private static void sendFormattedMessage(Player player, String messageText, String clipboardText, String hoverText) {

        TextComponent message = new TextComponent(messageText);

        // Add copy to clipboard and hover events
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clipboardText));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(hoverText)}));

        player.sendMessage(message);
    }
}
