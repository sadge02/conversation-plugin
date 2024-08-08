package minecraft.conversationPlugin.parsers.nodes;

/**
 * Keys for node JSON object.
 */
public enum NodeKeys {

    NODE_TYPE("node_type"),
    TRIGGER("trigger"),
    REQUIREMENTS("requirements"),
    COMMANDS("commands"),
    NEXT_NODE("next_node"),
    NODE_SETTINGS("node_settings");


    private final String key;

    NodeKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
