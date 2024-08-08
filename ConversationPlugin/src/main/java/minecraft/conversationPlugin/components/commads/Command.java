package minecraft.conversationPlugin.components.commads;

import minecraft.conversationPlugin.components.commads.types.CommandExecutionType;
import minecraft.conversationPlugin.components.commads.types.CommandSenderType;

/**
 * Class that holds node command information.
 * <p>
 * This command is executed before or after the node is triggered with a delay.
 *
 * @param command Command to be executed
 * @param delay   Delay after which the command is executed
 * @param execute Whether the command is executed at the start or end of the node
 * @param sender  Who executes the command
 */
public record Command(String command, float delay, CommandExecutionType execute, CommandSenderType sender) {
}
