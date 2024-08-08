package minecraft.conversationPlugin.components.nodes.types;

/**
 * Enum representing different ways to send or display messages to a player.
 *
 * <ul>
 *     <li>SUBTITLE: Displays only a subtitle message.</li>
 *     <li>TITLE: Displays only a title message.</li>
 *     <li>TITLE_SUBTITLE: Displays a combined title and subtitle message.</li>
 *     <li>CHAT: Sends a message to the player’s chat.</li>
 *     <li>DISPLAY: Shows a text display in the world at a specific location.</li>
 *     <li>ACTION_BAR: Sends a message to the player's action bar.</li>
 *     <li>BOSS_BAR: Shows a boss bar with a progress indicator.</li>
 *     <li>CHOICE: Presents choices to the player in the world, allowing selection.</li>
 *     <li>INPUT: Sends a message to chat and waits for the player’s response.</li>
 * </ul>
 */
public enum NodeType {

    SUBTITLE,
    TITLE,
    TITLE_SUBTITLE,
    CHAT,
    DISPLAY,
    ACTION_BAR,
    BOSS_BAR,
    CHOICE,
    INPUT,
}
