package minecraft.conversationPlugin.components.trigger.command;

import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;

/**
 * Triggers node only with a command.
 */
public class CommandTrigger extends Trigger {

    public CommandTrigger() {
        super(TriggerType.COMMAND);
    }
}
