package me.alfie.alfinolib.gui;

import me.alfie.alfinolib.gui.util.MousePos;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
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
        super(menu, inventory, title, imageWidth, imageHeight);
    }

    //Common render calls

    /*
    < 26.1 = renderBg
    > 26.1 = extractBackground
     */
    @Override
    public final void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(new GuiGraphicsX(graphics), new MousePos(mouseX, mouseY), partialTick);
    }

    /**
     * Common renderBackground method.
     */
    public void renderBackground(GuiGraphicsX gx, MousePos mousePos, float partialTick) {
        super.extractBackground(gx.graphics(), mousePos.x(), mousePos.y(), partialTick);
    }

    /*
    < 26.1 = render
    > 26.1 = extractRenderState
     */
    @Override
    public final void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        render(new GuiGraphicsX(graphics), new MousePos(mouseX, mouseY), partialTick);
    }

    /**
     * Common render method.
     */
    public void render(GuiGraphicsX gx, MousePos mousePos, float partialTick) {
        super.extractRenderState(gx.graphics(), mousePos.x(), mousePos.y(), partialTick);
    }

    /*
    < 26.1 = renderLabels
    > 26.1 = extractLabels
     */
    @Override
    protected final void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        renderLabels(new GuiGraphicsX(graphics), new MousePos(mouseX, mouseY));
    }

    /**
     * Common renderLabels method.
     */
    public void renderLabels(GuiGraphicsX gx, MousePos mousePos) {
        super.extractLabels(gx.graphics(), mousePos.x(), mousePos.y());
    }

    /*
    Mouse events:
    Abstracted to fit < 26.1 - Redirects events to ScreenEventListener methods.
    Users can override the ScreenEventListener methods i.e onMouseClick().
    Users can also implement ScreenEventListener into other GUI helper classes for common mouse event handling.
     */
    @Override
    public final boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return onMouseClick(new MousePos((int) event.x(), (int) event.y()), event.button());
    }

    @Override
    public boolean onMouseClick(MousePos mousePos, int button) {
        return ScreenEventListener.super.onMouseClick(mousePos, button);
    }

    @Override
    public final boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        return onMouseDrag(new MousePos((int) event.x(), (int) event.y()), event.button(), dx, dy);
    }

    @Override
    public boolean onMouseDrag(MousePos mousePos, int button, double dx, double dy) {
        return ScreenEventListener.super.onMouseDrag(mousePos, button, dx, dy);
    }

    @Override
    public final boolean mouseReleased(MouseButtonEvent event) {
        return onMouseRelease(new MousePos((int) event.x(), (int) event.y()), event.button());
    }

    @Override
    public boolean onMouseRelease(MousePos mousePos, int button) {
        return ScreenEventListener.super.onMouseRelease(mousePos, button);
    }

    @Override
    public final boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        return onMouseScrolled(new MousePos((int) x, (int) y), scrollY);
    }

    @Override
    public boolean onMouseScrolled(MousePos mousePos, double scrollY) {
        return ScreenEventListener.super.onMouseScrolled(mousePos, scrollY);
    }

    //Keyboard events

    @Override
    public final boolean keyPressed(KeyEvent event) {
        return onKeyPress(event.key(), event.scancode(), event.modifiers());
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        return ScreenEventListener.super.onKeyPress(keyCode, scanCode, modifiers);
    }

    @Override
    public final boolean charTyped(CharacterEvent event) {
        return onCharTyped((char) event.codepoint(), 0);
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
