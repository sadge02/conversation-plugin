package minecraft.conversationPlugin.commands.types.management.player;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

/**
 * <p>
 * Add requirement or variable to the player in the conversation.
 * <br>
 * <p>
 * /conversation [player] [conversation] add requirement [requirement]
 * <ul>
 *     <li>Adds requirement to the player in the conversation.</li>
 * </ul>
 * <p>
 * /conversation [player] [conversation] add variable [variable] [value]
 * <ul>
 *     <li>Adds variable to the player in the conversation.</li>
 * </ul>
 */
public class AddCommand {

    /**
     * Execute the ADD command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] args, Logger logger) {

        if (args.length > CommandLength.ADD.length()) {

            if (args[3].equals(CommandArguments.VARIABLE.value())) {
                return addVariable(sender, conversation, player, args, logger);
            }

            if (args[3].equals(CommandArguments.REQUIREMENT.value())) {
                return addRequirements(sender, conversation, player, args, logger);
            }
        }

        return CommandController.warnPlayer(sender, "Invalid command.", logger);
    }

    /**
     * Add variable to the player in the conversation.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the variable was added successfully
     */
    private static boolean addVariable(CommandSender sender, Conversation conversation, String player, String[] args, Logger logger) {

        if (args.length != CommandLength.ADD_VARIABLE.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        try {
            conversation.getPlayer(player).addVariable(args[4], args[5]);
        } catch (Exception exception) {
            return CommandController.warnPlayer(sender, String.format("Error adding variable %s in conversation %s for player %s.", args[4], conversation.name(), player), logger);
        }

        return CommandController.notifyPlayer(sender, String.format("Variable %s with value %s in conversation %s added for player %s.", args[4], args[5], conversation.name(), player), logger);
    }

    /**
     * Add requirement to the player in the conversation.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param args         Command arguments
     * @param logger       Logger
     * @return true if the requirement was added successfully
     */
    private static boolean addRequirements(CommandSender sender, Conversation conversation, String player, String[] args, Logger logger) {

        if (args.length != CommandLength.ADD_REQUIREMENT.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        try {
            conversation.getPlayer(player).addRequirement(args[4]);
        } catch (Exception exception) {
            return CommandController.warnPlayer(sender, String.format("Error adding requirement %s in conversation %s for player %s.", args[4], conversation.name(), player), logger);
        }

        return CommandController.notifyPlayer(sender, String.format("Requirements %s in conversation %s added for player %s.", args[4], conversation.name(), player), logger);
    }
}
