package minecraft.conversationPlugin.components.choice;

import minecraft.conversationPlugin.components.display.DisplaySettings;
import minecraft.conversationPlugin.components.display.types.DisplayTarget;
import minecraft.conversationPlugin.components.location.ConversationLocation;

import java.util.ArrayList;

/**
 * Represents a choice in a conversation.
 *
 * @param text            Text that is displayed to the player
 * @param node            Node that the choice leads to
 * @param location        Location that the player is teleported to
 * @param target          On what target is text displayed
 * @param displaySettings Settings for the text display
 * @param requirements    Requirements that need to be met to display the choice
 */
public record Choice(String text, String node, ConversationLocation location, DisplayTarget target,
                     DisplaySettings displaySettings, ArrayList<String> requirements) {

}
