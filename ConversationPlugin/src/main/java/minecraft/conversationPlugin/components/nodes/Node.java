package minecraft.conversationPlugin.components.nodes;

import minecraft.conversationPlugin.components.commads.Command;
import minecraft.conversationPlugin.components.commads.types.CommandExecutionType;
import minecraft.conversationPlugin.components.commads.types.CommandSenderType;
import minecraft.conversationPlugin.components.controller.ConversationController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.actionbar.ActionBarNode;
import minecraft.conversationPlugin.components.nodes.bossbar.BossBarNode;
import minecraft.conversationPlugin.components.nodes.chat.ChatNode;
import minecraft.conversationPlugin.components.nodes.choice.ChoiceNode;
import minecraft.conversationPlugin.components.nodes.display.DisplayNode;
import minecraft.conversationPlugin.components.nodes.input.InputNode;
import minecraft.conversationPlugin.components.nodes.title.TitleNode;
import minecraft.conversationPlugin.components.nodes.types.NodeType;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents a node in a conversation.
 * <ul>
 *     <li>NodeID       ID of the node.</li>
 *     <li>Type         Type of the node.</li>
 *     <li>Trigger      How the node is triggered.</li>
 *     <li>Command      Command to execute.</li>
 *     <li>NextNodeID   ID of the next node.</li>
 *     <li>Requirements Requirements to execute the node.</li>
 * </ul>
 */
public class Node {

    private final String node;
    private final NodeType type;
    private final Trigger trigger;
    private final ArrayList<Command> commands;
    private final String nextNode;
    private ArrayList<String> requirements;

    public Node(String node, NodeType type, Trigger trigger, ArrayList<String> requirements, ArrayList<Command> commands, String nextNode) {
        this.node = node;
        this.type = type;
        this.trigger = trigger;
        this.requirements = requirements;
        this.commands = commands;
        this.nextNode = nextNode;
    }

    /**
     * Checks if the conversation has finished.
     *
     * <p>
     * Conversation is finished when the node ID is empty string.
     *
     * @param conversation Conversation
     * @param player       Player
     * @return true if the conversation has finished, false otherwise
     */
    public static boolean finishedConversation(Conversation conversation, Player player) {

        if (conversation.getPlayer(player.getName()) == null) {
            return true;
        }

        if (conversation.getPlayer(player.getName()).getCurrentNodeID().isEmpty()) {

            if (conversation.settings().endMessage()) {
                player.sendMessage(Component.text(String.format("Conversation %s has been finished.", conversation.name())).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, true));
            }

            if (conversation.settings().endSound()) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }

            String command = String.format("conversation %s %s remove", player.getName(), conversation.name());

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);

            return true;
        }

        return false;
    }

    /**
     * Executes the node.
     *
     * @param plugin       Plugin
     * @param conversation Conversation
     * @param playerName   Player name
     */
    public void executeNode(Plugin plugin, Conversation conversation, String playerName) {

        Player player = Bukkit.getPlayer(playerName);
        PlayerFlags playerFlags = PlayerFlags.getPlayerFlags(playerName);
        PlayerInfo playerInfo = conversation.getPlayer(playerName);

        if (playerFlags == null || playerInfo == null || player == null) {
            return;
        }

        // Check if the conversation has finished
        if (finishedConversation(conversation, player)) {
            return;
        }

        // Check if the conversation can run
        if (!ConversationController.canExecuteConversation(conversation, playerName)) {
            return;
        }

        Node node = conversation.nodes().get(playerInfo.getCurrentNodeID());

        if (node == null) {
            return;
        }

        // Check if the player has the node requirements
        if (!checkRequirements(conversation, node, player, playerName)) {
            return;
        }

        playerFlags.setInNode(true);

        // Run commands at the beginning
        runCommands(CommandExecutionType.START, plugin, playerName);

        // Execute the node
        switch (node.getNodeType()) {
            case ACTION_BAR ->
                    ((ActionBarNode) node).executeActionBarNode(player, plugin, conversation, playerFlags, playerInfo);
            case CHAT -> ((ChatNode) node).executeChatNode(player, plugin, conversation, playerFlags, playerInfo);
            case TITLE, SUBTITLE, TITLE_SUBTITLE ->
                    ((TitleNode) node).executeTitleNode(player, plugin, conversation, playerFlags, playerInfo);
            case BOSS_BAR ->
                    ((BossBarNode) node).executeBossBarNode(player, plugin, conversation, playerFlags, playerInfo);
            case CHOICE -> ((ChoiceNode) node).executeChoiceNode(player, plugin, conversation, playerFlags, playerInfo);
            case INPUT -> ((InputNode) node).executeInputNode(player, conversation, playerFlags, playerInfo);
            case DISPLAY ->
                    ((DisplayNode) node).executeDisplayNode(player, plugin, conversation, playerFlags, playerInfo);
        }
    }

    /**
     * Finishes the node.
     *
     * @param duration     Duration
     * @param plugin       Plugin
     * @param conversation Conversation
     * @param player       Player
     * @param playerFlags  Player flags
     * @param playerInfo   Player info
     * @param nextNode     Next node
     */
    public void finishNode(float duration, Plugin plugin, Conversation conversation, Player player, PlayerFlags playerFlags, PlayerInfo playerInfo, String nextNode) {

        new BukkitRunnable() {
            @Override
            public void run() {
                // Set the next node
                playerInfo.setCurrentNode(nextNode, conversation);

                // Remove blocking
                playerFlags.setInNode(false);

                // Run commands at the end
                runCommands(CommandExecutionType.END, plugin, player.getName());

                if (finishedConversation(conversation, player)) {
                    return;
                }

                // Run the next node if it is a time trigger
                if (conversation.getNode(nextNode).getTrigger().getType() == TriggerType.TIME) {
                    ConversationController.executeConversation(conversation, player.getName(), plugin);
                }
            }
        }.runTaskLater(plugin, (int) (20 * duration));
    }

    /**
     * Finishes the node.
     *
     * @param duration     Duration
     * @param plugin       Plugin
     * @param conversation Conversation
     * @param player       Player
     * @param playerFlags  Player flags
     * @param playerInfo   Player info
     * @param nextNode     Next node
     * @param runCommands  Should the commands be executed
     */
    public void finishNode(float duration, Plugin plugin, Conversation conversation, Player player, PlayerFlags playerFlags, PlayerInfo playerInfo, String nextNode, boolean runCommands) {

        new BukkitRunnable() {
            @Override
            public void run() {
                // Set the next node
                playerInfo.setCurrentNode(nextNode, conversation);

                // Remove blocking
                playerFlags.setInNode(false);

                if (runCommands) {
                    runCommands(CommandExecutionType.END, plugin, player.getName());
                }

                if (finishedConversation(conversation, player)) {
                    return;
                }

                // Run the next node if it is a time trigger
                if (conversation.getNode(nextNode).getTrigger().getType() == TriggerType.TIME) {
                    ConversationController.executeConversation(conversation, player.getName(), plugin);
                }
            }
        }.runTaskLater(plugin, (int) (20 * duration));
    }

    /**
     * Checks if the player has requirements.
     *
     * @param playerRequirements Player requirements
     * @return true if the node has requirements, false otherwise
     */
    public boolean checkRequirements(ArrayList<String> playerRequirements) {

        for (String requirement : this.requirements) {

            if (!playerRequirements.contains(requirement)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the player has requirements.
     *
     * @param conversation Conversation
     * @param node         Node
     * @param player       Player
     * @param playerName   Player name
     * @return true if the player has requirements, false otherwise
     */
    private boolean checkRequirements(Conversation conversation, Node node, Player player, String playerName) {

        if (!(node.checkRequirements(conversation.getPlayer(playerName).getRequirements()))) {
            player.sendMessage(Component.text(String.format("Missing requirements %s.", conversation.getMissingRequirements(node.getRequirements(), conversation.getPlayer(playerName).getRequirements()))).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, true));
            return false;
        }

        return true;
    }

    /**
     * Runs commands.
     *
     * @param execute Execution type
     * @param plugin  Plugin
     * @param player  Player
     */
    public void runCommands(CommandExecutionType execute, Plugin plugin, String player) {

        for (Command command : commands) {

            if (command.execute() == execute) {

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        // Execute the command as a console or as a player
                        if (command.sender() == CommandSenderType.CONSOLE) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.command().replace("%player%", player));
                        } else {
                            if (Bukkit.getPlayer(player) != null) {
                                Bukkit.getServer().dispatchCommand(Objects.requireNonNull(Bukkit.getPlayer(player)), command.command().replace("%player%", player));
                            }
                        }
                    }
                }.runTaskLater(plugin, (int) (20 * command.delay()));
            }
        }
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public NodeType getNodeType() {
        return type;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public String getNode() {
        return node;
    }

    public ArrayList<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<String> requirements) {
        this.requirements = requirements;
    }

    public String getNextNode() {
        return nextNode;
    }
}
