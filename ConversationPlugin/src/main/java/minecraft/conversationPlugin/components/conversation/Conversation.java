package minecraft.conversationPlugin.components.conversation;

import minecraft.conversationPlugin.ConversationPlugin;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.settings.Settings;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a conversation in the plugin. The conversation consists of:
 *
 * <ul>
 *     <li>Name        Name of the conversation.</li>
 *     <li>StartNodeID ID of the starting node.</li>
 *     <li>Players     Map of player IDs to player info.</li>
 *     <li>Nodes       Map of node IDs to nodes.</li>
 *     <li>Settings    Settings for the conversation.</li>
 * </ul>
 */
public record Conversation(String name, String startNodeID, HashMap<String, PlayerInfo> players,
                           HashMap<String, Node> nodes, Settings settings) {

    /**
     * Checks if a conversation is valid.
     *
     * @param conversationName Name of the conversation
     * @return true if the conversation is valid, false otherwise
     */
    public static boolean isConversationValid(String conversationName) {

        for (Conversation conversation : ConversationPlugin.conversations) {

            if (conversation.name().equals(conversationName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets a conversation by name.
     *
     * @param conversationName Name of the conversation
     * @return the conversation with the given name
     */
    public static Conversation getConversation(String conversationName) {

        for (Conversation conversation : ConversationPlugin.conversations) {

            if (conversation.name().equals(conversationName)) {
                return conversation;
            }
        }

        return null;
    }

    /**
     * Gets all conversations a player is in.
     *
     * @param player Name of the player to get conversations for
     * @return a list of conversations the player is in
     */
    public static ArrayList<Conversation> getPlayerConversations(String player) {

        ArrayList<Conversation> playerConversations = new ArrayList<>();

        for (Conversation conversation : ConversationPlugin.conversations) {

            if (conversation.hasPlayer(player)) {
                playerConversations.add(conversation);
            }
        }

        return playerConversations;
    }


    /**
     * Gets the missing requirements for a player.
     *
     * @param nodeRequirements   Node requirements
     * @param playerRequirements Player's requirements
     * @return the missing requirements
     */
    public String getMissingRequirements(ArrayList<String> nodeRequirements, ArrayList<String> playerRequirements) {

        StringBuilder missingRequirements = new StringBuilder();

        for (String requirement : nodeRequirements) {

            if (!playerRequirements.contains(requirement)) {
                missingRequirements.append(requirement).append(", ");
            }
        }

        // Remove the last comma and space
        if (!missingRequirements.isEmpty()) {
            missingRequirements.setLength(missingRequirements.length() - 2);
        }

        return missingRequirements.toString();
    }

    public void addPlayer(String playerID, PlayerInfo playerInfo) {
        players.put(playerID, playerInfo);
    }

    public void removePlayer(String playerID) {
        players.remove(playerID);
    }

    public boolean hasPlayer(String playerID) {
        return players.containsKey(playerID);
    }

    public boolean hasNode(String nodeID) {
        return nodes.containsKey(nodeID);
    }

    public PlayerInfo getPlayer(String playerID) {
        return players.get(playerID);
    }

    public Node getNode(String nodeID) {
        return nodes.get(nodeID);
    }
}
