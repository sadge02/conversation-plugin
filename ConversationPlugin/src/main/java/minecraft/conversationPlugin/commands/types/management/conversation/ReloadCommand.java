package minecraft.conversationPlugin.commands.types.management.conversation;

import minecraft.conversationPlugin.ConversationPlugin;
import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import minecraft.conversationPlugin.components.player.PlayerFlags;
import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.parsers.conversation.ConversationParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * <p>
 * Reloads conversations for all players.
 * <br>
 * <p>
 * /conversation reload
 * <ul>
 *     <li>Reloads conversations.</li>
 *     <li>Players can't be in an active node.</li>
 * </ul>
 */
public class ReloadCommand {

    /**
     * Execute the RELOAD command.
     *
     * @param sender       Command sender
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, String[] args, Logger logger) {

        if (args.length != CommandLength.RELOAD.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        File conversationsDirectory = new File(Paths.get(System.getProperty("user.dir"), "plugins", "ConversationPlugin", "conversations").toString());

        ConversationParser.saveConversations(conversationsDirectory);

        ConversationPlugin.conversations.clear();

        for (String player : ConversationPlugin.players.keySet()) {
            PlayerFlags playerFlags = ConversationPlugin.players.get(player);

            if (playerFlags.getInNode()) {
                return CommandController.warnPlayer(sender, String.format("%s is currently in a conversation.", player), logger);
            }
        }

        for (String player : ConversationPlugin.players.keySet()) {
            ConversationPlugin.players.get(player).reset();
            System.out.println(ConversationPlugin.players.get(player).getInNode());
        }

        ConversationParser.loadConversations(conversationsDirectory);

        return CommandController.notifyPlayer(sender, "Conversations reset.", logger);
    }
}
