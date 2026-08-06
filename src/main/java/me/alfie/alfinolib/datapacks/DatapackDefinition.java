package me.alfie.alfinolib.datapacks;

import com.mojang.serialization.Codec;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;

public record DatapackDefinition<T>(DatapackKey<T> key, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
}
