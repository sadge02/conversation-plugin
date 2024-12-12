package minecraft.conversationPlugin.parsers.settings;

/**
 * Keys for settings JSON object.
 */
public enum SettingsKeys {

    START_MESSAGE("start_message"),
    START_SOUND("start_sound"),
    END_MESSAGE("end_message"),
    END_SOUND("end_sound"),
    BLOCKING("blocking"),
    CITIZENS("citizens"),
    ADD_ON_JOIN("add_on_join"),
    START_ON_JOIN("start_on_join");

    private final String key;

    SettingsKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
