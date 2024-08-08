package minecraft.conversationPlugin.parsers.nodes.type.chat;

/**
 * Keys for chat node JSON object.
 */
public enum ChatNodeKeys {

    TEXT("text");

    private final String key;

    ChatNodeKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
