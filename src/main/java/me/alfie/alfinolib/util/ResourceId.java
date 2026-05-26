package me.alfie.alfinolib.util;

import net.minecraft.resources.ResourceLocation;

/**
 * A cross-platform, cross-version, stable call to {@code ResourceLocation} on older versions
 * and {@code Identifier} on new versions.
 */
public record ResourceId(String namespace, String path) {

    /**
     * @return Converts to Minecraft Identifier/ResourceLocation depending on version.
     */
    public ResourceLocation mc() {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public String toString() {
        return namespace + ":" + path;
    }

    /**
     * Convert a string i.e "minecraft:dirt" into a ResourceId object by splitting at the colon.
     * @param id id as string
     * @return ResourceId object
     */
    public static ResourceId parse(String id) {
        String[] parts = id.split(":", 2);

        if (parts.length != 2) throw new IllegalArgumentException("Invalid ResourceId: " + id);

        return new ResourceId(parts[0], parts[1]);
    }


}

