package minecraft.conversationPlugin.commands.autofill;

import minecraft.conversationPlugin.commands.components.CommandArguments;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Tab completer for the conversation plugin.
 * <p>
 * Autofill arguments for the conversation command.
 */
public class CommandAutofill implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        // Autofill arguments for the conversation command
        switch (strings.length) {
            case 1 -> {
                return CommandArguments.firstArgument(strings);
            }
            case 2 -> {
                return CommandArguments.secondArgument(strings);
            }
            case 3 -> {
                return CommandArguments.thirdArgument(strings);
            }
            case 4 -> {
                return CommandArguments.fourthArgument(strings);
            }
        }

        // Return an empty list if no arguments are found
        return new ArrayList<>();
    }
}
