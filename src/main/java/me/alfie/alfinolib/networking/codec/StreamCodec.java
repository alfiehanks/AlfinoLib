package me.alfie.alfinolib.networking.codec;

import io.netty.buffer.ByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Internal StreamCodec interface based on StreamCodec from NeoForge 1.21.1
 */
public interface StreamCodec<B extends ByteBuf, V> {

    void encode(B buf, V value);
    V decode(B buf);

    static <B extends ByteBuf, V> StreamCodec<B, V> of(BiConsumer<B, V> encoder, Function<B, V> decoder) {
        return new StreamCodec<>() {
            @Override public void encode(B buf, V value) { encoder.accept(buf, value); }
            @Override public V decode(B buf) { return decoder.apply(buf); }
        };
    }

    static <B extends ByteBuf, V> StreamCodec<B, V> unit(V value) {
        return new StreamCodec<>() {
            @Override public V decode(B buf) { return value; }
            @Override public void encode(B buf, V input) {
                if (!value.equals(input)) throw new IllegalStateException("Expected constant value '" + value + "' but got '" + input + "'");
            }
        };
    }

    /** Return a net.minecraft.network.codec.StreamCodec<B,V> based on this API StreamCodec.*/
    default net.minecraft.network.codec.StreamCodec<B, V> toMinecraftStreamCodec() {
        return net.minecraft.network.codec.StreamCodec.of(this::encode, this::decode);
    }
}
