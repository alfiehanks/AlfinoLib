package me.alfie.alfinolib.gui.util;

import me.alfie.alfinolib.gui.CommonAbstractContainerScreen;
import me.alfie.alfinolib.gui.GuiGraphicsX;
import me.alfie.alfinolib.util.ResourceId;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

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
        gx.graphics().blit(resourceId.mc(),
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
        gx.graphics().drawString(font, component, x, y, 0xFFFFFF, dropShadow);
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
        gx.graphics().renderItem(stack, x, y);
        gx.graphics().renderItemDecorations(font, stack, x, y);
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
            gx.graphics().renderTooltip(font, stack, mousePos.x(), mousePos.y());
        }
    }


    /**
     * Renders an ItemStack item with its number, and shows the item tooltip when hovered over.
     * This method cycles through a list of item stack, changing the stack displayed based on cycleSpeed.
     * @param gx GuiGraphicsX from CommonAbstractContainerScreen
     * @param stack List of ItemStacks to render
     * @param font Font from Screen
     * @param x X position
     * @param y Y position
     * @param mousePos Mouse position
     * @param cycleSpeed How fast the stack cycles
     */
    public static void itemStackWithTooltipCycled(GuiGraphicsX gx, List<ItemStack> stack, Font font, int x, int y, MousePos mousePos, int cycleSpeed) {
        itemStackWithTooltip(gx, getCycledElement(stack, cycleSpeed), font, x, y, mousePos);
    }

    /**
     * Returns an element within the list.
     * @param list List to cycle through
     * @param speed Speed of cycling through list in ms.
     * @return Element from list, determined by speed.
     */
    public static <T> T getCycledElement(List<T> list, int speed) {
        if (list == null || list.isEmpty()) return null;
        if (speed <= 0) return list.get(0);

        long currentTime = System.currentTimeMillis();
        int index = (int) ((currentTime / speed) % list.size());

        return list.get(index);
    }
}
