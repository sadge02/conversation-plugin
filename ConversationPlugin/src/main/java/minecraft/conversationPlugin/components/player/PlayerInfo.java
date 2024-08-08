package minecraft.conversationPlugin.components.player;

import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Information about a player.
 * <p>
 * <ul>
 *     <li>player       Player name</li>
 *     <li>requirements Requirements of the player</li>
 *     <li>variables    Variables of the player</li>
 *     <li>node         Current node the player is in</li>
 * </ul>
 */
public class PlayerInfo {

    private final String player;
    private final ArrayList<String> requirements;
    private final HashMap<String, String> variables;
    private String node;

    public PlayerInfo(String player, ArrayList<String> requirements, HashMap<String, String> variables, String node) {
        this.player = player;
        this.requirements = requirements;
        this.variables = variables;
        this.node = node;
    }

    public void addVariable(String key, String value) {
        variables.put("%" + key + "%", value);
    }

    public void addRequirement(String requirement) {
        requirements.add(requirement);
    }

    public HashMap<String, String> getVariables() {
        return variables;
    }

    public String getVariable(String key) {
        return variables.get(key);
    }

    public String getCurrentNodeID() {
        return node;
    }

    public void setCurrentNode(String currentNode, Conversation conversation) {

        if (currentNode.isEmpty()) {
            Player player = Bukkit.getPlayer(this.player);

            if (player == null) {
                return;
            }
            Node.finishedConversation(conversation, player);
        }
        this.node = currentNode;
    }

    public ArrayList<String> getRequirements() {
        return requirements;
    }

    public void removeVariable(String key) {
        variables.remove(key);
    }

    public void removeRequirement(String requirement) {
        requirements.remove(requirement);
    }

    public void clearRequirements() {
        requirements.clear();
    }

    public void clearVariables() {
        variables.clear();
    }
}
