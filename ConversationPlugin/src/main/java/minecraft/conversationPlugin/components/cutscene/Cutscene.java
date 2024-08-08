package minecraft.conversationPlugin.components.cutscene;

import io.papermc.paper.entity.LookAnchor;
import minecraft.conversationPlugin.components.camera.CameraSettings;
import minecraft.conversationPlugin.components.commads.types.CommandExecutionType;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.display.DisplaySettings;
import minecraft.conversationPlugin.components.display.types.DisplayTarget;
import minecraft.conversationPlugin.components.display.types.EntityBind;
import minecraft.conversationPlugin.components.nodes.display.DisplayNode;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.text.Text;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class is composed of methods to create, adjust and run cutscenes.
 */
public class Cutscene {

    /**
     * Get entity from string
     *
     * @param world  World
     * @param entity Entity UUID
     * @return Entity
     */
    public static Entity getEntity(World world, String entity) {

        return world.getEntity(UUID.fromString(entity));
    }

    /**
     * Create a custom text display
     *
     * @param location   Location of the text display
     * @param node       Display node information
     * @param player     Player
     * @param plugin     Plugin
     * @param playerInfo Player info
     * @return TextDisplay
     */
    public static TextDisplay createTextDisplay(Location location, DisplayNode node, Player player, Plugin plugin, PlayerInfo playerInfo) {

        TextDisplay text = location.getWorld().spawn(location, TextDisplay.class);

        DisplaySettings settings = node.getTextSettings();

        // Set processed message
        text.text(Text.processTextComponentMessage(node.getText(), playerInfo, player.getName()));

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
     * Spawn text display in the world
     *
     * @param node        Display node
     * @param player      Player
     * @param plugin      Plugin
     * @param playerFlags Player flags
     * @param playerInfo  Player info
     */
    public static void spawnTextDisplay(DisplayNode node, Player player, Plugin plugin, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        DisplaySettings settings = node.getTextSettings();

        // Spawn text display in a location or on an entity
        if (node.getDisplayTarget() == DisplayTarget.LOCATION) {

            // Get location
            Location location = node.getLocation(player.getWorld());

            // Create text display in set location
            TextDisplay text = createTextDisplay(location.add(0, settings.elevation(), 0), node, player, plugin, playerInfo);

            // Remove text display after duration
            new BukkitRunnable() {
                @Override
                public void run() {
                    text.remove();

                    // Allow player to move in between cutscenes
                    if (playerFlags != null) {
                        playerFlags.setInCutscene(false);
                    }
                }
            }.runTaskLater(plugin, (int) (node.getDuration() * 20L));

        } else {

            // Get entity
            Entity entity = getEntity(player.getWorld(), node.getEntity());

            if (entity == null) {
                playerFlags.setInCutscene(false);
                throw new IllegalArgumentException("Entity not found.");
            }

            // Create text display
            TextDisplay text = createTextDisplay(entity.getLocation().add(0, entity.getHeight() + settings.elevation(), 0), node, player, plugin, playerInfo);

            // If text is binding to an entity create another invisible entity to elevate the text
            Entity invisibleEntity = null;

            if (settings.bind() == EntityBind.LOW_ELEVATION) {
                invisibleEntity = player.getWorld().spawn(entity.getLocation(), Tadpole.class);
            } else if (settings.bind() == EntityBind.MEDIUM_ELEVATION) {
                invisibleEntity = player.getWorld().spawn(entity.getLocation(), Armadillo.class);
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
            }

            // Remove text display after duration
            Entity finalInvisibleEntity = invisibleEntity;
            new BukkitRunnable() {
                @Override
                public void run() {
                    text.remove();
                    if (finalInvisibleEntity != null) {
                        finalInvisibleEntity.remove();
                    }

                    // Allow player to move in between cutscenes
                    if (playerFlags != null) {
                        playerFlags.setInCutscene(false);
                    }
                }
            }.runTaskLater(plugin, (int) (node.getDuration() * 20L));
        }
    }

    /**
     * Get the camera location.
     *
     * @param player         Player
     * @param playerLocation Player location
     * @param location       Location
     * @param cameraSettings Camera settings
     * @return camera location.
     */
    public static Location getCameraLocation(Player player, Location playerLocation, Location location, CameraSettings cameraSettings) {

        // Calculate the radius and multiply it by the distance multiplier
        double radius = playerLocation.distance(location) * cameraSettings.distanceMultiplier();

        // Convert the angle from degrees to radians
        double angleRadians = Math.toRadians(cameraSettings.angleDegrees());

        // Calculate the middle y value between the player and the entity
        double yMid = (playerLocation.getY() + location.getY()) / 2;

        // Calculate the new position
        double x = location.getX() + radius * Math.cos(angleRadians);
        double z = location.getZ() + radius * Math.sin(angleRadians);

        return new Location(player.getWorld(), x, yMid + player.getHeight() * cameraSettings.heightMultiplier(), z);
    }

    /**
     * Set the camera location.
     *
     * @param player           Player
     * @param cameraSettings   Camera settings
     * @param originalLocation Original location of the player
     * @param node             Display node information
     * @param playerFlags      Player flags
     * @param plugin           Plugin
     */
    public static void setCameraLocation(Player player, CameraSettings cameraSettings, Location originalLocation, DisplayNode node, PlayerFlags playerFlags, Plugin plugin) {

        Location cameraLocation;
        Entity entity;

        playerFlags.setInCutscene(false);

        if (node.getDisplayTarget() == DisplayTarget.LOCATION) {
            cameraLocation = getCameraLocation(player, originalLocation,node.getLocation(player.getWorld()), cameraSettings);
        } else {
            entity = getEntity(player.getWorld(), node.getEntity());
            cameraLocation = getCameraLocation(player,originalLocation ,entity.getLocation(), cameraSettings);
        }

        // Teleport the player to the camera location
        player.teleport(cameraLocation);

        // Make the player face the player, entity or location
        switch (cameraSettings.cameraTarget()) {
            case PLAYER:
                player.lookAt(originalLocation.getX(), originalLocation.getY(), originalLocation.getZ(), LookAnchor.EYES);
                break;
            case ENTITY:
                entity = getEntity(player.getWorld(), node.getEntity());
                player.lookAt(entity, LookAnchor.EYES, LookAnchor.EYES);
                break;
            case LOCATION:
                Location location = node.getLocation(player.getWorld());
                player.lookAt(location.getX(), location.getY(), location.getZ(), LookAnchor.EYES);
                break;
        }

        // Zoom in player's camera
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, (int) node.getDuration() * 20, cameraSettings.zoomLevel(), false, false));

        new BukkitRunnable() {
            @Override
            public void run() {
                playerFlags.setInCutscene(true);
            }
        }.runTaskLater(plugin, 3L);
    }

    /**
     * Process cutscene
     *
     * @param nodes            Display nodes
     * @param index            Index
     * @param plugin           Plugin
     * @param player           Player
     * @param conversation     Conversation
     * @param originalLocation Original location
     * @param originalGameMode Original game mode
     * @param playerFlags      Player flags
     * @param playerInfo       Player info
     */
    private static void processCutscene(ArrayList<DisplayNode> nodes, int index, Plugin plugin, Player player, Conversation conversation, Location originalLocation, GameMode originalGameMode, PlayerFlags playerFlags, PlayerInfo playerInfo, NPC npc) {

        // All nodes processed
        if (index >= nodes.size()) {

            // Remove zoom effect
            player.removePotionEffect(PotionEffectType.SLOWNESS);

            // Revert the player's game mode and position
            player.teleport(originalLocation);
            player.setGameMode(originalGameMode);

            // Remove the NPC if it was created
            if (conversation.settings().citizens() && npc != null && npc.isSpawned()) {
                npc.destroy();
            }

            // Remove player from cutscene
            playerFlags.setInCutscene(false);
            playerFlags.setInNode(false);

            DisplayNode.finishedConversation(conversation, player);

            return;
        }

        DisplayNode node = nodes.get(index);

        node.runCommands(CommandExecutionType.START, plugin, player.getName());

        // Spawn the text display and set the camera
        Cutscene.spawnTextDisplay(node, player, plugin, null, playerInfo);
        Cutscene.setCameraLocation(player, node.getCamera(), originalLocation, node, playerFlags, plugin);

        // Wait for the node to finish before processing the next one
        new BukkitRunnable() {
            @Override
            public void run() {

                node.runCommands(CommandExecutionType.END, plugin, player.getName());

                // Set the player's current node to the next node
                conversation.getPlayer(player.getName()).setCurrentNode(node.getNextNode(), conversation);

                // Process the next node
                processCutscene(nodes, index + 1, plugin, player, conversation, originalLocation, originalGameMode, playerFlags, playerInfo, npc);

            }
        }.runTaskLater(plugin, (int) (node.getDuration() * 20L));
    }

    /**
     * Executes cutscenes
     *
     * @param nodes        Display nodes
     * @param plugin       Plugin
     * @param player       Player
     * @param conversation Conversation
     * @param playerFlags  Player flags
     * @param playerInfo   Player info
     */
    public static void executeCutscenes(ArrayList<DisplayNode> nodes, Plugin plugin, Player player, Conversation conversation, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        NPC npc = null;

        playerFlags.setInCutscene(true);

        // Create NPC if citizens is enabled and hide the name tag
        if (conversation.settings().citizens()) {
            npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getName());
            npc.spawn(player.getLocation());
            npc.getEntity().setRotation(player.getLocation().getYaw(), player.getLocation().getPitch());
            npc.getOrAddTrait(SkinTrait.class).setSkinName(player.getName());

            if (npc.getName() != null) {
                npc.setName("");
            }
        }

        // Store the player's original game mode and location
        GameMode gameMode = player.getGameMode();
        Location originalLocation = player.getLocation();

        // Put the player to spectator mode
        player.setGameMode(GameMode.SPECTATOR);

        processCutscene(nodes, 0, plugin, player, conversation, originalLocation, gameMode, playerFlags, playerInfo, npc);
    }

    /**
     * Execute player cutscene
     *
     * @param node        Display node
     * @param player      Player
     * @param plugin      Plugin
     * @param playerFlags Player flags
     * @param playerInfo  Player info
     */
    public static void executePlayerCutscene(DisplayNode node, Player player, Plugin plugin, PlayerFlags playerFlags, PlayerInfo playerInfo) {

        // Spawn the text display
        Cutscene.spawnTextDisplay(node, player, plugin, playerFlags, playerInfo);

        if (node.getDisplayTarget() == DisplayTarget.LOCATION) {

            Location location = node.getLocation(player.getWorld());

            // Set the camera location
            player.lookAt(location.getX(), location.getY(), location.getZ(), LookAnchor.EYES);
        } else {
            // Get entity
            Entity entity = getEntity(player.getWorld(), node.getEntity());

            if (entity == null) {
                playerFlags.setInCutscene(false);
                return;
            }

            // Set the camera location
            player.lookAt(entity, LookAnchor.EYES, LookAnchor.EYES);
        }

        // Zoom in player's camera
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, (int) node.getDuration() * 20, node.getCamera().zoomLevel(), false, false));

        // Allow player to move in order to switch camera
        new BukkitRunnable() {
            @Override
            public void run() {
                playerFlags.setInCutscene(true);
            }
        }.runTaskLater(plugin, 2L);

        // Block player movement
        new BukkitRunnable() {
            @Override
            public void run() {
                playerFlags.setInCutscene(false);
            }
        }.runTaskLater(plugin, (int) (node.getDuration() * 20L));
    }
}
