package me.alfie.alfinolib.gui.util;

/**
 * Container for mouseX and mouseY - used throughout the library's GUI module.
 * <br>NeoForge 26.1 in particular mixes use of separate mouseX/mouseY variables and mixed MouseEvent x()/y() calls.
 * <br>This record standardises how mouse positions are handled.
 * @param x
 * @param y
 */
public record MousePos(int x, int y) {

    /**
     * Check if the mouse is over a bounding box on the screen.
     * @param x Top-left X pos of the bounding box
     * @param y Top-left Y pos of the bounding box
     * @param width Width of the bounding box
     * @param height Height of the bounding box
     * @return true if mouse is over the bounding box
     */
    public boolean isOver(int x, int y, int width, int height) {
        return this.x >= x && this.x < x + width
                && this.y >= y && this.y < y + height;
    }
}
