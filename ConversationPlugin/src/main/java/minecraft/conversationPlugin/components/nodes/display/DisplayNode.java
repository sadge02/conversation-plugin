package minecraft.conversationPlugin.components.nodes.display;

import minecraft.conversationPlugin.components.camera.CameraSettings;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.cutscene.Cutscene;
import minecraft.conversationPlugin.components.cutscene.types.CutsceneType;
import minecraft.conversationPlugin.components.display.DisplaySettings;
import minecraft.conversationPlugin.components.display.types.DisplayTarget;
import minecraft.conversationPlugin.components.location.ConversationLocation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.nodes.types.NodeType;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for DisplayNode objects.
 */
public class DisplayNode extends Node {

    private final String text;
    private final float duration;
    private final DisplaySettings displaySettings;
    private final CameraSettings cameraSettings;
    private final ConversationLocation location;
    private final String entity;
    private final DisplayTarget target;

    public DisplayNode(Node node, String text, DisplaySettings displaySettings, CameraSettings cameraSettings, float duration, ConversationLocation location, String entity, DisplayTarget displayTarget) {
        super(node.getNode(), node.getNodeType(), node.getTrigger(), node.getRequirements(), node.getCommands(), node.getNextNode());
        this.text = text;
        this.duration = duration;
        this.displaySettings = displaySettings;
        this.cameraSettings = cameraSettings;
        this.location = location;
        this.entity = entity;
        this.target = displayTarget;
    }

    /**
     * Executes the DisplayNode.
     *
     * @param player       Player
     * @param plugin       Plugin
     * @param conversation Conversation
     * @param playerFlags  PlayerFlags
     * @param playerInfo   PlayerInfo
     */
    public void executeDisplayNode(Player player, Plugin plugin, Conversation conversation, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        ArrayList<DisplayNode> nodes = new ArrayList<>(List.of(this));

        switch (cameraSettings.cutsceneType()) {
            case NONE:
                // Create text display and do not restrict the player view
                Cutscene.spawnTextDisplay(this, player, plugin, playerFlags, playerInfo);
                break;
            case PLAYER:
                // Create text display and restrict the player view
                Cutscene.executePlayerCutscene(this, player, plugin, playerFlags, playerInfo);
                break;
            case CUTSCENE:

                Node node = conversation.getNode(this.getNextNode());

                // Add all nodes that are of type display, are cutscenes and have a time trigger
                while (node != null && node.getNodeType() == NodeType.DISPLAY) {

                    DisplayNode displayNode = (DisplayNode) node;

                    if ((displayNode.getCamera().cutsceneType() != CutsceneType.CUTSCENE) || (node.getTrigger().getType() != TriggerType.TIME)) {
                        break;
                    }

                    nodes.add(displayNode);
                    node = conversation.getNode(displayNode.getNextNode());
                }

                // Run the chained cutscenes
                Cutscene.executeCutscenes(nodes, plugin, player, conversation, playerFlags, playerInfo);
                break;
        }

        float duration = 0;

        for (DisplayNode displayNode : nodes) {
            duration += displayNode.getDuration();
        }

        finishNode(duration, plugin, conversation, player, playerFlags, playerInfo, getNextNode(), false);
    }

    public String getText() {
        return text;
    }

    public float getDuration() {
        return duration;
    }

    public DisplaySettings getTextSettings() {
        return displaySettings;
    }

    public CameraSettings getCamera() {
        return cameraSettings;
    }

    public ConversationLocation getLocation() {
        return location;
    }

    public Location getLocation(World world) {
        return new Location(world, location.x(), location.y(), location.z());
    }

    public String getEntity() {
        return entity;
    }

    public DisplayTarget getDisplayTarget() {
        return target;
    }
}
