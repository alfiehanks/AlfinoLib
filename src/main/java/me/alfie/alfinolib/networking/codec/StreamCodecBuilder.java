package me.alfie.alfinolib.networking.codec;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A builder for creating {@link StreamCodec} instances in a composable way.
 *
 * <p>This replaces the need for multiple {@code composite(...)} overloads by allowing
 * you to register an arbitrary number of encoded fields in sequence.</p>
 *
 * <p>Each {@code add(codec, getter)} defines how one field of type {@code T} is:
 * <ul>
 *     <li>encoded using a {@link StreamCodec}</li>
 *     <li>extracted from the parent object using a getter</li>
 * </ul>
 * </p>
 *
 * <h2>Example usage</h2>
 *
 * <pre>{@code
 * public record ItemCost(Item item, int count) {}
 *
 * public static final StreamCodec<FriendlyByteBuf, ItemCost> CODEC =
 *         StreamCodecBuilder.<FriendlyByteBuf, ItemCost>create()
 *             .add(ItemCodec, ItemCost::item)
 *             .add(IntCodec, ItemCost::count)
 *             .build(values -> new ItemCost(
 *                 (Item) values.get(0),
 *                 (Integer) values.get(1)
 *             ));
 * }</pre>
 *
 * <p>This allows unlimited fields without needing multiple overloaded composite methods.</p>
 *
 * @param <B> buffer type (e.g. {@link ByteBuf}, FriendlyByteBuf)
 * @param <V> value type being encoded/decoded
 */
public final class StreamCodecBuilder<B extends ByteBuf, V> {

    private final List<Step<B, V, ?>> steps = new ArrayList<>();

    private StreamCodecBuilder() {}

    public static <B extends ByteBuf, V> StreamCodecBuilder<B, V> create() {
        return new StreamCodecBuilder<>();
    }

    /**
     * Adds a field to the codec.
     *
     * @param codec  codec used to encode/decode the field
     * @param getter extracts the field value from the parent object
     * @param <T>    field type
     * @return this builder for chaining
     */
    public <T> StreamCodecBuilder<B, V> add(StreamCodec<B, T> codec, Function<V, T> getter) {
        steps.add(new Step<>(codec, getter));
        return this;
    }

    /**
     * Builds the final StreamCodec.
     *
     * @param factory function that reconstructs the object from decoded values
     * @return a fully composed StreamCodec
     */
    public StreamCodec<B, V> build(Function<List<Object>, V> factory) {
        return new StreamCodec<>() {

            @Override
            public void encode(B buf, V value) {
                for (Step<B, V, ?> step : steps) {
                    step.encode(buf, value);
                }
            }

            @Override
            public V decode(B buf) {
                List<Object> values = new ArrayList<>(steps.size());

                for (Step<B, V, ?> step : steps) {
                    values.add(step.decode(buf));
                }

                return factory.apply(values);
            }
        };
    }

    /**
     * Internal step representing one encoded field.
     */
    private record Step<B extends ByteBuf, V, T>(StreamCodec<B, T> codec, Function<V, T> getter) {

        void encode(B buf, V value) {
                codec.encode(buf, getter.apply(value));
            }

            Object decode(B buf) {
                return codec.decode(buf);
            }
        }
}