package minecraft.conversationPlugin.commands.types.debug;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import minecraft.conversationPlugin.commands.components.CommandLength;
import minecraft.conversationPlugin.commands.controller.CommandController;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.logging.Logger;

/**
 * <p>
 * Tool for getting the entity UUID or the block location.
 * <br>
 * <p>
 * /conversation give tool
 * <ul>
 *     <li>Gives the player a conversation tool.</li>
 * </ul>
 */
public class GiveCommand {

    /**
     * Execute the GIVE command.
     *
     * @param sender Command sender
     * @param args   Command arguments
     * @param logger Logger
     * @return true if the command was executed successfully
     */
    public static boolean execute(CommandSender sender, String[] args, Logger logger) {

        if (args.length != CommandLength.GIVE.length() || !args[1].equals(CommandArguments.CONVERSATION.value()) || !args[2].equals(CommandArguments.TOOL.value())) {
            return CommandController.warnPlayer(sender, "Invalid command.", logger);
        }

        // You must be a player to obtain the conversation tool.
        if (sender instanceof Player player) {

            ItemStack conversationStick = new ItemStack(Material.STICK);
            ItemMeta conversationStickMeta = conversationStick.getItemMeta();

            if (conversationStickMeta != null) {
                conversationStickMeta.displayName(Component.text("conversation tool"));
                conversationStick.setItemMeta(conversationStickMeta);
            }

            player.getInventory().addItem(conversationStick);

            return CommandController.notifyPlayer(sender, "Conversation Stick given.", logger);
        }

        return CommandController.warnPlayer(sender, "You must be a player to use this command and receive the tool.", logger);
    }
}
