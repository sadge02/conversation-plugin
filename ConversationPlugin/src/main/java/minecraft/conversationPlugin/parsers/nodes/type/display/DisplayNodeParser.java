package minecraft.conversationPlugin.parsers.nodes.type.display;

import minecraft.conversationPlugin.components.camera.CameraSettings;
import minecraft.conversationPlugin.components.display.DisplaySettings;
import minecraft.conversationPlugin.components.display.types.DisplayTarget;
import minecraft.conversationPlugin.components.location.ConversationLocation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.display.DisplayNode;
import minecraft.conversationPlugin.parsers.camera.CameraParser;
import minecraft.conversationPlugin.parsers.display.DisplaySettingsParser;
import minecraft.conversationPlugin.parsers.location.LocationParser;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize display node objects.
 */
public class DisplayNodeParser {

    private static final Logger logger = Logger.getLogger(DisplayNodeParser.class.getName());

    /**
     * Deserialize display node object from JSON object.
     *
     * @param node               Node object to deserialize
     * @param nodeSettingsObject JSON object to deserialize
     * @return deserialized display node object
     */
    public static DisplayNode parseDisplayNode(Node node, JSONObject nodeSettingsObject) {

        try {
            String text = nodeSettingsObject.getString(DisplayNodeKeys.TEXT.getKey());
            float duration = (float) nodeSettingsObject.getDouble(DisplayNodeKeys.DURATION.getKey());
            DisplayTarget display = DisplayTarget.valueOf(nodeSettingsObject.getString(DisplayNodeKeys.TARGET.getKey()));
            ConversationLocation conversationLocation = LocationParser.parseLocation(nodeSettingsObject.getJSONObject(DisplayNodeKeys.LOCATION.getKey()));
            String entity = nodeSettingsObject.getString(DisplayNodeKeys.ENTITY.getKey());
            CameraSettings cameraSettings = CameraParser.parseCamera(nodeSettingsObject.getJSONObject(DisplayNodeKeys.CAMERA_SETTINGS.getKey()));
            DisplaySettings settings = DisplaySettingsParser.parseTextSettings(nodeSettingsObject.getJSONObject(DisplayNodeKeys.DISPLAY_SETTINGS.getKey()));

            return new DisplayNode(node, text, settings, cameraSettings, duration, conversationLocation, entity, display);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse display node object %s: %s", node.getNode(), exception.getMessage()));
        }

        return null;
    }

    /**
     * Serialize display node object to JSON object.
     *
     * @param displayNode Display node object to serialize
     * @return serialized display node object
     */
    public static JSONObject saveDisplayNodeOptions(DisplayNode displayNode) {

        JSONObject displayNodeOptions = new JSONObject();

        try {
            displayNodeOptions.put(DisplayNodeKeys.TEXT.getKey(), displayNode.getText());
            displayNodeOptions.put(DisplayNodeKeys.DURATION.getKey(), displayNode.getDuration());
            displayNodeOptions.put(DisplayNodeKeys.TARGET.getKey(), displayNode.getDisplayTarget().toString());
            displayNodeOptions.put(DisplayNodeKeys.LOCATION.getKey(), LocationParser.saveLocation(displayNode.getLocation()));
            displayNodeOptions.put(DisplayNodeKeys.ENTITY.getKey(), displayNode.getEntity());
            displayNodeOptions.put(DisplayNodeKeys.DISPLAY_SETTINGS.getKey(), DisplaySettingsParser.saveTextSettings(displayNode.getTextSettings()));
            displayNodeOptions.put(DisplayNodeKeys.CAMERA_SETTINGS.getKey(), CameraParser.saveCamera(displayNode.getCamera()));

        } catch (Exception exception) {
            logger.warning(String.format("Failed to save display node options %s: %s", displayNode.getNode(), exception.getMessage()));
        }

        return displayNodeOptions;
    }
}
