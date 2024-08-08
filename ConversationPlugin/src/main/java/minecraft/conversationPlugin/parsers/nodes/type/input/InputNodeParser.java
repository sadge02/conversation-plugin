package minecraft.conversationPlugin.parsers.nodes.type.input;

import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.input.InputNode;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize input node objects.
 */
public class InputNodeParser {

    private static final Logger logger = Logger.getLogger(InputNodeParser.class.getName());

    /**
     * Deserialize input node object from JSON object.
     *
     * @param node               Node object to deserialize
     * @param nodeSettingsObject JSON object to deserialize
     * @return deserialized input node object
     */
    public static InputNode parseInputNode(Node node, JSONObject nodeSettingsObject) {

        try {
            String text = nodeSettingsObject.getString(InputNodeKeys.TEXT.getKey());
            String variable = nodeSettingsObject.getString(InputNodeKeys.VARIABLE.getKey());

            return new InputNode(node, text, variable);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse input node object %s: %s", node.getNode(), exception.getMessage()));
        }

        return null;
    }

    /**
     * Serialize input node object to JSON object.
     *
     * @param inputNode Input node object to serialize
     * @return serialized input node object
     */
    public static JSONObject saveInputNodeOptions(InputNode inputNode) {

        JSONObject inputNodeOptions = new JSONObject();

        try {
            inputNodeOptions.put(InputNodeKeys.TEXT.getKey(), inputNode.getText());
            inputNodeOptions.put(InputNodeKeys.VARIABLE.getKey(), inputNode.getVariable());

        } catch (Exception exception) {
            logger.warning(String.format("Failed to save input node options %s: %s", inputNode.getNode(), exception.getMessage()));
        }

        return inputNodeOptions;
    }
}
