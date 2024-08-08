package minecraft.conversationPlugin.parsers.trigger.type.entity;

import minecraft.conversationPlugin.components.trigger.entity.EntityTrigger;
import minecraft.conversationPlugin.components.trigger.entity.types.EntityActionType;
import org.json.JSONObject;

/**
 * Serialize and deserialize EntityTrigger object.
 */
public class EntityTriggerParser {

    /**
     * Serialize JSONObject to EntityTrigger object.
     *
     * @param entityTriggerObject JSONObject to serialize
     * @return serialized EntityTrigger object
     */
    public static EntityTrigger parseEntityTrigger(JSONObject entityTriggerObject) {

        EntityActionType action = EntityActionType.valueOf(entityTriggerObject.getString(EntityTriggerKeys.ACTION.getKey()));
        String entity = entityTriggerObject.getString(EntityTriggerKeys.ENTITY.getKey());

        return new EntityTrigger(action, entity);
    }

    /**
     * Deserialize EntityTrigger object to JSONObject.
     *
     * @param nodeSettings  JSONObject to deserialize
     * @param entityTrigger EntityTrigger object to deserialize
     */
    public static void saveEntityTrigger(JSONObject nodeSettings, EntityTrigger entityTrigger) {

        nodeSettings.put(EntityTriggerKeys.ACTION.getKey(), entityTrigger.getEntityTriggerType().toString());
        nodeSettings.put(EntityTriggerKeys.ENTITY.getKey(), entityTrigger.getEntityID());
    }
}
