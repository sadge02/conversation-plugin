package minecraft.conversationPlugin.commands.components;

import minecraft.conversationPlugin.ConversationPlugin;
import minecraft.conversationPlugin.components.conversation.Conversation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the possible arguments for the conversation command.
 * <p>
 * These arguments are used to autofill the command arguments when the player is typing the command in the chat.
 */
public enum CommandArguments {

    // Execute the node
    START("start"),
    RESTART("restart"),
    RUN("run"),
    CONTINUE("continue"),
    JUMP("jump"),
    RELOAD("reload"),

    // Do not execute the node
    RESET("reset"),
    INITIATE("initiate"),
    NEXT("next"),
    SET("set"),

    // Manage the conversation
    REMOVE("remove"),
    CLEAR("clear"),
    ADD("add"),

    // Debug the conversation
    PRINT("print"),

    // Other arguments
    CONVERSATION("conversation"),
    REQUIREMENT("requirement"),
    VARIABLE("variable"),
    REQUIREMENTS("requirements"),
    VARIABLES("variables"),
    GIVE("give"),
    TOOL("tool");

    private static final ArrayList<String> PRINT_AND_CLEAR_ARGUMENTS = new ArrayList<>(List.of(REQUIREMENTS.value(), VARIABLES.value()));
    private static final ArrayList<String> ADD_AND_REMOVE_ARGUMENTS = new ArrayList<>(List.of(REQUIREMENT.value(), VARIABLE.value()));
    private static final ArrayList<String> ARGUMENTS = new ArrayList<>(List.of(START.value(), INITIATE.value(), RESTART.value(), RUN.value(), NEXT.value(), REMOVE.value(), SET.value(), PRINT.value(), CLEAR.value(), ADD.value(), CONTINUE.value(), RESET.value(), JUMP.value()));

    private final String value;

    CommandArguments(String key) {
        this.value = key;
    }

    /**
     * Get the first argument for the conversation command.
     *
     * @param arguments Arguments of the command
     * @return the list of all possible options for the first argument
     */
    public static ArrayList<String> firstArgument(String[] arguments) {

        // Return list of player names filtered by the input
        return Stream.concat(
                        Stream.of(GIVE.value(), RELOAD.value()),
                        Bukkit.getOnlinePlayers().stream().map(Player::getName)
                )
                .filter(name -> name.toLowerCase().startsWith(arguments[0].toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get the second argument for the conversation command.
     *
     * @param arguments Arguments of the command
     * @return the list of all possible options for the second argument
     */
    public static ArrayList<String> secondArgument(String[] arguments) {

        // Return list of arguments for the GIVE argument
        if (arguments[0].equalsIgnoreCase(GIVE.value())) {
            return new ArrayList<>(List.of(CONVERSATION.value()));
        }

        // Return empty list for the RELOAD command
        if (arguments[0].equalsIgnoreCase(RELOAD.value())) {
            return new ArrayList<>();
        }

        // Return all available conversations filtered by the input
        return ConversationPlugin.conversations.stream()
                .map(Conversation::name)
                .filter(conversation -> conversation.toLowerCase().startsWith(arguments[1].toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get the third argument for the conversation command.
     *
     * @param arguments Arguments of the command
     * @return the list of all possible options for the third argument
     */
    public static ArrayList<String> thirdArgument(String[] arguments) {

        // Return list of arguments for the GIVE CONVERSATION arguments
        if (arguments[0].equalsIgnoreCase(GIVE.value()) && arguments[1].equalsIgnoreCase(CONVERSATION.value())) {
            return new ArrayList<>(List.of(TOOL.value()));
        }

        // Return empty list for the RELOAD command
        if (arguments[0].equalsIgnoreCase(RELOAD.value())) {
            return new ArrayList<>();
        }

        // Return all available commands filtered by the input
        return ARGUMENTS.stream()
                .filter(commandName -> commandName.toLowerCase().startsWith(arguments[2].toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get the fourth argument for the conversation command.
     *
     * @param arguments Arguments of the command
     * @return the list of all possible options for the fourth argument
     */
    public static ArrayList<String> fourthArgument(String[] arguments) {

        // Return empty list for the GIVE CONVERSATION TOOL arguments
        if (arguments[0].equalsIgnoreCase(GIVE.value()) && arguments[1].equalsIgnoreCase(CONVERSATION.value())) {
            return new ArrayList<>();
        }

        // Return empty list for the RELOAD command
        if (arguments[0].equalsIgnoreCase(RELOAD.value())) {
            return new ArrayList<>();
        }

        // Return list of arguments for the PRINT and CLEAR arguments
        if (arguments[2].equalsIgnoreCase(PRINT.value()) || arguments[2].equalsIgnoreCase(CLEAR.value())) {
            return PRINT_AND_CLEAR_ARGUMENTS;
        }

        // Return list of arguments for the ADD and REMOVE arguments
        if (arguments[2].equalsIgnoreCase(ADD.value()) || arguments[2].equalsIgnoreCase(REMOVE.value())) {
            return ADD_AND_REMOVE_ARGUMENTS;
        }

        // Return list of all nodes filtered by the input
        if (arguments[2].equalsIgnoreCase(SET.value()) || arguments[2].equalsIgnoreCase(JUMP.value())) {
            return Stream.concat(
                            Stream.of(NEXT.value()), // Add NEXT argument
                            ConversationPlugin.conversations.stream()
                                    .filter(conversation -> conversation.name().equalsIgnoreCase(arguments[1]))
                                    .flatMap(conversation -> conversation.nodes().keySet().stream()
                                            .map(key -> conversation.getNode(key).getNode())
                                            .filter(nodeId -> nodeId.toLowerCase().startsWith(arguments[3].toLowerCase())))
                    )
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new ArrayList<>();
    }

    public String value() {
        return value;
    }
}
