package minecraft.conversationPlugin.components.trigger.types;

/**
 * This enum contains the different types of triggers.
 *
 * <ul>
 *     <li>LOCATION: Trigger based on location</li>
 *     <li>INTERACT: Trigger based on interaction.</li>
 *     <li>ENTITY: Trigger based on entity.</li>
 *     <li>BLOCK: Trigger based on block.</li>
 *     <li>ELIMINATE: Trigger based on elimination.</li>
 *     <li>ITEM: Trigger based on item.</li>
 *     <li>TIME: Trigger based on time.</li>
 *     <li>COMMAND: Trigger based on command.</li>
 * </ul>
 */
public enum TriggerType {

    // Automatic
    LOCATION,
    INTERACT,
    ENTITY,
    BLOCK,
    ELIMINATE,
    ITEM,
    TIME,

    // Manual
    COMMAND,
}
