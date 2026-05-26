package me.alfie.alfinolib.gui.util;

/**
 * Container for mouseX and mouseY - used throughout the library's GUI module.
 * <br>NeoForge 26.1 in particular mixes use of separate mouseX/mouseY variables and mixed MouseEvent x()/y() calls.
 * <br>This record standardises how mouse positions are handled.
 * @param x
 * @param y
 */
public record MousePos(int x, int y) {
}
