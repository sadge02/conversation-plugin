package minecraft.conversationPlugin.parsers.nodes.type.choice;

/**
 * Keys for choice node JSON object.
 */
public enum ChoiceNodeKeys {

    TEXT("text"),
    TARGET("target"),
    LOCATION("location"),
    ENTITY("entity"),
    DISPLAY_SETTINGS("display_settings"),
    CHOICES("choices");

    private final String key;

    ChoiceNodeKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
