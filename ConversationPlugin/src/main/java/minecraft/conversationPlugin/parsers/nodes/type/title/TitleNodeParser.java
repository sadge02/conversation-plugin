package minecraft.conversationPlugin.parsers.nodes.type.title;

import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.types.NodeType;
import minecraft.conversationPlugin.components.nodes.title.TitleNode;
import minecraft.conversationPlugin.parsers.conversation.ConversationKeys;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize title node objects.
 */
public class TitleNodeParser {

    private static final Logger logger = Logger.getLogger(TitleNodeParser.class.getName());

    /**
     * Deserialize title node object from JSON object.
     *
     * @param node               Node object to deserialize
     * @param nodeSettingsObject JSON object to deserialize
     * @return deserialized title node object
     */
    public static TitleNode parseTitleNode(Node node, JSONObject nodeSettingsObject) {

        try {
            String title = "";
            if (node.getNodeType() == NodeType.TITLE || node.getNodeType() == NodeType.TITLE_SUBTITLE) {
                title = nodeSettingsObject.getString(TitleNodeKeys.TITLE.getKey());
            }
            String subtitle = "";
            if (node.getNodeType() == NodeType.SUBTITLE || node.getNodeType() == NodeType.TITLE_SUBTITLE) {
                subtitle = nodeSettingsObject.getString(TitleNodeKeys.SUBTITLE.getKey());
            }
            float fadeIn = ConversationKeys.parseFloat(nodeSettingsObject, TitleNodeKeys.FADE_IN.getKey());
            float fadeOut = ConversationKeys.parseFloat(nodeSettingsObject, TitleNodeKeys.FADE_OUT.getKey());
            float duration = ConversationKeys.parseFloat(nodeSettingsObject, TitleNodeKeys.DURATION.getKey());

            return new TitleNode(node, duration, title, subtitle, fadeIn, fadeOut);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse title node object %s: %s", node.getNode(), exception.getMessage()));
        }

        return null;
    }

    /**
     * Serialize title node object to JSON object.
     *
     * @param titleNode Title node object to serialize
     * @return serialized title node object
     */
    public static JSONObject saveTitleNodeOptions(TitleNode titleNode) {

        JSONObject titleNodeOptions = new JSONObject();

        try {
            if (titleNode.getNodeType() == NodeType.TITLE || titleNode.getNodeType() == NodeType.TITLE_SUBTITLE) {
                titleNodeOptions.put(TitleNodeKeys.TITLE.getKey(), titleNode.getTitle());
            }
            if (titleNode.getNodeType() == NodeType.SUBTITLE || titleNode.getNodeType() == NodeType.TITLE_SUBTITLE) {
                titleNodeOptions.put(TitleNodeKeys.SUBTITLE.getKey(), titleNode.getSubtitle());
            }
            titleNodeOptions.put(TitleNodeKeys.FADE_IN.getKey(), titleNode.getFadeIn());
            titleNodeOptions.put(TitleNodeKeys.FADE_OUT.getKey(), titleNode.getFadeOut());
            titleNodeOptions.put(TitleNodeKeys.DURATION.getKey(), titleNode.getDuration());

        } catch (Exception exception) {
            logger.warning(String.format("Failed to save title node options %s: %s", titleNode.getNode(),  exception.getMessage()));
        }

        return titleNodeOptions;
    }
}
