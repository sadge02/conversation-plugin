package minecraft.conversationPlugin.parsers.settings;

import minecraft.conversationPlugin.components.settings.Settings;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize settings.
 */
public class SettingsParser {

    private static final Logger logger = Logger.getLogger(SettingsParser.class.getName());

    /**
     * Serialize JSON object to settings object.
     *
     * @param settingsObject JSON object to serialize
     * @return serialized settings object
     */
    public static Settings parseSettings(JSONObject settingsObject) {

        try {
            boolean startMessage = settingsObject.getBoolean(SettingsKeys.START_MESSAGE.getKey());
            boolean endMessage = settingsObject.getBoolean(SettingsKeys.END_MESSAGE.getKey());
            boolean startSound = settingsObject.getBoolean(SettingsKeys.START_SOUND.getKey());
            boolean endSound = settingsObject.getBoolean(SettingsKeys.END_SOUND.getKey());
            boolean blocking = settingsObject.getBoolean(SettingsKeys.BLOCKING.getKey());
            boolean citizens = settingsObject.getBoolean(SettingsKeys.CITIZENS.getKey());
            boolean addOnJoin = settingsObject.getBoolean(SettingsKeys.ADD_ON_JOIN.getKey());
            boolean startOnJoin = settingsObject.getBoolean(SettingsKeys.START_ON_JOIN.getKey());

            return new Settings(startMessage, endMessage, startSound, endSound, blocking, citizens, addOnJoin, startOnJoin);

        } catch (Exception exception) {
            logger.warning("Failed to parse settings.");
        }
        return null;
    }

    /**
     * Deserialize settings object to JSON object.
     *
     * @param settings Settings object to deserialize
     * @return deserialized settings object
     */
    public static JSONObject saveSettings(Settings settings) {

        JSONObject settingsObject = new JSONObject();

        settingsObject.put(SettingsKeys.START_MESSAGE.getKey(), settings.startMessage());
        settingsObject.put(SettingsKeys.END_MESSAGE.getKey(), settings.endMessage());
        settingsObject.put(SettingsKeys.START_SOUND.getKey(), settings.startSound());
        settingsObject.put(SettingsKeys.END_SOUND.getKey(), settings.endSound());
        settingsObject.put(SettingsKeys.BLOCKING.getKey(), settings.blocking());
        settingsObject.put(SettingsKeys.CITIZENS.getKey(), settings.citizens());
        settingsObject.put(SettingsKeys.ADD_ON_JOIN.getKey(), settings.addOnJoin());
        settingsObject.put(SettingsKeys.START_ON_JOIN.getKey(), settings.startOnJoin());

        return settingsObject;
    }
}
