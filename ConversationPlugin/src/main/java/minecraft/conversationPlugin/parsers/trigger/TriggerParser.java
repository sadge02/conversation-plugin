package minecraft.conversationPlugin.parsers.trigger;

import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.block.BlockTrigger;
import minecraft.conversationPlugin.components.trigger.command.CommandTrigger;
import minecraft.conversationPlugin.components.trigger.elimination.EliminationTrigger;
import minecraft.conversationPlugin.components.trigger.entity.EntityTrigger;
import minecraft.conversationPlugin.components.trigger.interact.InteractTrigger;
import minecraft.conversationPlugin.components.trigger.item.ItemTrigger;
import minecraft.conversationPlugin.components.trigger.location.LocationTrigger;
import minecraft.conversationPlugin.components.trigger.time.TimeTrigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.parsers.trigger.type.block.BlockTriggerParser;
import minecraft.conversationPlugin.parsers.trigger.type.eliminate.EliminateTriggerParser;
import minecraft.conversationPlugin.parsers.trigger.type.entity.EntityTriggerParser;
import minecraft.conversationPlugin.parsers.trigger.type.item.ItemTriggerParser;
import minecraft.conversationPlugin.parsers.trigger.type.location.LocationTriggerParser;
import minecraft.conversationPlugin.parsers.trigger.type.time.TimeTriggerParser;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Serialize and deserialize triggers.
 */
public class TriggerParser {

    private static final Logger logger = Logger.getLogger(TriggerParser.class.getName());

    /**
     * Deserialize trigger object from JSON object.
     *
     * @param triggerObject JSON object to deserialize
     * @return deserialized trigger object
     */
    public static Trigger parseTrigger(JSONObject triggerObject) {

        try {
            TriggerType triggerInfo = TriggerType.valueOf(triggerObject.getString(TriggerKeys.TRIGGER_TYPE.getKey()));

            return switch (triggerInfo) {
                case ENTITY -> EntityTriggerParser.parseEntityTrigger(triggerObject);
                case BLOCK -> BlockTriggerParser.parseBlockTrigger(triggerObject);
                case LOCATION -> LocationTriggerParser.parseLocationTrigger(triggerObject);
                case INTERACT -> new InteractTrigger();
                case COMMAND -> new CommandTrigger();
                case ITEM -> ItemTriggerParser.parseItemTrigger(triggerObject);
                case TIME -> TimeTriggerParser.parseTimeTrigger(triggerObject);
                case ELIMINATE -> EliminateTriggerParser.parseKillTrigger(triggerObject);
            };
        } catch (Exception exception) {
            logger.warning(String.format("Failed to parse trigger: %s", exception.getMessage()));
        }

        return null;
    }

    /**
     * Serialize trigger object to JSON object.
     *
     * @param trigger Trigger object to serialize
     * @return serialized JSON object
     */
    public static JSONObject saveTrigger(Trigger trigger) {

        JSONObject triggerObject = new JSONObject();

        try {
            triggerObject.put(TriggerKeys.TRIGGER_TYPE.getKey(), trigger.getType().toString());

            switch (trigger.getType()) {
                case ENTITY:
                    EntityTriggerParser.saveEntityTrigger(triggerObject, (EntityTrigger) trigger);
                    break;
                case BLOCK:
                    BlockTriggerParser.saveBlockTrigger(triggerObject, (BlockTrigger) trigger);
                    break;
                case LOCATION:
                    LocationTriggerParser.saveLocationTrigger(triggerObject, (LocationTrigger) trigger);
                    break;
                case ITEM:
                    ItemTriggerParser.saveItemTrigger(triggerObject, (ItemTrigger) trigger);
                    break;
                case TIME:
                    TimeTriggerParser.saveTimeTrigger(triggerObject, (TimeTrigger) trigger);
                    break;
                case ELIMINATE:
                    EliminateTriggerParser.saveEliminateTrigger(triggerObject, (EliminationTrigger) trigger);
                    break;
            }
        } catch (Exception exception) {
            logger.warning(String.format("Failed to save trigger: %s", exception.getMessage()));
        }

        return triggerObject;
    }
}
