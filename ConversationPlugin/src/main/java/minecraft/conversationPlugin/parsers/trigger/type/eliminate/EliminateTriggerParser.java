package minecraft.conversationPlugin.parsers.trigger.type.eliminate;

import minecraft.conversationPlugin.components.trigger.elimination.EliminationTrigger;
import minecraft.conversationPlugin.components.trigger.elimination.types.EliminationType;
import org.json.JSONObject;

/**
 * Serialize and deserialize EliminateTrigger objects.
 */
public class EliminateTriggerParser {

    /**
     * Deserialize EliminateTrigger object from JSON object.
     *
     * @param eliminateTriggerObject JSON object to deserialize
     * @return deserialized EliminateTrigger object
     */
    public static EliminationTrigger parseKillTrigger(JSONObject eliminateTriggerObject) {

        EliminationType action = EliminationType.valueOf(eliminateTriggerObject.getString(EliminateTriggerKeys.ELIMINATE.getKey()).toUpperCase());
        int amount = eliminateTriggerObject.getInt(EliminateTriggerKeys.QUANTITY.getKey());

        return new EliminationTrigger(action, amount);
    }

    /**
     * Serialize EliminateTrigger object to JSON object.
     *
     * @param nodeSettings     JSON object to serialize
     * @param eliminateTrigger EliminateTrigger object to serialize
     */
    public static void saveEliminateTrigger(JSONObject nodeSettings, EliminationTrigger eliminateTrigger) {

        nodeSettings.put(EliminateTriggerKeys.ELIMINATE.getKey(), eliminateTrigger.getEliminateTriggerType().toString());
        nodeSettings.put(EliminateTriggerKeys.QUANTITY.getKey(), eliminateTrigger.getQuantity());
    }
}
