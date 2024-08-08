package minecraft.conversationPlugin.parsers.nodes.type.chat;

import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.chat.ChatNode;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * Serialize and deserialize chat node objects.
 */
public class ChatNodeParser {

    private static final Logger logger = Logger.getLogger(ChatNodeParser.class.getName());

    /**
     * Deserialize chat node object from JSON object.
     *
     * @param node               Node object to deserialize
     * @param nodeSettingsObject JSON object to deserialize
     * @return deserialized chat node object
     */
    public static ChatNode parseChatNode(Node node, JSONObject nodeSettingsObject) {

        try {
            String text = nodeSettingsObject.getString(ChatNodeKeys.TEXT.getKey());

            return new ChatNode(node, text);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse chat node object %s: %s", node.getNode(), exception.getMessage()));
        }

        return null;
    }

    /**
     * Serialize chat node object to JSON object.
     *
     * @param chatNode Chat node object to serialize
     * @return serialized chat node object
     */
    public static JSONObject saveChatNodeOptions(ChatNode chatNode) {

        LinkedHashMap<String, Object> chatNodeOptions = new LinkedHashMap<>();

        try {
            chatNodeOptions.put(ChatNodeKeys.TEXT.getKey(), chatNode.getText());

        } catch (Exception exception) {
            logger.warning(String.format("Failed to save chat node options %s: %s", chatNode.getNode(), exception.getMessage()));
        }

        return new JSONObject(chatNodeOptions);
    }
}
