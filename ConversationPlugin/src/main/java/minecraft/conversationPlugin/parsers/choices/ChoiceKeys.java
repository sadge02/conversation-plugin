package minecraft.conversationPlugin.parsers.choices;

/**
 * Keys for choice objects.
 */
public enum ChoiceKeys {

    TEXT("text"),
    NODE("node"),
    REQUIREMENTS("requirements"),
    LOCATION("location"),
    TARGET("target"),
    COMMANDS("commands"),
    DISPLAY_SETTINGS("display_settings");

    private final String key;

    ChoiceKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
