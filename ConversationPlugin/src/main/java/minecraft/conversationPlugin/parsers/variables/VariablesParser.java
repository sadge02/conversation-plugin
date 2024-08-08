package minecraft.conversationPlugin.parsers.variables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Serialize and deserialize variables.
 */
public class VariablesParser {

    private static final Logger logger = Logger.getLogger(VariablesParser.class.getName());

    /**
     * Serialize JSON object to variables map.
     *
     * @param jsonObject JSON object to serialize
     * @return map of variables
     */
    public static HashMap<String, String> parseVariables(JSONObject jsonObject) {

        HashMap<String, String> variables = new HashMap<>();

        for (String variable : jsonObject.keySet()) {
            try {
                variables.put(variable, jsonObject.getString(variable));
            } catch (Exception exception) {
                logger.warning(String.format("Error parsing variable %s: %s", variable, exception.getMessage()));
            }
        }

        return variables;
    }

    /**
     * Deserialize variables map to JSON object.
     *
     * @param variables Map of variables to deserialize
     * @return deserialized JSON object
     */
    public static JSONObject saveVariables(HashMap<String, String> variables) {

        JSONObject variablesObject = new JSONObject();

        for (String variable : variables.keySet()) {
            try {
                variablesObject.put(variable, variables.get(variable));
            } catch (Exception exception) {
                logger.warning(String.format("Error saving variable %s: %s", variable, exception.getMessage()));
            }
        }

        return variablesObject;
    }
}
