package minecraft.conversationPlugin.parsers.conversation;

import org.json.JSONObject;

/**
 * Keys for conversation JSON object.
 */
public enum ConversationKeys {

    START_NODE("start_node"),
    SETTINGS("settings"),
    PLAYERS("players"),
    NODES("nodes");

    private final String key;

    ConversationKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    /**
     * Parses a float from a JSON object.
     *
     * @param object JSON object
     * @param key    Key to parse
     * @return parsed float
     */
    public static float parseFloat(JSONObject object, String key) {

        try {
            return object.getFloat(key);
        } catch (Exception exception) {
            return (float) object.getInt(key);
        }
    }
}
