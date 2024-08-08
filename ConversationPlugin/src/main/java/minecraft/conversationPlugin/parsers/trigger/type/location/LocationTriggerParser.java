package minecraft.conversationPlugin.parsers.trigger.type.location;

import minecraft.conversationPlugin.components.location.ConversationLocation;
import minecraft.conversationPlugin.components.trigger.location.LocationTrigger;
import minecraft.conversationPlugin.parsers.conversation.ConversationKeys;
import minecraft.conversationPlugin.parsers.location.LocationParser;
import org.json.JSONObject;

/**
 * Serialize and deserialize a LocationTrigger object
 */
public class LocationTriggerParser {

    /**
     * Deserialize a LocationTrigger object
     *
     * @param locationTriggerObject JSONObject to deserialize
     * @return serialized LocationTrigger object
     */
    public static LocationTrigger parseLocationTrigger(JSONObject locationTriggerObject) {

        ConversationLocation conversationLocation = LocationParser.parseLocation(locationTriggerObject.getJSONObject(LocationTriggerKeys.LOCATION.getKey()));
        float radius = ConversationKeys.parseFloat(locationTriggerObject, LocationTriggerKeys.RADIUS.getKey());

        return new LocationTrigger(conversationLocation, radius);
    }

    /**
     * Serialize a LocationTrigger object
     *
     * @param nodeSettings    JSONObject to serialize
     * @param locationTrigger LocationTrigger object to serialize
     */
    public static void saveLocationTrigger(JSONObject nodeSettings, LocationTrigger locationTrigger) {

        nodeSettings.put(LocationTriggerKeys.LOCATION.getKey(), LocationParser.saveLocation(locationTrigger.getLocation()));
        nodeSettings.put(LocationTriggerKeys.RADIUS.getKey(), locationTrigger.getRadius());
    }
}
