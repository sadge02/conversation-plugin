package minecraft.conversationPlugin.commands;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.commands.types.debug.GiveCommand;
import minecraft.conversationPlugin.commands.types.debug.PrintCommand;
import minecraft.conversationPlugin.commands.types.flow.*;
import minecraft.conversationPlugin.commands.types.management.conversation.*;
import minecraft.conversationPlugin.commands.types.management.player.AddCommand;
import minecraft.conversationPlugin.commands.types.management.player.ClearCommand;
import minecraft.conversationPlugin.commands.types.management.player.RemoveCommand;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * Command executor for the /conversation command.
 */
public class ConversationCommand implements CommandExecutor {

    private final Plugin plugin;
    private final Logger logger;

    public ConversationCommand(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        // Requirements check
        if (commandSender instanceof Player player) {
            if (!player.hasPermission("conversation.use")) {
                return CommandController.warnPlayer(commandSender, "You do not have permission to use this command.", logger);
            }
        }

        // Command length check
        if (strings.length < 1 || strings.length > 6) {
            return false;
        }

        // Handle GIVE command separately
        if (strings[0].equals(CommandArguments.GIVE.value())) {
            return GiveCommand.execute(commandSender, strings, logger);
        }

        // Handle RELOAD command separately
        if (strings[0].equals(CommandArguments.RELOAD.value())) {
            return ReloadCommand.execute(commandSender, strings, logger);
        }

        // Valid conversation check
        if (!Conversation.isConversationValid(strings[1])) {
            return CommandController.warnPlayer(commandSender, "Invalid conversation name.", logger);
        }

        return executeCommand(commandSender, strings[0], strings[1], strings[2], strings);
    }

    /**
     * Execute the command
     *
     * @param sender       Command sender
     * @param player       Player
     * @param conversation Conversation
     * @param args         Command arguments
     * @return true if the command was executed successfully
     */
    private boolean executeCommand(CommandSender sender, String player, String conversation, String action, String[] args) {

        if (CommandArguments.INITIATE.value().equals(action)) {
            return InitiateCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger);
        } else if (CommandArguments.START.value().equals(action)) {
            return StartCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger, plugin);
        } else if (CommandArguments.RESTART.value().equals(action)) {
            return RestartCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger);
        } else if (CommandArguments.RUN.value().equals(action)) {
            return RunCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger, plugin);
        } else if (CommandArguments.NEXT.value().equals(action)) {
            return NextCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger);
        } else if (CommandArguments.REMOVE.value().equals(action)) {
            return RemoveCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger);
        } else if (CommandArguments.PRINT.value().equals(action)) {
            return PrintCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger);
        } else if (CommandArguments.SET.value().equals(action)) {
            return SetCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger);
        } else if (CommandArguments.CLEAR.value().equals(action)) {
            return ClearCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger);
        } else if (CommandArguments.ADD.value().equals(action)) {
            return AddCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger);
        } else if (CommandArguments.CONTINUE.value().equals(action)) {
            return ContinueCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger, plugin);
        } else if (CommandArguments.RESET.value().equals(action)) {
            return ResetCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger);
        } else if (CommandArguments.JUMP.value().equals(action)) {
            return JumpCommand.execute(sender, Conversation.getConversation(conversation), player, args, logger, plugin);
        }
        return CommandController.warnPlayer(sender, "Invalid command.", logger);
    }
}
