package minecraft.conversationPlugin.parsers.trigger;

/**
 * Keys for trigger JSON object.
 */
public enum TriggerKeys {

    TRIGGER_TYPE("trigger_type");

    private final String key;

    TriggerKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
