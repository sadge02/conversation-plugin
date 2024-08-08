package minecraft.conversationPlugin.parsers.requirements;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Serialize and deserialize requirements.
 */
public class RequirementsParser {

    private static final Logger logger = Logger.getLogger(RequirementsParser.class.getName());

    /**
     * Serialize JSON object to requirements list.
     *
     * @param requirementsArray JSON array to serialize
     * @return list of requirements
     */
    public static ArrayList<String> parseRequirements(JSONArray requirementsArray) {

        ArrayList<String> requirements = new ArrayList<>();

        for (int index = 0; index < requirementsArray.length(); ++index) {
            try {
                requirements.add(requirementsArray.getString(index));
            } catch (Exception exception) {
                logger.warning(String.format("Error parsing requirements %d: %s", index, exception.getMessage()));
            }
        }

        return requirements;
    }

    /**
     * Deserialize requirements list to JSON object.
     *
     * @param requirements List of requirements to deserialize
     * @return deserialized JSON object
     */
    public static JSONArray saveRequirements(ArrayList<String> requirements) {

        JSONArray requirementsArray = new JSONArray();

        for (String requirement : requirements) {
            try {
                requirementsArray.put(requirement);
            } catch (Exception exception) {
                logger.warning(String.format("Error saving requirements %s: %s", requirement, exception.getMessage()));
            }
        }

        return requirementsArray;
    }
}
