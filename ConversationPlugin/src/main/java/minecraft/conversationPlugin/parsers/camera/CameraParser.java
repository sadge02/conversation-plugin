package minecraft.conversationPlugin.parsers.camera;

import minecraft.conversationPlugin.components.camera.CameraSettings;
import minecraft.conversationPlugin.components.camera.types.CameraTargetType;
import minecraft.conversationPlugin.components.cutscene.types.CutsceneType;
import minecraft.conversationPlugin.parsers.conversation.ConversationKeys;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize camera objects.
 */
public class CameraParser {

    private static final Logger logger = Logger.getLogger(CameraParser.class.getName());

    /**
     * Deserialize camera object from JSON object.
     *
     * @param cameraObject JSON object to deserialize
     * @return deserialized camera object
     */
    public static CameraSettings parseCamera(JSONObject cameraObject) {

        try {
            CutsceneType camera = CutsceneType.valueOf(cameraObject.getString(CameraKeys.CUTSCENE.getKey()));
            CameraTargetType target = CameraTargetType.valueOf(cameraObject.getString(CameraKeys.TARGET.getKey()));
            int angle = cameraObject.getInt(CameraKeys.ANGLE.getKey());
            float distanceMultiplier = ConversationKeys.parseFloat(cameraObject, CameraKeys.DISTANCE_MULTIPLIER.getKey());
            float heightMultiplier = ConversationKeys.parseFloat(cameraObject, CameraKeys.HEIGHT_MULTIPLIER.getKey());
            int zoom = cameraObject.getInt(CameraKeys.ZOOM.getKey());

            return new CameraSettings(camera, target, angle, distanceMultiplier, heightMultiplier, zoom);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse settings: %s", exception.getMessage()));
        }
        return null;
    }

    /**
     * Serialize camera object to JSON object.
     *
     * @param cameraSettings Camera object to serialize
     * @return serialized camera object
     */
    public static JSONObject saveCamera(CameraSettings cameraSettings) {

        JSONObject settingsObject = new JSONObject();

        settingsObject.put(CameraKeys.CUTSCENE.getKey(), cameraSettings.cutsceneType().toString());
        settingsObject.put(CameraKeys.TARGET.getKey(), cameraSettings.cameraTarget().toString());
        settingsObject.put(CameraKeys.ANGLE.getKey(), cameraSettings.angleDegrees());
        settingsObject.put(CameraKeys.DISTANCE_MULTIPLIER.getKey(), cameraSettings.distanceMultiplier());
        settingsObject.put(CameraKeys.HEIGHT_MULTIPLIER.getKey(), cameraSettings.heightMultiplier());
        settingsObject.put(CameraKeys.ZOOM.getKey(), cameraSettings.zoomLevel());

        return settingsObject;
    }
}
