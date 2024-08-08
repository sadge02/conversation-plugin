package minecraft.conversationPlugin.parsers.trigger.type.item;

import minecraft.conversationPlugin.components.trigger.item.ItemTrigger;
import minecraft.conversationPlugin.components.trigger.item.types.ItemActionType;
import org.json.JSONObject;

/**
 * Serialize and deserialize ItemTrigger object.
 */
public class ItemTriggerParser {

    /**
     * Serialize JSONObject to ItemTrigger object.
     *
     * @param itemTriggerObject JSONObject to serialize
     * @return serialized ItemTrigger object
     */
    public static ItemTrigger parseItemTrigger(JSONObject itemTriggerObject) {

        ItemActionType action = ItemActionType.valueOf(itemTriggerObject.getString(ItemTriggerKeys.ACTION.getKey()));
        String name = itemTriggerObject.getString(ItemTriggerKeys.NAME.getKey());
        int amount = itemTriggerObject.getInt(ItemTriggerKeys.AMOUNT.getKey());
        int consume = itemTriggerObject.getInt(ItemTriggerKeys.CONSUME.getKey());

        return new ItemTrigger(action, name, amount, consume);
    }

    /**
     * Deserialize ItemTrigger object to JSONObject.
     *
     * @param nodeSettings JSONObject to deserialize
     * @param itemTrigger  ItemTrigger object to deserialize
     */
    public static void saveItemTrigger(JSONObject nodeSettings, ItemTrigger itemTrigger) {

        nodeSettings.put(ItemTriggerKeys.ACTION.getKey(), itemTrigger.getItemTriggerType().name());
        nodeSettings.put(ItemTriggerKeys.NAME.getKey(), itemTrigger.getName());
        nodeSettings.put(ItemTriggerKeys.AMOUNT.getKey(), itemTrigger.getAmount());
        nodeSettings.put(ItemTriggerKeys.CONSUME.getKey(), itemTrigger.getConsume());
    }
}
