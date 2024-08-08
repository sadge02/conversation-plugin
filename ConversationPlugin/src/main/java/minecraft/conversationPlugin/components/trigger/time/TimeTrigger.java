package minecraft.conversationPlugin.components.trigger.time;

import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;

/**
 * Triggers node based on a time delay.
 * <ul>
 *     <li>Delay Time delay in seconds.
 * </ul>
 */
public class TimeTrigger extends Trigger {

    private final float delay;

    public TimeTrigger(float delay) {
        super(TriggerType.TIME);
        this.delay = delay;
    }

    public float getDelay() {
        return delay;
    }
}
