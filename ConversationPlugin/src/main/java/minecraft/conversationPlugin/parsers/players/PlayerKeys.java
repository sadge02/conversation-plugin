package minecraft.conversationPlugin.parsers.players;

/**
 * Keys for player JSON object.
 */
public enum PlayerKeys {

    CURRENT_NODE("current_node"),
    REQUIREMENTS("requirements"),
    VARIABLES("variables");

    private final String key;

    PlayerKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
