package minecraft.conversationPlugin.parsers.camera;

/**
 * Keys for camera JSON object.
 */
public enum CameraKeys {

    CUTSCENE("cutscene"),
    TARGET("target"),
    ANGLE("angle"),
    DISTANCE_MULTIPLIER("distance_multiplier"),
    HEIGHT_MULTIPLIER("height_multiplier"),
    ZOOM("zoom");

    private final String key;

    CameraKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
