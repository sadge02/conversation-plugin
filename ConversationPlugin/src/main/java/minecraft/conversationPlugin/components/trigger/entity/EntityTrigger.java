package minecraft.conversationPlugin.components.trigger.entity;

import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.components.trigger.entity.types.EntityActionType;
import org.bukkit.entity.Entity;

/**
 * Trigger node based on an entity interaction.
 * <ul>
 *     <li>EntityTriggerType Type of entity trigger.
 *     <li>EntityID          ID of entity.
 * </ul>
 * <p>
 * Uses PlayerInteractEntityEvent.
 */
public class EntityTrigger extends Trigger {

    private final EntityActionType type;
    private final String entityID;

    public EntityTrigger(EntityActionType type, String entityID) {
        super(TriggerType.ENTITY);
        this.type = type;
        this.entityID = entityID;
    }

    public static boolean checkEntity(Entity entity, String name) {
        return name.equals(entity.getUniqueId().toString());
    }

    public EntityActionType getEntityTriggerType() {
        return type;
    }

    public String getEntityID() {
        return entityID;
    }
}
