package minecraft.conversationPlugin.parsers.trigger.type.time;

/**
 * Keys for time trigger JSON object.
 */
public enum TimeTriggerKeys {

    DELAY("delay");

    private final String key;

    TimeTriggerKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
