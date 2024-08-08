package minecraft.conversationPlugin.commands.types.management.conversation;

import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

/**
 * <p>
 * Add player to the conversation without executing the node.
 * <br>
 * <p>
 * /conversation [player] [conversation] initiate
 * <ul>
 *     <li>Adds player to the conversation.</li>
 *     <li>Does not execute the node.</li>
 * </ul>
 */
public class InitiateCommand {

    /**
     * Execute the INITIATE command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param name         Player
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String name, String[] args, Logger logger) {

        if (args.length != CommandLength.INITIATE.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        Player player = Bukkit.getPlayer(name);

        if (player == null) {
            return false;
        }

        if (!CommandController.addPlayer(conversation, name, sender, logger)) {
            return false;
        }

        // Show start message to the specified player
        if (conversation.settings().startMessage()) {
            player.sendMessage(Component.text(String.format("%s has been initiated.", conversation.name())).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, true));
        }

        // Play start sound for the specified player
        if (conversation.settings().startSound()) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }

        return CommandController.notifyPlayer(sender, String.format("Conversation %s initiated for player %s.", conversation.name(), name), logger);
    }
}
