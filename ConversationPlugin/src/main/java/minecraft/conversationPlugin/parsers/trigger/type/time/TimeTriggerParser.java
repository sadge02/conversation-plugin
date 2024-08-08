package minecraft.conversationPlugin.parsers.trigger.type.time;

import minecraft.conversationPlugin.components.trigger.time.TimeTrigger;
import minecraft.conversationPlugin.parsers.conversation.ConversationKeys;
import org.json.JSONObject;

/**
 * Serialize and deserialize TimeTrigger objects.
 */
public class TimeTriggerParser {

    /**
     * Deserialize TimeTrigger object from JSON object.
     *
     * @param timeTriggerObject JSON object to deserialize
     * @return deserialized TimeTrigger object
     */
    public static TimeTrigger parseTimeTrigger(JSONObject timeTriggerObject) {

        float delay = ConversationKeys.parseFloat(timeTriggerObject, TimeTriggerKeys.DELAY.getKey());

        return new TimeTrigger(delay);
    }

    /**
     * Serialize TimeTrigger object to JSON object.
     *
     * @param timeTrigger TimeTrigger object to serialize
     */
    public static void saveTimeTrigger(JSONObject nodeSettings, TimeTrigger timeTrigger) {

        nodeSettings.put(TimeTriggerKeys.DELAY.getKey(), timeTrigger.getDelay());
    }
}
