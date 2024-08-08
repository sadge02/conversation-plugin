package minecraft.conversationPlugin.parsers.nodes.type.actionbar;

/**
 * Keys for action bar node JSON object.
 */
public enum ActionBarNodeKeys {

    TEXT("text"),
    DURATION("duration");

    private final String key;

    ActionBarNodeKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
