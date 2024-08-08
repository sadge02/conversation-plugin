package minecraft.conversationPlugin.parsers.commands;

import minecraft.conversationPlugin.components.commads.Command;
import minecraft.conversationPlugin.components.commads.types.CommandExecutionType;
import minecraft.conversationPlugin.components.commads.types.CommandSenderType;
import minecraft.conversationPlugin.parsers.conversation.ConversationKeys;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Serialize and deserialize command objects.
 */
public class CommandParser {

    private static final Logger logger = Logger.getLogger(CommandParser.class.getName());

    /**
     * Deserialize command objects from JSON array.
     *
     * @param commandsArray JSON array to deserialize
     * @return deserialized command objects
     */
    public static ArrayList<Command> parseCommands(JSONArray commandsArray) {

        ArrayList<Command> commands = new ArrayList<>();

        for (int index = 0; index < commandsArray.length(); ++index) {

            try {
                commands.add(CommandParser.parseCommand(commandsArray.getJSONObject(index)));

            } catch (Exception exception) {
                logger.warning(String.format("Failed to parse command %d: %s", index, exception.getMessage()));
            }
        }

        return commands;
    }

    /**
     * Deserialize command object from JSON object.
     *
     * @param jsonObject JSON object to deserialize
     * @return deserialized command object
     */
    public static Command parseCommand(JSONObject jsonObject) {

        String text = jsonObject.getString(CommandKeys.COMMAND.getKey());
        float delay = ConversationKeys.parseFloat(jsonObject, CommandKeys.DELAY.getKey());
        CommandExecutionType commandType = CommandExecutionType.valueOf(jsonObject.getString(CommandKeys.EXECUTE.getKey()));
        CommandSenderType senderType = CommandSenderType.valueOf(jsonObject.getString(CommandKeys.SENDER.getKey()));

        return new Command(text, delay, commandType, senderType);
    }

    /**
     * Serialize command objects to JSON array.
     *
     * @param commands Command objects to serialize
     * @return serialized command objects
     */
    public static JSONArray saveCommands(ArrayList<Command> commands) {

        JSONArray commandsArray = new JSONArray();

        for (Command command : commands) {

            try {
                commandsArray.put(CommandParser.saveCommand(command));
            } catch (Exception exception) {
                logger.warning(String.format("Failed to save command %s: %s", command.command(), exception.getMessage()));
            }
        }

        return commandsArray;
    }

    /**
     * Serialize command object to JSON object.
     *
     * @param command Command object to serialize
     * @return serialized command object
     */
    public static JSONObject saveCommand(Command command) {

        JSONObject commandObject = new JSONObject();

        commandObject.put(CommandKeys.COMMAND.getKey(), command.command());
        commandObject.put(CommandKeys.DELAY.getKey(), command.delay());
        commandObject.put(CommandKeys.EXECUTE.getKey(), command.execute().toString());
        commandObject.put(CommandKeys.SENDER.getKey(), command.sender().toString());

        return commandObject;
    }
}
