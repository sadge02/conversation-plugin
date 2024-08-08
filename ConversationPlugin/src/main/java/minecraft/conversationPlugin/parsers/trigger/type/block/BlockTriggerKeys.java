package minecraft.conversationPlugin.parsers.trigger.type.block;

/**
 * Keys for block trigger JSON object.
 */
public enum BlockTriggerKeys {

    ACTION("action"),
    LOCATION("location"),
    REMOVE("remove");

    private final String key;

    BlockTriggerKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
