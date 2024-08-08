package minecraft.conversationPlugin.components.trigger;

import minecraft.conversationPlugin.components.trigger.types.TriggerType;

/**
 * This class contains the information about node trigger.
 * <p>
 * Triggers are used to determine when a node should be executed.
 */
public class Trigger {

    private final TriggerType type;

    public Trigger(TriggerType type) {
        this.type = type;
    }

    public TriggerType getType() {
        return type;
    }
}
