package me.alfie.alfinolib.gui;

import me.alfie.alfinolib.gui.util.GuiGraphicsApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;

/**
 * Stands for GuiGraphicsX-Platform: A cross-platform, cross-version, stable call to {@code GuiGraphics} on older versions
 * and {@code GuiGraphicsExtractor} on new versions. This is used in CommonAbstractContainerScreen
 * for method params.
 *
 * @see GuiGraphicsApi
 */
public record GuiGraphicsX(GuiGraphics graphics) {

}
