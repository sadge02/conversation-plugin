package minecraft.conversationPlugin.components.settings;

/**
 * Settings for the conversation.
 *
 * @param startMessage Send start message
 * @param endMessage   Send end message
 * @param startSound   Play start sound
 * @param endSound     Play end sound
 * @param blocking     Blocking other blocking nodes
 * @param citizens     Citizens plugin enabled
 * @param addOnJoin    Add conversation on join
 * @param startOnJoin  Start conversation on join
 */
public record Settings(boolean startMessage, boolean endMessage, boolean startSound, boolean endSound,
                       boolean blocking, boolean citizens, boolean addOnJoin, boolean startOnJoin) {
}
