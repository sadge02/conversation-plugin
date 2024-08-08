package minecraft.conversationPlugin.parsers.conversation;

import minecraft.conversationPlugin.ConversationPlugin;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.settings.Settings;
import minecraft.conversationPlugin.parsers.nodes.NodeParser;
import minecraft.conversationPlugin.parsers.players.PlayerParser;
import minecraft.conversationPlugin.parsers.settings.SettingsParser;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Serialize and deserialize conversations.
 */
public class ConversationParser {

    private static final Logger logger = Logger.getLogger(ConversationParser.class.getName());

    /**
     * Serializes all JSON files in the [conversations] directory to conversation objects.
     *
     * @param directory Directory to serialize conversations from
     */
    public static void loadConversations(File directory) {

        if (directory != null && directory.exists()) {

            try {
                for (File file : Objects.requireNonNull(directory.listFiles())) {

                    Conversation conversation = ConversationParser.loadConversation(file);

                    if (conversation != null) {
                        ConversationPlugin.conversations.add(conversation);
                    }
                }
            } catch (Exception exception) {
                logger.warning(String.format("Failed to load conversations: %s", exception.getMessage()));
            }
        }
    }

    /**
     * Serializes conversation object from JSON file.
     *
     * @param file File to serialize conversation from
     * @return serialized conversation object
     */
    public static Conversation loadConversation(File file) {

        Conversation conversation = null;

        if (file.isFile() && file.getName().endsWith(".json")) {

            try (FileReader reader = new FileReader(file)) {
                conversation = ConversationParser.parseConversation(new JSONObject(new JSONTokener(reader)), file.getName().replace(".json", ""));
            } catch (IOException exception) {
                logger.warning(String.format("Failed to load conversation: %s", exception.getMessage()));
            }
        }

        return conversation;
    }

    /**
     * Deserializes all conversation objects to JSON files in the [conversations] directory.
     *
     * @param directory Directory to deserialize conversations to
     */
    public static void saveConversations(File directory) {

        for (Conversation conversation : ConversationPlugin.conversations) {

            try {
                JSONObject conversationObject = ConversationParser.saveConversation(conversation);
                FileWriter savedFile = new FileWriter(String.format("%s/%s.json", directory.getAbsolutePath(), conversation.name()));
                savedFile.write(conversationObject.toString());
                savedFile.close();
                logger.info(String.format("Saved conversation %s", savedFile));
            } catch (Exception exception) {
                logger.warning(String.format("Failed to save conversation %s: %s", conversation.name(), exception.getMessage()));
            }
        }
    }

    /**
     * Deserializes conversation object to JSON object.
     *
     * @param conversation Conversation object to create JSON from
     * @return deserialized conversation object
     */
    public static JSONObject saveConversation(Conversation conversation) {

        JSONObject conversationObject = new JSONObject();

        conversationObject.put(ConversationKeys.START_NODE.getKey(), conversation.startNodeID());
        conversationObject.put(ConversationKeys.SETTINGS.getKey(), SettingsParser.saveSettings(conversation.settings()));
        conversationObject.put(ConversationKeys.PLAYERS.getKey(), PlayerParser.savePlayers(conversation.players()));
        conversationObject.put(ConversationKeys.NODES.getKey(), NodeParser.saveNodes(conversation.nodes()));

        return conversationObject;
    }

    /**
     * Parses conversation from JSON object.
     *
     * @param conversationObject JSON object to parse conversation from
     * @param name               Name of the conversation
     * @return parsed conversation
     */
    public static Conversation parseConversation(JSONObject conversationObject, String name) {

        try {
            String startNode = conversationObject.getString(ConversationKeys.START_NODE.getKey());
            Settings settings = SettingsParser.parseSettings(conversationObject.getJSONObject(ConversationKeys.SETTINGS.getKey()));
            HashMap<String, PlayerInfo> players = PlayerParser.parsePlayers(conversationObject.getJSONObject(ConversationKeys.PLAYERS.getKey()));
            HashMap<String, Node> nodes = NodeParser.parseNodes(conversationObject.getJSONObject(ConversationKeys.NODES.getKey()));

            return new Conversation(name, startNode, players, nodes, settings);

        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse conversation %s: %s", name, exception.getMessage()));
        }

        return null;
    }
}
