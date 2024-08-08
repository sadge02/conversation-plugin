package minecraft.conversationPlugin.parsers.display;

/**
 * Keys for display settings JSON object.
 */
public enum DisplaySettingsKeys {

    FACE("face"),
    SHADOW("shadow"),
    ALIGNMENT("alignment"),
    SEE_THROUGH("see_through"),
    VISIBLE("visible"),
    GLOWING("glowing"),
    SCALE("scale"),
    BACKGROUND("background"),
    LINE_WIDTH("line_width"),
    BIND_TO_ENTITY("bind_to_entity"),
    ELEVATION("elevation");


    private final String key;

    DisplaySettingsKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
