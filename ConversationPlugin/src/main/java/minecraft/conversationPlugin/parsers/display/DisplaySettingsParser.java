package minecraft.conversationPlugin.parsers.display;

import minecraft.conversationPlugin.components.display.DisplaySettings;
import minecraft.conversationPlugin.components.display.types.EntityBind;
import minecraft.conversationPlugin.parsers.conversation.ConversationKeys;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize text settings objects.
 */
public class DisplaySettingsParser {

    private static final Logger logger = Logger.getLogger(DisplaySettingsParser.class.getName());

    /**
     * Deserialize text settings object from JSON object.
     *
     * @param textSettingsObject JSON object to deserialize
     * @return deserialized text settings object
     */
    public static DisplaySettings parseTextSettings(JSONObject textSettingsObject) {

        try {
            Display.Billboard face = Display.Billboard.valueOf(textSettingsObject.getString(DisplaySettingsKeys.FACE.getKey()));
            boolean shadow = textSettingsObject.getBoolean(DisplaySettingsKeys.SHADOW.getKey());
            TextDisplay.TextAlignment alignment = TextDisplay.TextAlignment.valueOf(textSettingsObject.getString(DisplaySettingsKeys.ALIGNMENT.getKey()));
            boolean seeThrough = textSettingsObject.getBoolean(DisplaySettingsKeys.SEE_THROUGH.getKey());
            boolean visible = textSettingsObject.getBoolean(DisplaySettingsKeys.VISIBLE.getKey());
            boolean glowing = textSettingsObject.getBoolean(DisplaySettingsKeys.GLOWING.getKey());
            float scale = ConversationKeys.parseFloat(textSettingsObject, DisplaySettingsKeys.SCALE.getKey());
            int background = textSettingsObject.getInt(DisplaySettingsKeys.BACKGROUND.getKey());
            int lineWidth = textSettingsObject.getInt(DisplaySettingsKeys.LINE_WIDTH.getKey());
            EntityBind bind = EntityBind.valueOf(textSettingsObject.getString(DisplaySettingsKeys.BIND_TO_ENTITY.getKey()));
            float elevation = ConversationKeys.parseFloat(textSettingsObject, DisplaySettingsKeys.ELEVATION.getKey());

            return new DisplaySettings(shadow, background, alignment, face, lineWidth, seeThrough, visible, glowing, scale, bind, elevation);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse settings: %s", exception.getMessage()));
        }
        return null;
    }

    /**
     * Serialize text settings object to JSON object.
     *
     * @param displaySettings text settings object to serialize
     * @return serialized text settings object
     */
    public static JSONObject saveTextSettings(DisplaySettings displaySettings) {

        JSONObject settingsObject = new JSONObject();

        settingsObject.put(DisplaySettingsKeys.FACE.getKey(), displaySettings.billboard().toString());
        settingsObject.put(DisplaySettingsKeys.SHADOW.getKey(), displaySettings.shadow());
        settingsObject.put(DisplaySettingsKeys.ALIGNMENT.getKey(), displaySettings.alignment().toString());
        settingsObject.put(DisplaySettingsKeys.SEE_THROUGH.getKey(), displaySettings.seeThrough());
        settingsObject.put(DisplaySettingsKeys.VISIBLE.getKey(), displaySettings.visible());
        settingsObject.put(DisplaySettingsKeys.GLOWING.getKey(), displaySettings.glowing());
        settingsObject.put(DisplaySettingsKeys.SCALE.getKey(), displaySettings.scale());
        settingsObject.put(DisplaySettingsKeys.BACKGROUND.getKey(), displaySettings.background());
        settingsObject.put(DisplaySettingsKeys.LINE_WIDTH.getKey(), displaySettings.lineWidth());
        settingsObject.put(DisplaySettingsKeys.BIND_TO_ENTITY.getKey(), displaySettings.bind().toString());
        settingsObject.put(DisplaySettingsKeys.ELEVATION.getKey(), displaySettings.elevation());

        return settingsObject;
    }
}
