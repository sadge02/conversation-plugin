package minecraft.conversationPlugin.parsers.players;

import minecraft.conversationPlugin.components.player.PlayerInfo;
import minecraft.conversationPlugin.parsers.requirements.RequirementsParser;
import minecraft.conversationPlugin.parsers.variables.VariablesParser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Serialize and deserialize player.
 */
public class PlayerParser {

    private static final Logger logger = Logger.getLogger(PlayerParser.class.getName());

    /**
     * Serialize JSON object to players object.
     *
     * @param playersObject JSON object to serialize
     * @return serialized players object
     */
    public static HashMap<String, PlayerInfo> parsePlayers(JSONObject playersObject) {

        HashMap<String, PlayerInfo> players = new HashMap<>();

        // Go through all players
        for (String name : playersObject.keySet()) {
            try {
                JSONObject playerObject = playersObject.getJSONObject(name);

                String currentNode = playerObject.getString(PlayerKeys.CURRENT_NODE.getKey());
                ArrayList<String> requirements = RequirementsParser.parseRequirements(playerObject.getJSONArray(PlayerKeys.REQUIREMENTS.getKey()));
                HashMap<String, String> variables = VariablesParser.parseVariables(playerObject.getJSONObject(PlayerKeys.VARIABLES.getKey()));

                players.put(name, new PlayerInfo(name, requirements, variables, currentNode));

            } catch (Exception exception) {
                logger.warning(String.format("Failed to parse player %s: %s", name, exception.getMessage()));
            }
        }

        return players;
    }

    /**
     * Deserialize players object to JSON object.
     *
     * @param players Players object to deserialize
     * @return deserialized players object
     */
    public static JSONObject savePlayers(HashMap<String, PlayerInfo> players) {

        JSONObject playersObject = new JSONObject();

        for (String name : players.keySet()) {
            try {
                PlayerInfo player = players.get(name);
                JSONObject playerObject = new JSONObject();

                playerObject.put(PlayerKeys.CURRENT_NODE.getKey(), player.getCurrentNodeID());
                playerObject.put(PlayerKeys.REQUIREMENTS.getKey(), RequirementsParser.saveRequirements(player.getRequirements()));
                playerObject.put(PlayerKeys.VARIABLES.getKey(), VariablesParser.saveVariables(player.getVariables()));

                playersObject.put(name, playerObject);

            } catch (Exception exception) {
                logger.warning(String.format("Failed to save player %s: %s", name, exception.getMessage()));
            }
        }

        return playersObject;
    }
}
