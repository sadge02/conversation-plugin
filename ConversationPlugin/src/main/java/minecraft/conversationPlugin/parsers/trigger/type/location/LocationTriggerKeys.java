package minecraft.conversationPlugin.parsers.trigger.type.location;

/**
 * Keys for location trigger JSON object.
 */
public enum LocationTriggerKeys {

    LOCATION("location"),
    RADIUS("radius");

    private final String key;

    LocationTriggerKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
