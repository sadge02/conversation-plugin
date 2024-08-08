package minecraft.conversationPlugin.components.trigger.block;

import minecraft.conversationPlugin.components.location.ConversationLocation;
import minecraft.conversationPlugin.components.trigger.Trigger;
import minecraft.conversationPlugin.components.trigger.types.TriggerType;
import minecraft.conversationPlugin.components.trigger.block.types.BlockActionType;

/**
 * Triggers node if player interacts with a block.
 * <ul>
 *     <li>BlockTriggerType Type of block trigger.</li>
 *     <li>Location         Location of block.</li>
 *     <li>Remove           If true, block will be removed after trigger.</li>
 * </ul>
 * <p>
 * Uses PlayerInteractEvent, PlayerBreakBlockEvent, PlayerPlaceBlockEvent.
 */
public class BlockTrigger extends Trigger {

    private final BlockActionType type;
    private final ConversationLocation conversationLocation;
    private final boolean remove;

    public BlockTrigger(BlockActionType type, ConversationLocation conversationLocation, boolean remove) {
        super(TriggerType.BLOCK);
        this.type = type;
        this.conversationLocation = conversationLocation;
        this.remove = remove;
    }

    public BlockActionType getBlockTriggerType() {
        return type;
    }

    public ConversationLocation getBlockLocation() {
        return conversationLocation;
    }

    public boolean getRemove() {
        return remove;
    }
}
