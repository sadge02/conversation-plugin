package minecraft.conversationPlugin.components.location;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents a location in the game.
 */
public record ConversationLocation(int x, int y, int z) {

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }

    /**
     * Converts a ConversationLocation to a bukkit Location.
     *
     * @param location ConversationLocation to convert
     * @return bukkit location.
     */
    public static Location toBukkitLocation(ConversationLocation location, World world) {

        return new Location(world, location.x(), location.y(), location.z());
    }
}
