package minecraft.conversationPlugin.components.nodes.choice;

import minecraft.conversationPlugin.components.choice.Choice;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.cutscene.Cutscene;
import minecraft.conversationPlugin.components.display.DisplaySettings;
import minecraft.conversationPlugin.components.display.types.DisplayTarget;
import minecraft.conversationPlugin.components.display.types.EntityBind;
import minecraft.conversationPlugin.components.location.ConversationLocation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.text.Text;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;

/**
 * Class for ChoiceNode objects.
 */
public class ChoiceNode extends Node {

    private final String text;
    private final DisplayTarget target;
    private final String entity;
    private final ConversationLocation conversationLocation;
    private final DisplaySettings settings;
    private final ArrayList<Choice> choices;

    public ChoiceNode(Node node, String text, ArrayList<Choice> choices, DisplayTarget target, String entity, ConversationLocation conversationLocation, DisplaySettings settings) {
        super(node.getNode(), node.getNodeType(), node.getTrigger(), node.getRequirements(), node.getCommands(), node.getNextNode());
        this.text = text;
        this.choices = choices;
        this.target = target;
        this.entity = entity;
        this.conversationLocation = conversationLocation;
        this.settings = settings;
    }

    /**
     * Spawns the entity choices.
     *
     * @param node         Choice node
     * @param choices      List of choices
     * @param plugin       Plugin
     * @param player       Player
     * @param playerInfo   PlayerInfo
     * @param playerFlags  PlayerFlags
     * @param conversation Conversation
     */
    private static void spawnEntityChoices(ChoiceNode node, ArrayList<Choice> choices, Plugin plugin, Player player, PlayerInfo playerInfo, PlayerFlags playerFlags, Conversation conversation) {

        Location targetLocation = null;

        // Spawn choices between player and entity/location
        if (node.target == DisplayTarget.ENTITY) {

            Entity entity = Cutscene.getEntity(player.getWorld(), node.entity);

            if (entity == null) {
                playerFlags.setInChoiceNode(false);
                playerFlags.clearEntities();
                throw new IllegalArgumentException("Entity not found.");
            }

            targetLocation = entity.getLocation();
        } else {
            targetLocation = ConversationLocation.toBukkitLocation(node.conversationLocation, player.getWorld());
        }

        Location playerLocation = player.getLocation();

        // Calculate direction vector from entity to player
        Vector directionToPlayer = playerLocation.toVector().subtract(targetLocation.toVector()).normalize();

        // Offset midpoint location 2 blocks away from entity in direction of player
        Location choiceBaseLocation = targetLocation.clone().add(directionToPlayer);

        // Create perpendicular vector to directionToPlayer
        Vector perpendicularDirectionVector = new Vector(-directionToPlayer.getZ(), 0, directionToPlayer.getX()).normalize();

        // Calculate starting point for choices along perpendicular line
        Location startLocation = choiceBaseLocation.clone().add(perpendicularDirectionVector.multiply(1.25d * (((double) choices.size() - 1.0d) / 2.0d)));

        // Add 1 block elevation to the start location
        startLocation.add(0, 1, 0);

        // If there is only one choice, spawn it in the midpoint
        if (choices.size() == 1) {

            Choice choice = choices.getFirst();

            // Create text display on location
            TextDisplay text = createTextDisplay(choice.text(), choice.displaySettings(), player, plugin, playerInfo, choiceBaseLocation);

            // Create interaction entity so the player can click on the choice
            Interaction interaction = player.getWorld().spawn(choiceBaseLocation, Interaction.class);

            // Add command metadata to the interaction entity
            interaction.setMetadata("choice", new FixedMetadataValue(plugin, String.format("conversation player %s jump %s", conversation.name(), choice.node())));

            // Add text and interaction entities to the player flags, so they can be removed later and won't be lost
            playerFlags.addEntity(text);
            playerFlags.addEntity(interaction);
        } else {
            double idx = 0;
            perpendicularDirectionVector = new Vector(-perpendicularDirectionVector.getX(), 0, -perpendicularDirectionVector.getZ());
            // Spawn the choices with spacings
            for (Choice choice : choices) {

                Location location = startLocation.clone().add(perpendicularDirectionVector.clone().multiply(idx));

                // Create text display on location
                TextDisplay text = createTextDisplay(choice.text(), choice.displaySettings(), player, plugin, playerInfo, location);

                // Create interaction entity so the player can click on the choice
                Interaction interaction = player.getWorld().spawn(location, Interaction.class);

                // Add command metadata to the interaction entity
                interaction.setMetadata("choice", new FixedMetadataValue(plugin, String.format("conversation player %s jump %s", conversation.name(), choice.node())));

                // Add text and interaction entities to the player flags, so they can be removed later and won't be lost
                playerFlags.addEntity(text);
                playerFlags.addEntity(interaction);

                idx += 1.25d;
            }
        }
    }

    /**
     * Creates a text display for choice.
     *
     * @param message    String
     * @param settings   DisplaySettings
     * @param player     Player
     * @param plugin     Plugin
     * @param playerInfo PlayerInfo
     * @param location   Location
     * @return TextDisplay
     */
    private static TextDisplay createTextDisplay(String message, DisplaySettings settings, Player player, Plugin plugin, PlayerInfo playerInfo, org.bukkit.Location location) {

        TextDisplay text = player.getWorld().spawn(location, TextDisplay.class);

        // Translate message
        text.text(Text.processTextComponentMessage(message, playerInfo, player.getName()));

        // Show text only to the player
        text.setVisibleByDefault(settings.visible());
        player.showEntity(plugin, text);

        // Set text face, shadow, alignment, glowing, see through, background color, line width
        text.setBillboard(settings.billboard());
        text.setShadowed(settings.shadow());
        text.setAlignment(settings.alignment());
        text.setGlowing(settings.glowing());
        text.setSeeThrough(settings.seeThrough());
        text.setBackgroundColor(Color.fromARGB(settings.background()));
        text.setLineWidth(settings.lineWidth());

        // Set text scale
        text.setTransformation(
                new Transformation(
                        new Vector3f(),
                        new AxisAngle4f(),
                        new Vector3f(settings.scale(), settings.scale(), settings.scale()),
                        new AxisAngle4f()
                )
        );

        return text;
    }

    /**
     * Spawns the choice text.
     *
     * @param node        ChoiceNode
     * @param plugin      Plugin
     * @param player      Player
     * @param playerInfo  PlayerInfo
     * @param playerFlags PlayerFlags
     */
    private static void spawnChoiceText(ChoiceNode node, Plugin plugin, Player player, PlayerInfo playerInfo, PlayerFlags playerFlags) {

        DisplaySettings settings = node.getSettings();

        // Spawn in set location or follow an entity
        if (node.getTarget() == DisplayTarget.LOCATION) {

            // Create text display on location
            TextDisplay text = createTextDisplay(node.getText(), node.getSettings(), player, plugin, playerInfo, ConversationLocation.toBukkitLocation(node.getLocation(), player.getWorld()).add(0, settings.elevation(), 0));

            // Add text entity to the player flags, so it can be removed later and won't be lost
            playerFlags.addEntity(text);
        } else {
            // Get entity
            Entity entity = Cutscene.getEntity(player.getWorld(), node.getEntity());

            if (entity == null) {
                playerFlags.setInChoiceNode(false);
                throw new IllegalArgumentException("Entity not found.");
            }

            // Create text display
            TextDisplay text = createTextDisplay(node.getText(), node.getSettings(), player, plugin, playerInfo, entity.getLocation().add(0, settings.elevation(), 0));

            // Create invisible entity to elevate text
            Entity invisibleEntity = null;

            if (settings.bind() == EntityBind.LOW_ELEVATION) {
                invisibleEntity = player.getWorld().spawn(entity.getLocation(), Tadpole.class);
            } else if (settings.bind() == EntityBind.MEDIUM_ELEVATION) {
                invisibleEntity = player.getWorld().spawn(entity.getLocation(), Chicken.class);
            } else if (settings.bind() == EntityBind.HIGH_ELEVATION) {
                invisibleEntity = player.getWorld().spawn(entity.getLocation(), ArmorStand.class);
            }

            if (invisibleEntity != null) {
                invisibleEntity.setInvisible(true);
                invisibleEntity.setInvulnerable(true);
                invisibleEntity.setSilent(true);
                entity.addPassenger(invisibleEntity);

                // Add text display as passenger to invisible entity
                entity.addPassenger(invisibleEntity);
                invisibleEntity.addPassenger(text);

                playerFlags.addEntity(invisibleEntity);
            }

            // Add text entity to the player flags, so it can be removed later and won't be lost
            playerFlags.addEntity(text);
        }
    }

    /**
     * Checks if the player can show the choice.
     *
     * @param choiceRequirements Requirements for the choice
     * @param playerRequirements Requirements of the player
     * @return boolean
     */
    public static boolean canShowChoice(ArrayList<String> choiceRequirements, ArrayList<String> playerRequirements) {

        if (choiceRequirements.isEmpty()) {
            return true;
        }

        for (String requirement : choiceRequirements) {

            if (!playerRequirements.contains(requirement)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Executes the ChoiceNode object.
     *
     * @param player       Player
     * @param plugin       Plugin
     * @param conversation Conversation
     * @param playerFlags  PlayerFlags
     * @param playerInfo   PlayerInfo
     */
    public void executeChoiceNode(Player player, Plugin plugin, Conversation conversation, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        playerFlags.setInChoiceNode(true);
        playerFlags.setConversation(conversation);

        if (!text.isEmpty()) {
            // Create and spawn the text display message
            spawnChoiceText(this, plugin, player, playerInfo, playerFlags);
        }

        ArrayList<Choice> entityChoices = new ArrayList<>();
        for (Choice choice : choices) {

            // Only show choices that the player has requirements for
            if (!canShowChoice(choice.requirements(), playerInfo.getRequirements())) {
                continue;
            }

            // Process the choice based on the target
            if (choice.target() == DisplayTarget.LOCATION) {

                // Create text display on location
                Location location = ConversationLocation.toBukkitLocation(choice.location(), player.getWorld());
                TextDisplay text = createTextDisplay(choice.text(), choice.displaySettings(), player, plugin, playerInfo, location);

                // Create interaction entity so the player can click on the choice
                Interaction interaction = player.getWorld().spawn(location, Interaction.class);

                // Add command metadata to the interaction entity
                interaction.setMetadata("choice", new FixedMetadataValue(plugin, String.format("conversation player %s jump %s", conversation.name(), choice.node())));

                // Add text and interaction entities to the player flags, so they can be removed later and won't be lost
                playerFlags.addEntity(text);
                playerFlags.addEntity(interaction);
            } else {
                entityChoices.add(choice);
            }
        }

        if (!entityChoices.isEmpty()) {
            spawnEntityChoices(this, entityChoices, plugin, player, playerInfo, playerFlags, conversation);
        }
    }

    public String getText() {
        return text;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public DisplayTarget getTarget() {
        return target;
    }

    public String getEntity() {
        return entity;
    }

    public ConversationLocation getLocation() {
        return conversationLocation;
    }

    public DisplaySettings getSettings() {
        return settings;
    }
}
