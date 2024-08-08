package minecraft.conversationPlugin.components.trigger.interact;

import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;

/**
 * Trigger node based on any player interaction.
 * <p>
 * Uses PlayerInteractEvent.
 */
public class InteractTrigger extends Trigger {

    public InteractTrigger() {
        super(TriggerType.INTERACT);
    }
}
