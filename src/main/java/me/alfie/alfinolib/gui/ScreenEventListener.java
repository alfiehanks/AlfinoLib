package me.alfie.alfinolib.gui;

import me.alfie.alfinolib.gui.util.MousePos;

public interface ScreenEventListener {

    default boolean onMouseClick(MousePos mousePos, int button, int modifiers) {
        return false;
    }

    default boolean onMouseDrag(MousePos mousePos, int button, double dx, double dy) {
        return false;
    }

    default boolean onMouseRelease(MousePos mousePos, int button) {
        return false;
    }

    default boolean onMouseScrolled(MousePos mousePos, double scrollY) {
        return false;
    }

    default boolean onKeyPress(int keyCode, int scanCode, int modifiers) { return false; }

    default boolean onCharTyped(char codePoint, int modifiers) { return false; }

}
