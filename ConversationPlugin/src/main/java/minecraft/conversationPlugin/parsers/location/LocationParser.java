package minecraft.conversationPlugin.parsers.location;

import minecraft.conversationPlugin.components.location.ConversationLocation;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize location objects.
 */
public class LocationParser {

    private static final Logger logger = Logger.getLogger(LocationParser.class.getName());

    /**
     * Deserialize location object from JSON object.
     *
     * @param locationObject JSON object to deserialize
     * @return deserialized location object
     */
    public static ConversationLocation parseLocation(JSONObject locationObject) {

        try {
            // Parse location
            int x = locationObject.getInt("x");
            int y = locationObject.getInt("y");
            int z = locationObject.getInt("z");

            return new ConversationLocation(x, y, z);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse location object: %s", exception.getMessage()));
        }

        return null;
    }

    /**
     * Serialize location object to JSON object.
     *
     * @param conversationLocation Location object to serialize
     * @return serialized location object
     */
    public static JSONObject saveLocation(ConversationLocation conversationLocation) {

        JSONObject locationObject = new JSONObject();

        try {
            locationObject.put("x", conversationLocation.x());
            locationObject.put("y", conversationLocation.y());
            locationObject.put("z", conversationLocation.z());

        } catch (Exception exception) {
            logger.warning(String.format("Failed to save location object: %s", exception.getMessage()));
        }

        return locationObject;
    }
}
