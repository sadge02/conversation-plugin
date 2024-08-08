package minecraft.conversationPlugin.parsers.trigger.type.item;

/**
 * Keys for item trigger JSON object.
 */
public enum ItemTriggerKeys {

    ACTION("action"),
    NAME("name"),
    AMOUNT("amount"),
    CONSUME("consume");

    private final String key;

    ItemTriggerKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
