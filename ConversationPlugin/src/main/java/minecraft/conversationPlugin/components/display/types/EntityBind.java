package minecraft.conversationPlugin.components.display.types;

/**
 * Text bind to entity and text elevation.
 * <ul>
 *     <li>NONE: Do not bind text to an entity.</li>
 *     <li>LOW_ELEVATION: Bind the text and elevate it. Uses silverfish entity.</li>
 *     <li>MEDIUM_ELEVATION: Bind the text and elevate it. Uses armadillo entity.</li>
 *     <li>HIGH_ELEVATION: Bind the text and elevate it. Uses armor stand entity.</li>
 * </ul>
 */
public enum EntityBind {

    NONE,
    LOW_ELEVATION,
    MEDIUM_ELEVATION,
    HIGH_ELEVATION,
}
