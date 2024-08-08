package minecraft.conversationPlugin.components.trigger.location;

import minecraft.conversationPlugin.components.location.ConversationLocation;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;

/**
 * Triggers node if player is within a certain radius of a location.
 * <ul>
 *     <li>Location Location of trigger.
 *     <li>Radius   Radius of trigger.
 * </ul>
 * <p>
 * Uses PlayerMoveEvent to check, so it could potentially be hard on the server if used too much.
 */
public class LocationTrigger extends Trigger {

    private final ConversationLocation conversationLocation;
    private final float radius;

    public LocationTrigger(ConversationLocation conversationLocation, float radius) {
        super(TriggerType.LOCATION);
        this.conversationLocation = conversationLocation;
        this.radius = radius;
    }

    public ConversationLocation getLocation() {
        return conversationLocation;
    }

    public float getRadius() {
        return radius;
    }
}
