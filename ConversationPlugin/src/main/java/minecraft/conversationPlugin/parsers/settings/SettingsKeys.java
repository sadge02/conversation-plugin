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
    CITIZENS("citizens");

    private final String key;

    SettingsKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
