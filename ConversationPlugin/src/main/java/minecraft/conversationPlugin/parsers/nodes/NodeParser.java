package minecraft.conversationPlugin.parsers.nodes;

import minecraft.conversationPlugin.components.commads.Command;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.actionbar.ActionBarNode;
import minecraft.conversationPlugin.components.nodes.bossbar.BossBarNode;
import minecraft.conversationPlugin.components.nodes.chat.ChatNode;
import minecraft.conversationPlugin.components.nodes.display.DisplayNode;
import minecraft.conversationPlugin.components.nodes.title.TitleNode;
import minecraft.conversationPlugin.components.nodes.types.NodeType;
import minecraft.conversationPlugin.components.nodes.choice.ChoiceNode;
import minecraft.conversationPlugin.components.nodes.input.InputNode;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.parsers.commands.CommandParser;
import minecraft.conversationPlugin.parsers.nodes.type.actionbar.ActionBarNodeParser;
import minecraft.conversationPlugin.parsers.nodes.type.bossbar.BossBarNodeParser;
import minecraft.conversationPlugin.parsers.nodes.type.chat.ChatNodeParser;
import minecraft.conversationPlugin.parsers.nodes.type.choice.ChoiceNodeParser;
import minecraft.conversationPlugin.parsers.nodes.type.display.DisplayNodeParser;
import minecraft.conversationPlugin.parsers.nodes.type.input.InputNodeParser;
import minecraft.conversationPlugin.parsers.nodes.type.title.TitleNodeParser;
import minecraft.conversationPlugin.parsers.requirements.RequirementsParser;
import minecraft.conversationPlugin.parsers.trigger.TriggerParser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Serialize and deserialize nodes.
 */
public class NodeParser {

    private static final Logger logger = Logger.getLogger(NodeParser.class.getName());

    /**
     * Serialize JSON object to nodes object.
     *
     * @param nodesObject JSON object to serialize
     * @return serialized nodes object
     */
    public static HashMap<String, Node> parseNodes(JSONObject nodesObject) {

        HashMap<String, Node> nodes = new HashMap<>();

        for (String node : nodesObject.keySet()) {
            try {
                nodes.put(node, NodeParser.parseNode(nodesObject.getJSONObject(node), node));

            } catch (Exception exception) {
                logger.warning(String.format("Failed to parse node %s: %s", node, exception.getMessage()));
            }
        }

        return nodes;
    }

    /**
     * Deserialize node object to JSON object.
     *
     * @param nodeObject Node object to deserialize
     * @param nodeID     Node ID
     * @return deserialized node object
     */
    public static Node parseNode(JSONObject nodeObject, String nodeID) {

        try {
            NodeType type = NodeType.valueOf(nodeObject.getString(NodeKeys.NODE_TYPE.getKey()));
            Trigger trigger = TriggerParser.parseTrigger(nodeObject.getJSONObject(NodeKeys.TRIGGER.getKey()));
            ArrayList<String> requirements = RequirementsParser.parseRequirements(nodeObject.getJSONArray(NodeKeys.REQUIREMENTS.getKey()));
            ArrayList<Command> commands = CommandParser.parseCommands(nodeObject.getJSONArray(NodeKeys.COMMANDS.getKey()));
            String nextNode = nodeObject.getString(NodeKeys.NEXT_NODE.getKey());

            Node node = new Node(nodeID, type, trigger, requirements, commands, nextNode);

            // Parse node settings
            JSONObject nodeSettingsObject = nodeObject.getJSONObject(NodeKeys.NODE_SETTINGS.getKey());

            return switch (type) {
                case TITLE, SUBTITLE, TITLE_SUBTITLE -> TitleNodeParser.parseTitleNode(node, nodeSettingsObject);
                case CHAT -> ChatNodeParser.parseChatNode(node, nodeSettingsObject);
                case INPUT -> InputNodeParser.parseInputNode(node, nodeSettingsObject);
                case CHOICE -> ChoiceNodeParser.parseChoiceNode(node, nodeSettingsObject);
                case ACTION_BAR -> ActionBarNodeParser.parseActionBarNode(node, nodeSettingsObject);
                case BOSS_BAR -> BossBarNodeParser.parseBossBarNode(node, nodeSettingsObject);
                case DISPLAY -> DisplayNodeParser.parseDisplayNode(node, nodeSettingsObject);
            };
        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse node %s: %s", nodeID, exception.getMessage()));
        }

        return null;
    }

    /**
     * Deserialize nodes object to JSON object.
     *
     * @param nodes Nodes object map to deserialize
     * @return deserialized nodes object
     */
    public static JSONObject saveNodes(HashMap<String, Node> nodes) {

        JSONObject nodesObject = new JSONObject();

        for (String nodeID : nodes.keySet()) {
            Node node = nodes.get(nodeID);
            switch (node.getNodeType()) {
                case TITLE, SUBTITLE, TITLE_SUBTITLE ->
                        nodesObject.put(nodeID, NodeParser.saveNode(node, TitleNodeParser.saveTitleNodeOptions((TitleNode) node)));
                case CHAT ->
                        nodesObject.put(nodeID, NodeParser.saveNode(node, ChatNodeParser.saveChatNodeOptions((ChatNode) node)));
                case INPUT ->
                        nodesObject.put(nodeID, NodeParser.saveNode(node, InputNodeParser.saveInputNodeOptions((InputNode) node)));
                case CHOICE ->
                        nodesObject.put(nodeID, NodeParser.saveNode(node, ChoiceNodeParser.saveChoiceNodeOptions((ChoiceNode) node)));
                case ACTION_BAR ->
                        nodesObject.put(nodeID, NodeParser.saveNode(node, ActionBarNodeParser.saveActionBarNodeOptions((ActionBarNode) node)));
                case BOSS_BAR ->
                        nodesObject.put(nodeID, NodeParser.saveNode(node, BossBarNodeParser.saveBossBarNodeOptions((BossBarNode) node)));
                case DISPLAY ->
                        nodesObject.put(nodeID, NodeParser.saveNode(node, DisplayNodeParser.saveDisplayNodeOptions((DisplayNode) node)));
            }
        }

        return nodesObject;
    }

    /**
     * Deserialize node object to JSON object.
     *
     * @param node               Node object to deserialize
     * @param nodeSettingsObject Node options object to deserialize
     * @return deserialized node object
     */
    public static JSONObject saveNode(Node node, JSONObject nodeSettingsObject) {

        JSONObject nodeObject = new JSONObject();

        try {
            nodeObject.put(NodeKeys.REQUIREMENTS.getKey(), RequirementsParser.saveRequirements(node.getRequirements()));
            nodeObject.put(NodeKeys.TRIGGER.getKey(), TriggerParser.saveTrigger(node.getTrigger()));
            nodeObject.put(NodeKeys.NEXT_NODE.getKey(), node.getNextNode());
            nodeObject.put(NodeKeys.NODE_TYPE.getKey(), node.getNodeType().toString());
            nodeObject.put(NodeKeys.NODE_SETTINGS.getKey(), nodeSettingsObject);
            nodeObject.put(NodeKeys.COMMANDS.getKey(), CommandParser.saveCommands(node.getCommands()));

        } catch (Exception exception) {
            logger.severe(String.format("Failed to save node %s: %s", node.getNode(), exception.getMessage()));
        }

        return nodeObject;
    }
}
