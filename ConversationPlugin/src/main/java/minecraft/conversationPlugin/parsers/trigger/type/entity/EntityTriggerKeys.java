package minecraft.conversationPlugin.parsers.trigger.type.entity;

/**
 * Keys for entity trigger JSON object.
 */
public enum EntityTriggerKeys {

    ACTION("action"),
    ENTITY("entity");

    private final String key;

    EntityTriggerKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
