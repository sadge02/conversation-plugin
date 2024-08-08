package minecraft.conversationPlugin.commands.types.debug;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * <p>
 * Print the current node, variables, or requirements of the player.
 * <br>
 * <p>
 * /conversation [player] [conversation] print
 * <ul>
 *     <li>Prints the current node of the player.</li>
 * </ul>
 * <p>
 * /conversation [player] [conversation] print variables
 * <ul>
 *     <li>Prints the variables of the player.</li>
 * </ul>
 * <p>
 * /conversation [player] [conversation] print requirements
 * <ul>
 *     <li>Prints the requirements of the player.</li>
 * </ul>
 */
public class PrintCommand {

    /**
     * Execute the PRINT command.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player
     * @param strings      Command arguments
     * @param logger       Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, Conversation conversation, String player, String[] strings, Logger logger) {

        if (strings.length > CommandLength.PRINT.length()) {

            if (strings[3].equals(CommandArguments.VARIABLES.value())) {
                return printVariables(sender, conversation, player, strings, logger);
            }

            if (strings[3].equals(CommandArguments.REQUIREMENTS.value())) {
                return printRequirements(sender, conversation, player, strings, logger);
            }

            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (strings.length != CommandLength.PRINT.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        return CommandController.notifyPlayer(sender, String.format("Current node ID: %s.", conversation.getPlayer(player).getCurrentNodeID()), logger);
    }

    /**
     * Print player's variables.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player to print variables of
     * @param strings      Command arguments
     * @param logger       Logger
     * @return true if the variables were printed successfully
     */
    private static boolean printVariables(CommandSender sender, Conversation conversation, String player, String[] strings, Logger logger) {

        if (strings.length != CommandLength.PRINT_VARIABLES.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        return CommandController.notifyPlayer(sender, String.format("Variables: %s.", processVariables(conversation.getPlayer(player).getVariables())), logger);
    }

    /**
     * Print player's requirements.
     *
     * @param sender       Command sender
     * @param conversation Conversation
     * @param player       Player to print requirements of
     * @param strings      Command arguments
     * @param logger       Logger
     * @return true if the requirements were printed successfully
     */
    private static boolean printRequirements(CommandSender sender, Conversation conversation, String player, String[] strings, Logger logger) {

        if (strings.length != CommandLength.PRINT_REQUIREMENTS.length()) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        if (!CommandController.checkPlayer(conversation, player, sender, logger)) {
            return false;
        }

        return CommandController.notifyPlayer(sender, String.format("Requirements: %s.", processRequirements(conversation.getPlayer(player).getRequirements())), logger);
    }

    /**
     * Process variables into a string.
     *
     * @param variables Variables to process
     * @return string of variables
     */
    private static String processVariables(HashMap<String, String> variables) {

        StringBuilder stringBuilder = new StringBuilder();

        for (String variable : variables.keySet()) {
            stringBuilder.append(variable).append(": ").append(variables.get(variable)).append(", ");
        }

        // Remove last semicolon and space
        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    /**
     * Process requirements into a string.
     *
     * @param requirements Requirements to process
     * @return string of requirements
     */
    private static String processRequirements(ArrayList<String> requirements) {

        StringBuilder stringBuilder = new StringBuilder();

        for (String requirement : requirements) {
            stringBuilder.append(requirement).append(", ");
        }

        // Remove last comma and space
        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }

        return stringBuilder.toString();
    }
}
