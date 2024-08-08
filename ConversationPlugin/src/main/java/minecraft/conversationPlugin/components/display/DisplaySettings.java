package minecraft.conversationPlugin.components.display;

import minecraft.conversationPlugin.components.display.types.EntityBind;
import org.bukkit.entity.TextDisplay;

/**
 * Display settings for text.
 *
 * @param shadow     Whether the text has a shadow
 * @param background Color of the background
 * @param alignment  Alignment of the text
 * @param billboard  Billboard of the text
 * @param lineWidth  Width of the line
 * @param seeThrough Whether the text is see through
 * @param visible    Whether the text is visible for other players
 * @param glowing    Whether the text is glowing
 * @param scale      Scale of the text
 * @param bind       Bind to entity and text elevation
 * @param elevation  Elevation of the text without binding
 */
public record DisplaySettings(boolean shadow, int background, TextDisplay.TextAlignment alignment,
                              TextDisplay.Billboard billboard, int lineWidth, boolean seeThrough, boolean visible,
                              boolean glowing, float scale, EntityBind bind, float elevation) {

}
