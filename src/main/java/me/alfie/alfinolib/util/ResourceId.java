package me.alfie.alfinolib.util;

import net.minecraft.resources.ResourceLocation;

/**
 * A cross-platform, cross-version, stable call to {@code ResourceLocation} on older versions
 * and {@code Identifier} on new versions.
 */
public record ResourceId(String namespace, String path) {

    /**
     * Converts to Minecraft Identifier/ResourceLocation depending on version.
     * @return
     */
    public ResourceLocation mc() {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
