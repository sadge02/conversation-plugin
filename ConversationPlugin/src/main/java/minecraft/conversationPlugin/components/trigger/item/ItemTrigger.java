package minecraft.conversationPlugin.components.trigger.item;

import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.components.trigger.item.types.ItemActionType;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

/**
 * Triggers node based on an interaction with an item.
 * <ul>
 *     <li>ItemTriggerType Type of item trigger.</li>
 *     <li>Name            Name of item.</li>
 *     <li>Amount          Amount of items.</li>
 *     <li>Consume         If true, item will be consumed after trigger.</li>
 * </ul>
 * <p>
 * Uses PlayerInteractEvent, PlayerPickupItemEvent, PlayerDropItemEvent.
 */
public class ItemTrigger extends Trigger {

    private final ItemActionType type;
    private final String name;
    private final int amount;
    private final int consume;

    public ItemTrigger(ItemActionType type, String name, int amount, int consume) {
        super(TriggerType.ITEM);
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.consume = consume;
    }

    /**
     * Get the number of specified items in the player's inventory.
     *
     * @param player Player to check items from
     * @param name   Name of the item to check
     * @return number of specified items in player's inventory
     */
    public static int getItemAmount(Player player, String name) {

        int items = 0;

        // Go through all items in the player's inventory
        for (ItemStack item : player.getInventory().getContents()) {

            if (checkItemName(item, name)) {
                items += item.getAmount();
            }
        }
        return items;
    }

    /**
     * Consumes the specified amount of items from the player's inventory.
     *
     * @param player   Player to consume items from
     * @param itemName Name of the item to consume
     * @param amount   Amount of items to consume
     */
    public static void consumeItems(Player player, String itemName, int amount) {

        if (getItemAmount(player, itemName) < amount) {
            return;
        }

        int removed = 0;

        // Go through all items in the player's inventory
        for (ItemStack item : player.getInventory().getContents()) {

            if (checkItemName(item, itemName)) {

                int toRemove = Math.min(amount - removed, item.getAmount());

                // Remove the specified amount from this item stack
                item.setAmount(item.getAmount() - toRemove);
                removed += toRemove;

                // Break if we have removed the desired amount
                if (removed >= amount) {
                    break;
                }
            }
        }
    }

    /**
     * Check if the item matches the specified item name.
     *
     * @param item Item to check
     * @param name Name of the item to check
     * @return true if the item matches the specified item name
     */
    public static boolean checkItemName(ItemStack item, String name) {

        if (item == null) {
            return false;
        }

        String itemName = item.getType().name();

        if (itemName.equalsIgnoreCase(name)) {
            return true;
        }

        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return false;
        }

        return itemMeta.hasDisplayName() && PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(itemMeta.displayName())).equals(name);
    }

    public ItemActionType getItemTriggerType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getConsume() {
        return consume;
    }
}
