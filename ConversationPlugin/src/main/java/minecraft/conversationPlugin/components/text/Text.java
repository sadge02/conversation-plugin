package minecraft.conversationPlugin.components.text;

import minecraft.conversationPlugin.components.player.PlayerInfo;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

/**
 * Translate text with placeholders and legacy format to text components and strings.
 */
public class Text {

    /**
     * Processes a text component message and replaces placeholders with player's variables.
     *
     * @param text   Text to process
     * @param player Player to process the text for
     * @return the processed text component
     */
    public static TextComponent processTextComponentMessage(String text, PlayerInfo player, String playerName) {

        text = text.replace("%player%", playerName);

        for (String key : player.getVariables().keySet()) {
            text = text.replace(key, player.getVariable(key));
        }

        return LegacyComponentSerializer.legacy('&').deserialize(text);
    }

    /**
     * Processes a string message and replaces placeholders with player's variables.
     *
     * @param text   Text to process
     * @param player Player to process the text for
     * @return the processed string
     */
    public static String processStringMessage(String text, PlayerInfo player, String playerName) {

        text = text.replace("%player%", playerName);

        for (String key : player.getVariables().keySet()) {
            text = text.replace(key, player.getVariable(key));
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
