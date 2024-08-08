package minecraft.conversationPlugin.parsers.commands;

/**
 * Keys for command JSON object.
 */
public enum CommandKeys {

    COMMAND("command"),
    DELAY("delay"),
    EXECUTE("execute"),
    SENDER("sender");

    private final String key;

    CommandKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
