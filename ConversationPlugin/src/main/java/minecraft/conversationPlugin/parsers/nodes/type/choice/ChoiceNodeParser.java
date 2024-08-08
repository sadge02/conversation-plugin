package minecraft.conversationPlugin.parsers.nodes.type.choice;

import minecraft.conversationPlugin.components.choice.Choice;
import minecraft.conversationPlugin.components.display.DisplaySettings;
import minecraft.conversationPlugin.components.display.types.DisplayTarget;
import minecraft.conversationPlugin.components.location.ConversationLocation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.choice.ChoiceNode;
import minecraft.conversationPlugin.parsers.choices.ChoicesParser;
import minecraft.conversationPlugin.parsers.display.DisplaySettingsParser;
import minecraft.conversationPlugin.parsers.location.LocationParser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Serialize and deserialize choice node objects.
 */
public class ChoiceNodeParser {

    private static final Logger logger = Logger.getLogger(ChoiceNodeParser.class.getName());

    /**
     * Deserialize choice node object from JSON object.
     *
     * @param node               Node object to deserialize
     * @param nodeSettingsObject JSON object to deserialize
     * @return deserialized choice node object
     */
    public static ChoiceNode parseChoiceNode(Node node, JSONObject nodeSettingsObject) {

        try {
            String text = nodeSettingsObject.getString(ChoiceNodeKeys.TEXT.getKey());
            DisplayTarget target = DisplayTarget.valueOf(nodeSettingsObject.getString(ChoiceNodeKeys.TARGET.getKey()));
            String entity = nodeSettingsObject.getString(ChoiceNodeKeys.ENTITY.getKey());
            DisplaySettings settings = DisplaySettingsParser.parseTextSettings(nodeSettingsObject.getJSONObject(ChoiceNodeKeys.DISPLAY_SETTINGS.getKey()));
            ConversationLocation conversationLocation = LocationParser.parseLocation(nodeSettingsObject.getJSONObject(ChoiceNodeKeys.LOCATION.getKey()));
            ArrayList<Choice> choices = ChoicesParser.parseChoices(nodeSettingsObject.getJSONArray(ChoiceNodeKeys.CHOICES.getKey()));

            return new ChoiceNode(node, text, choices, target, entity, conversationLocation, settings);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse choice node object %s: %s", node.getNode(), exception.getMessage()));
        }

        return null;
    }

    /**
     * Serialize choice node object to JSON object.
     *
     * @param choiceNode Choice node object to serialize
     * @return serialized choice node object
     */
    public static JSONObject saveChoiceNodeOptions(ChoiceNode choiceNode) {

        JSONObject choiceNodeOptions = new JSONObject();

        try {
            choiceNodeOptions.put(ChoiceNodeKeys.TEXT.getKey(), choiceNode.getText());
            choiceNodeOptions.put(ChoiceNodeKeys.TARGET.getKey(), choiceNode.getTarget().toString());
            choiceNodeOptions.put(ChoiceNodeKeys.ENTITY.getKey(), choiceNode.getEntity());
            choiceNodeOptions.put(ChoiceNodeKeys.DISPLAY_SETTINGS.getKey(), DisplaySettingsParser.saveTextSettings(choiceNode.getSettings()));
            choiceNodeOptions.put(ChoiceNodeKeys.LOCATION.getKey(), LocationParser.saveLocation(choiceNode.getLocation()));
            choiceNodeOptions.put(ChoiceNodeKeys.CHOICES.getKey(), ChoicesParser.saveChoices(choiceNode.getChoices()));

        } catch (Exception exception) {
            logger.warning(String.format("Failed to save choice node options %s: %s", choiceNode.getNode(), exception.getMessage()));
        }

        return choiceNodeOptions;
    }
}
