package minecraft.conversationPlugin.commands.components;

/**
 * Enum for command argument lengths.
 */
public enum CommandLength {

    // One argument
    RELOAD(1),

    // Three arguments
    CONTINUE(3),
    INITIATE(3),
    RESTART(3),
    REMOVE(3),
    START(3),
    PRINT(3),
    RESET(3),
    CLEAR(3),
    GIVE(3),
    NEXT(3),
    RUN(3),
    ADD(3),

    // Four arguments
    PRINT_REQUIREMENTS(4),
    CLEAR_REQUIREMENTS(4),
    PRINT_VARIABLES(4),
    CLEAR_VARIABLES(4),
    JUMP(4),
    SET(4),

    // Five arguments
    REMOVE_REQUIREMENT(5),
    ADD_REQUIREMENT(5),
    REMOVE_VARIABLE(5),

    // Six arguments
    ADD_VARIABLE(6);

    private final int length;

    CommandLength(int length) {
        this.length = length;
    }

    public int length() {
        return length;
    }
}
