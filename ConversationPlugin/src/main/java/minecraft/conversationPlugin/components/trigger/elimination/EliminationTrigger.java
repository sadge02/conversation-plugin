package minecraft.conversationPlugin.components.trigger.elimination;

import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.components.trigger.elimination.types.EliminationType;

import java.util.HashMap;

/**
 * Triggers node if player eliminates a certain quantity of mobs.
 * <ul>
 *     <li>Eliminate Type of mob.
 *     <li>Quantity  Number of mobs to eliminate.
 * </ul>
 * <p>
 * Uses EntityDeathEvent.
 */
public class EliminationTrigger extends Trigger {

    private final EliminationType eliminate;
    private final int quantity;

    private final HashMap<String, Integer> quantityLeft;

    public EliminationTrigger(EliminationType eliminate, int quantity) {
        super(TriggerType.ELIMINATE);
        this.eliminate = eliminate;
        this.quantity = quantity;
        this.quantityLeft = new HashMap<>();
    }

    public EliminationType getEliminateTriggerType() {
        return eliminate;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityLeft(String player) {
        return quantityLeft.getOrDefault(player, quantity);
    }

    public void decreaseQuantityLeft(String player) {
        if (quantityLeft.get(player) > 0) {
            quantityLeft.put(player, quantityLeft.get(player) - 1);
        }
    }
}
