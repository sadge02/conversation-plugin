package minecraft.conversationPlugin.parsers.trigger.type.eliminate;

/**
 * Keys for elimination trigger JSON object.
 */
public enum EliminateTriggerKeys {

    QUANTITY("quantity"),
    ELIMINATE("eliminate");

    private final String key;

    EliminateTriggerKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
