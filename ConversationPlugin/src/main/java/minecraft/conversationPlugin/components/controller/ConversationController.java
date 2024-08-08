package minecraft.conversationPlugin.components.controller;

import minecraft.conversationPlugin.ConversationPlugin;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.nodes.Node;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.components.trigger.time.TimeTrigger;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class contains methods to run a conversation.
 */
public class ConversationController {

    /**
     * Executes the conversation.
     *
     * @param conversation Conversation
     * @param player       Name of the player
     * @param plugin       Plugin that the conversation is running in
     */
    public static void executeConversation(Conversation conversation, String player, Plugin plugin) {

        Node node = conversation.nodes().get(conversation.getPlayer(player).getCurrentNodeID());

        if (node != null) {
            if (ConversationController.canExecuteConversation(conversation, player)) {
                if (node.getTrigger().getType() == TriggerType.TIME) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            node.executeNode(plugin, conversation, player);
                        }
                    }.runTaskLater(plugin, (int) (20 * ((TimeTrigger) node.getTrigger()).getDelay()));
                } else {
                    node.executeNode(plugin, conversation, player);
                }
            }
        }
    }

    /**
     * Checks if the conversation can be executed.
     *
     * @param conversation Conversation
     * @param player       Name of the player
     * @return true if the conversation can be executed, false otherwise
     */
    public static boolean canExecuteConversation(Conversation conversation, String player) {

        // Check if the player is in the conversation
        if (!ConversationPlugin.players.containsKey(player)) {
            return false;
        }

        // Run the conversation if the conversation is not blocking
        if (!conversation.settings().blocking()) {
            return true;
        }

        // Run the conversation if the player is not in a node, input node, choice node, or cutscene
        return !ConversationPlugin.players.get(player).getInNode() && !ConversationPlugin.players.get(player).getInChoiceNode() && !ConversationPlugin.players.get(player).getInInputNode() && !ConversationPlugin.players.get(player).getInCutscene();
    }
}
