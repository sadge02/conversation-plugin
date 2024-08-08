package minecraft.conversationPlugin.components.player;

import minecraft.conversationPlugin.ConversationPlugin;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Flags for the player.
 * <p>
 * <ul>
 *     <li>inNode       If the player is currently in a node.</li>
 *     <li>inInputNode  If the player is currently in an input node.</li>
 *     <li>inCutscene   If the player is currently in a cutscene.</li>
 *     <li>inChoiceNode If the player is currently in a choice node.</li>
 *     <li>conversation Conversation the player is currently in.</li>
 *     <li>entities     Entities in choice node that need to be freed.</li>
 * </ul>
 */
public class PlayerFlags {

    private boolean inNode;
    private boolean inInputNode;
    private boolean inCutscene;
    private boolean inChoiceNode;
    private Conversation conversation;
    private final ArrayList<Entity> entities;

    public PlayerFlags() {
        this.inNode = false;
        this.inInputNode = false;
        this.inCutscene = false;
        this.inChoiceNode = false;
        this.conversation = null;
        this.entities = new ArrayList<>();
    }

    public void reset() {
        this.inNode = false;
        this.inInputNode = false;
        this.inCutscene = false;
        this.inChoiceNode = false;
        this.conversation = null;
        clearEntities();
    }

    public static PlayerFlags getPlayerFlags(String playerName) {
        return ConversationPlugin.players.get(playerName);
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public boolean getInInputNode() {
        return inInputNode;
    }

    public void setInInputNode(boolean inInputNode) {
        this.inInputNode = inInputNode;
    }

    public boolean getInNode() {
        return inNode;
    }

    public void setInNode(boolean inNode) {
        this.inNode = inNode;
    }

    public boolean getInCutscene() {
        return inCutscene;
    }

    public void setInCutscene(boolean inCutscene) {
        this.inCutscene = inCutscene;
    }

    public boolean getInChoiceNode() {
        return inChoiceNode;
    }

    public void setInChoiceNode(boolean inChoiceNode) {
        this.inChoiceNode = inChoiceNode;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void clearEntities() {
        for (Entity entity : entities) {
            entity.remove();
        }
        entities.clear();
    }
}
