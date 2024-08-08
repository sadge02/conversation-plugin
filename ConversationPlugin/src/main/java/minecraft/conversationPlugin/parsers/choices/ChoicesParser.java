package minecraft.conversationPlugin.parsers.choices;

import minecraft.conversationPlugin.components.choice.Choice;
import minecraft.conversationPlugin.components.display.DisplaySettings;
import minecraft.conversationPlugin.components.display.types.DisplayTarget;
import minecraft.conversationPlugin.components.location.ConversationLocation;
import minecraft.conversationPlugin.parsers.display.DisplaySettingsParser;
import minecraft.conversationPlugin.parsers.location.LocationParser;
import minecraft.conversationPlugin.parsers.requirements.RequirementsParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Serialize and deserialize choices objects.
 */
public class ChoicesParser {

    private static final Logger logger = Logger.getLogger(ChoicesParser.class.getName());

    /**
     * Deserialize choices object from JSON object.
     *
     * @param choicesObject JSON object to deserialize
     * @return deserialized choices list
     */
    public static ArrayList<Choice> parseChoices(JSONArray choicesObject) {

        ArrayList<Choice> choices = new ArrayList<>();

        for (int i = 0; i < choicesObject.length(); i++) {

            try {
                choices.add(ChoicesParser.parseChoice(choicesObject.getJSONObject(i)));
            } catch (Exception exception) {
                logger.warning(String.format("Failed to parse choice %s: %s", i, exception.getMessage()));
            }
        }

        return choices;
    }

    /**
     * Deserialize choice object from JSON object.
     *
     * @param choiceObject JSON object to deserialize
     * @return deserialized choice object
     */
    public static Choice parseChoice(JSONObject choiceObject) {

        String node = choiceObject.getString(ChoiceKeys.NODE.getKey());
        String text = choiceObject.getString(ChoiceKeys.TEXT.getKey());
        ArrayList<String> requirements = RequirementsParser.parseRequirements(choiceObject.getJSONArray(ChoiceKeys.REQUIREMENTS.getKey()));
        ConversationLocation conversationLocation = LocationParser.parseLocation(choiceObject.getJSONObject(ChoiceKeys.LOCATION.getKey()));
        DisplayTarget target = DisplayTarget.valueOf(choiceObject.getString(ChoiceKeys.TARGET.getKey()));
        DisplaySettings displaySettings = DisplaySettingsParser.parseTextSettings(choiceObject.getJSONObject(ChoiceKeys.DISPLAY_SETTINGS.getKey()));

        return new Choice(text, node, conversationLocation, target, displaySettings, requirements);
    }

    /**
     * Serialize choices object to JSON object.
     *
     * @param choices Choices map to serialize
     * @return serialized choices object
     */
    public static JSONArray saveChoices(ArrayList<Choice> choices) {

        JSONArray choicesObject = new JSONArray();

        for (Choice choice : choices) {

            try {
                choicesObject.put(ChoicesParser.saveChoice(choice));
            } catch (Exception exception) {
                logger.warning(String.format("Failed to save choice %s: %s", choice.text(), exception.getMessage()));
            }
        }

        return choicesObject;
    }

    /**
     * Serialize choice object to JSON object.
     *
     * @param choice Choice object to serialize
     * @return serialized choice object
     */
    public static JSONObject saveChoice(Choice choice) {

        JSONObject choiceObject = new JSONObject();

        choiceObject.put(ChoiceKeys.NODE.getKey(), choice.node());
        choiceObject.put(ChoiceKeys.TEXT.getKey(), choice.text());
        choiceObject.put(ChoiceKeys.REQUIREMENTS.getKey(), RequirementsParser.saveRequirements(choice.requirements()));
        choiceObject.put(ChoiceKeys.LOCATION.getKey(), LocationParser.saveLocation(choice.location()));
        choiceObject.put(ChoiceKeys.TARGET.getKey(), choice.target().toString());
        choiceObject.put(ChoiceKeys.DISPLAY_SETTINGS.getKey(), DisplaySettingsParser.saveTextSettings(choice.displaySettings()));

        return choiceObject;
    }
}
