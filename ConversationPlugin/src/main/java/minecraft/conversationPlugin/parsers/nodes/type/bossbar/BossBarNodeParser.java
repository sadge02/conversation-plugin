package minecraft.conversationPlugin.parsers.nodes.type.bossbar;

import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.bossbar.BossBarNode;
import minecraft.conversationPlugin.parsers.conversation.ConversationKeys;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize boss bar node objects.
 */
public class BossBarNodeParser {

    private static final Logger logger = Logger.getLogger(BossBarNodeParser.class.getName());

    /**
     * Deserialize boss bar node object from JSON object.
     *
     * @param node               Node object to deserialize
     * @param nodeSettingsObject JSON object to deserialize
     * @return deserialized boss bar node object
     */
    public static BossBarNode parseBossBarNode(Node node, JSONObject nodeSettingsObject) {

        try {
            String text = nodeSettingsObject.getString(BossBarNodeKeys.TEXT.getKey());
            float progress = ConversationKeys.parseFloat(nodeSettingsObject, BossBarNodeKeys.PROGRESS.getKey());
            float duration = ConversationKeys.parseFloat(nodeSettingsObject, BossBarNodeKeys.DURATION.getKey());

            return new BossBarNode(node, text, progress, duration);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse boss bar node object %s: %s", node.getNode(), exception.getMessage()));
        }

        return null;
    }

    /**
     * Serialize boss bar node object to JSON object.
     *
     * @param bossBarNode Boss bar node object to serialize
     * @return serialized boss bar node object
     */
    public static JSONObject saveBossBarNodeOptions(BossBarNode bossBarNode) {

        JSONObject bossBarNodeOptions = new JSONObject();

        try {
            bossBarNodeOptions.put(BossBarNodeKeys.TEXT.getKey(), bossBarNode.getText());
            bossBarNodeOptions.put(BossBarNodeKeys.PROGRESS.getKey(), bossBarNode.getProgress());
            bossBarNodeOptions.put(BossBarNodeKeys.DURATION.getKey(), bossBarNode.getDuration());

        } catch (Exception exception) {
            logger.warning(String.format("Failed to save boss bar node options %s: %s", bossBarNode.getNode(), exception.getMessage()));
        }

        return bossBarNodeOptions;
    }
}
