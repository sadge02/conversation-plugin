package minecraft.conversationPlugin.components.camera;

import minecraft.conversationPlugin.components.camera.types.CameraTargetType;
import minecraft.conversationPlugin.components.cutscene.types.CutsceneType;

/**
 * Class that holds camera information.
 * <p>
 * Settings for location, target and zoom of the player camera during a cutscene.
 *
 * @param cutsceneType       Type of the cutscene
 * @param angleDegrees       Angle of the camera. From 0 to 360 degrees. 0 is facing the east, 90 is facing the south, 180 is facing the west and 270 is facing the north.
 * @param distanceMultiplier Multiplier for the distance of the camera (distance between player and the target)
 * @param heightMultiplier   Multiplier for the height of the camera (height of the player)
 * @param cameraTarget       Target that the camera focuses on
 * @param zoomLevel          Zoom level of the camera
 */
public record CameraSettings(CutsceneType cutsceneType, CameraTargetType cameraTarget, double angleDegrees,
                             double distanceMultiplier, double heightMultiplier, int zoomLevel) {
}
