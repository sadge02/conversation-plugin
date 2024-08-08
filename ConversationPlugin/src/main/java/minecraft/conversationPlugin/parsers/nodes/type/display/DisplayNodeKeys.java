package minecraft.conversationPlugin.parsers.nodes.type.display;

/**
 * Keys for display node JSON object.
 */
public enum DisplayNodeKeys {

    TEXT("text"),
    DURATION("duration"),
    DISPLAY_SETTINGS("display_settings"),
    CAMERA_SETTINGS("camera_settings"),
    TARGET("target"),
    LOCATION("location"),
    ENTITY("entity");

    private final String key;

    DisplayNodeKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
