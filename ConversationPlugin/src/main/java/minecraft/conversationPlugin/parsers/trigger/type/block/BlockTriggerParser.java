package minecraft.conversationPlugin.parsers.trigger.type.block;

import minecraft.conversationPlugin.components.location.ConversationLocation;
import minecraft.conversationPlugin.components.trigger.block.BlockTrigger;
import minecraft.conversationPlugin.components.trigger.block.types.BlockActionType;
import minecraft.conversationPlugin.parsers.location.LocationParser;
import org.json.JSONObject;

/**
 * Serialize and deserialize BlockTrigger object.
 */
public class BlockTriggerParser {

    /**
     * Serialize JSONObject to BlockTrigger object.
     *
     * @param blockTriggerObject JSONObject to serialize
     * @return serialized BlockTrigger object
     */
    public static BlockTrigger parseBlockTrigger(JSONObject blockTriggerObject) {

        BlockActionType action = BlockActionType.valueOf(blockTriggerObject.getString(BlockTriggerKeys.ACTION.getKey()));
        ConversationLocation conversationLocation = LocationParser.parseLocation(blockTriggerObject.getJSONObject(BlockTriggerKeys.LOCATION.getKey()));
        boolean remove = blockTriggerObject.getBoolean(BlockTriggerKeys.REMOVE.getKey());

        return new BlockTrigger(action, conversationLocation, remove);
    }

    /**
     * Deserialize BlockTrigger object to JSONObject.
     *
     * @param nodeSettings JSONObject to deserialize
     * @param blockTrigger BlockTrigger object to deserialize
     */
    public static void saveBlockTrigger(JSONObject nodeSettings, BlockTrigger blockTrigger) {

        nodeSettings.put(BlockTriggerKeys.ACTION.getKey(), blockTrigger.getBlockTriggerType().toString());
        nodeSettings.put(BlockTriggerKeys.LOCATION.getKey(), LocationParser.saveLocation(blockTrigger.getBlockLocation()));
        nodeSettings.put(BlockTriggerKeys.REMOVE.getKey(), blockTrigger.getRemove());
    }
}
