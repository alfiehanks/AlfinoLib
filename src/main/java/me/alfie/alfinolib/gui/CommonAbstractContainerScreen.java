package me.alfie.alfinolib.gui;

import me.alfie.alfinolib.gui.util.MousePos;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * A cross-platform, cross-version AbstractContainerScreen with stable method names/signatures.
 */
public abstract class CommonAbstractContainerScreen<T extends AbstractContainerMenu>
        extends AbstractContainerScreen<T> implements ScreenEventListener{

    /**
     * Common constructor
     * @param menu AbstractContainerMenu
     * @param inventory Inventory
     * @param title Title of GUI
     * @param imageWidth Background sprite width
     * @param imageHeight Background sprite height
     */
    public CommonAbstractContainerScreen(T menu, Inventory inventory, Component title, int imageWidth, int imageHeight) {
        super(menu, inventory, title);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    //Common render calls

    /*
    < 26.1 = renderBg
    > 26.1 = extractBackground
     */
    @Override
    protected final void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        renderBackground(new GuiGraphicsX(graphics), new MousePos(mouseX, mouseY), partialTick);
    }

    /**
     * Common renderBackground method.
     */
    public void renderBackground(GuiGraphicsX gx, MousePos mousePos, float partialTick) {
    }

    /*
    < 26.1 = render
    > 26.1 = extractRenderState
     */
    @Override
    public final void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        render(new GuiGraphicsX(graphics), new MousePos(mouseX, mouseY), partialTick);
    }

    /**
     * Common render method.
     */
    public void render(GuiGraphicsX gx, MousePos mousePos, float partialTick) {
        super.render(gx.graphics(), mousePos.x(), mousePos.y(), partialTick);
    }

    /*
    < 26.1 = renderLabels
    > 26.1 = extractLabels
     */
    @Override
    protected final void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        renderLabels(new GuiGraphicsX(graphics), new MousePos(mouseX, mouseY));
    }

    /**
     * Common renderLabels method.
     */
    public void renderLabels(GuiGraphicsX gx, MousePos mousePos) {
        super.renderLabels(gx.graphics(), mousePos.x(), mousePos.y());
    }

    /*
    Mouse events:
    Abstracted to fit < 26.1 - Redirects events to ScreenEventListener methods.
    Users can override the ScreenEventListener methods i.e onMouseClick().
    Users can also implement ScreenEventListener into other GUI helper classes for common mouse event handling.
     */
    @Override
    public final boolean mouseClicked(double mouseX, double mouseY, int button) {
        return onMouseClick(new MousePos((int) mouseX, (int) mouseY), button);
    }

    @Override
    public boolean onMouseClick(MousePos mousePos, int button) {
        return ScreenEventListener.super.onMouseClick(mousePos, button);
    }

    @Override
    public final boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        return onMouseDrag(new MousePos((int) mouseX, (int) mouseY), button, dx, dy);
    }

    @Override
    public boolean onMouseDrag(MousePos mousePos, int button, double dx, double dy) {
        return ScreenEventListener.super.onMouseDrag(mousePos, button, dx, dy);
    }

    @Override
    public final boolean mouseReleased(double mouseX, double mouseY, int button) {
        return onMouseRelease(new MousePos((int) mouseX, (int) mouseY), button);
    }

    @Override
    public boolean onMouseRelease(MousePos mousePos, int button) {
        return ScreenEventListener.super.onMouseRelease(mousePos, button);
    }

    @Override
    public final boolean mouseScrolled(double x, double y, double scrollY) {
        return onMouseScrolled(new MousePos((int) x, (int) y), scrollY);
    }

    @Override
    public boolean onMouseScrolled(MousePos mousePos, double scrollY) {
        return ScreenEventListener.super.onMouseScrolled(mousePos, scrollY);
    }

    //Keyboard events

    @Override
    public final boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return onKeyPress(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        return ScreenEventListener.super.onKeyPress(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return onCharTyped(codePoint, modifiers);
    }

    /**
     * Note: NeoForge 26.1 does not use modifiers - modifiers is always a dummy value in this version.
     * @param codePoint Character typed
     * @param modifiers Modifiers i.e shift
     * @return true if press captured
     */
    @Override
    public boolean onCharTyped(char codePoint, int modifiers) {
        return ScreenEventListener.super.onCharTyped(codePoint, modifiers);
    }
}
