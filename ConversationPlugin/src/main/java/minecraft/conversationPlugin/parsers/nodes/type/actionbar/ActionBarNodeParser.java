package minecraft.conversationPlugin.parsers.nodes.type.actionbar;

import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.actionbar.ActionBarNode;
import minecraft.conversationPlugin.parsers.conversation.ConversationKeys;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize action bar node objects.
 */
public class ActionBarNodeParser {

    private static final Logger logger = Logger.getLogger(ActionBarNodeParser.class.getName());

    /**
     * Deserialize action bar node object from JSON object.
     *
     * @param node               Node object to deserialize
     * @param nodeSettingsObject JSON object to deserialize
     * @return deserialized action bar node object
     */
    public static ActionBarNode parseActionBarNode(Node node, JSONObject nodeSettingsObject) {

        try {
            // Parse text, duration
            String text = nodeSettingsObject.getString(ActionBarNodeKeys.TEXT.getKey());
            float duration = ConversationKeys.parseFloat(nodeSettingsObject, ActionBarNodeKeys.DURATION.getKey());

            return new ActionBarNode(node, text, duration);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse action bar node object %s: %s", node.getNode(), exception.getMessage()));
        }

        return null;
    }

    /**
     * Serialize action bar node object to JSON object.
     *
     * @param actionBarNode action bar node object to serialize
     * @return serialized action bar node object
     */
    public static JSONObject saveActionBarNodeOptions(ActionBarNode actionBarNode) {

        JSONObject chatNodeOptions = new JSONObject();

        try {
            chatNodeOptions.put(ActionBarNodeKeys.TEXT.getKey(), actionBarNode.getText());
            chatNodeOptions.put(ActionBarNodeKeys.DURATION.getKey(), actionBarNode.getDuration());

        } catch (Exception exception) {
            logger.warning(String.format("Failed to save action bar node options %s: %s", actionBarNode.getNode(), exception.getMessage()));
        }

        return chatNodeOptions;
    }
}
