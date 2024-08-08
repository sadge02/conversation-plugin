package minecraft.conversationPlugin.parsers.nodes.type.bossbar;

/**
 * Keys for boss bar node JSON object.
 */
public enum BossBarNodeKeys {

    TEXT("text"),
    PROGRESS("progress"),
    DURATION("duration"),
    FADE_IN("fade_in"),
    FADE_OUT("fade_out");

    private final String key;

    BossBarNodeKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
