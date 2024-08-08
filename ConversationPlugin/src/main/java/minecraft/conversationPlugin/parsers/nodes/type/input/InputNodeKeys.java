package minecraft.conversationPlugin.parsers.nodes.type.input;

/**
 * Keys for input node JSON object.
 */
public enum InputNodeKeys {

    TEXT("text"),
    VARIABLE("variable");

    private final String key;

    InputNodeKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
