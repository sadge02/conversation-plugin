package minecraft.conversationPlugin.parsers.nodes.type.title;

/**
 * Keys for title node JSON object.
 */
public enum TitleNodeKeys {

    TITLE("title"),
    SUBTITLE("subtitle"),
    FADE_IN("fade_in"),
    STAY("stay"),
    FADE_OUT("fade_out"),
    DURATION("duration");

    private final String key;

    TitleNodeKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
