package me.alfie.alfinolib.gui.util;

import me.alfie.alfinolib.gui.CommonAbstractContainerScreen;
import me.alfie.alfinolib.gui.GuiGraphicsX;
import me.alfie.alfinolib.util.ResourceId;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Calls to GuiGraphics/GuiGraphicsExtractor that aim to stay the same between all versions, uses GuiGraphicsX.
 * <br> Note: You can still use the GuiGraphicsX instance directly if there are no matching use-cases here.
 */
public final class GuiGraphicsApi {

    /**
     * Calls .blit() internally, renders the ResourceLocation/Identifier.
     * <br> Removes requirement to pass in UV offset, RenderPipeline or repeat width/height.
     * @param gx GuiGraphicsX from CommonAbstractContainerScreen
     * @param resourceId Common ResourceLocation/Identifier for texture
     * @param x X-Pos on screen
     * @param y Y-Pos on screen
     * @param width Image width
     * @param height Image height
     */
    public static void blit(GuiGraphicsX gx, ResourceId resourceId, int x, int y, int width, int height) {
        gx.graphics().blit(RenderPipelines.GUI_TEXTURED,
                resourceId.mc(),
                x, y,
                0f, 0f,
                width, height,
                width, height);
    }

    /**
     * Special blit method that automatically places the sprite at 0, 0 relative to getGuiLeft() and getGuiTop().
     * @param gx GuiGraphicsX from CommonAbstractContainerScreen
     * @param resourceId Common ResourceLocation/Identifier for texture
     * @param screen CommonAbstractContainerScreen - used to getGuiLeft/Top()
     * @param x X relative to top-left (0, 0)
     * @param y Y relative to top-left (0, 0)
     * @param width Image width
     * @param height Image height
     */
    public static void blitRelative(GuiGraphicsX gx, ResourceId resourceId,
                                      CommonAbstractContainerScreen<?> screen,
                                      int x, int y, int width, int height) {
        blit(gx, resourceId, screen.getGuiLeft() + x, screen.getGuiTop() + y, width, height);
    }

    /**
     * Renders a text component - default color white, but this can be changed using Component.withStyle/withColor().
     * @param gx GuiGraphicsX from CommonAbstractContainerScreen
     * @param font Font from Screen
     * @param component Component (usually .translatable())
     * @param x X position
     * @param y Y position
     * @param dropShadow Whether or not to render the shadow behind the text (usually false in GUI screens)
     */
    public static void text(GuiGraphicsX gx, Font font, Component component, int x, int y, boolean dropShadow) {
        gx.graphics().text(font, component, x, y, 0xFFFFFF, dropShadow);
    }

    /**
     * Renders an ItemStack item with its number. For ItemStack's with tooltip on hover, use itemStackWithTooltip() instead.
     * @param gx GuiGraphicsX from CommonAbstractContainerScreen
     * @param stack ItemStack to render
     * @param font Font from Screen
     * @param x X position
     * @param y Y position
     */
    public static void itemStack(GuiGraphicsX gx, ItemStack stack, Font font, int x, int y) {
        gx.graphics().item(stack, x, y);
        gx.graphics().itemDecorations(font, stack, x, y);
    }

    /**
     * Renders an ItemStack item with its number, and shows the item tooltip when hovered over.
     * @param gx GuiGraphicsX from CommonAbstractContainerScreen
     * @param stack ItemStack to render
     * @param font Font from Screen
     * @param x X position
     * @param y Y position
     * @param mousePos Mouse position
     */
    public static void itemStackWithTooltip(GuiGraphicsX gx, ItemStack stack, Font font, int x, int y, MousePos mousePos) {
        itemStack(gx, stack, font, x, y);
        if (mousePos.isOver(x, y, 16, 16)) {
            gx.graphics().setTooltipForNextFrame(font, stack, mousePos.x(), mousePos.y());
        }
    }
}
