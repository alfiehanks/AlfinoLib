package me.alfie.alfinolib.networking.codec;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
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
 *             .add(CommonCodecs.VAR_INT, ItemCost::count)
 *             .build(ItemCost::new);
 * }</pre>
 *
 * <p>Supports up to 4 fields via typed {@code build()} overloads. For more fields,
 * implement {@link StreamCodec} directly.</p>
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
     * Adds a field to the codec. Accepts codecs typed for any supertype of {@code B},
     * so {@link CommonCodecs} constants (typed for {@link ByteBuf}) work directly.
     *
     * @param codec  codec used to encode/decode the field
     * @param getter extracts the field value from the parent object
     * @param <T>    field type
     * @return this builder for chaining
     */
    @SuppressWarnings("unchecked")
    public <T> StreamCodecBuilder<B, V> add(StreamCodec<? super B, T> codec, Function<V, T> getter) {
        steps.add(new Step<>((StreamCodec<B, T>) codec, getter));
        return this;
    }

    /** Builds a codec for a 1-field value. Pass a constructor reference, e.g. {@code MyRecord::new}. */
    @SuppressWarnings("unchecked")
    public <A> StreamCodec<B, V> build(Factory1<A, V> factory) {
        List<Step<B, V, ?>> s = List.copyOf(steps);
        return new StreamCodec<>() {
            @Override public void encode(B buf, V value) { for (var step : s) step.encode(buf, value); }
            @Override public V decode(B buf) { return factory.apply((A) s.get(0).decode(buf)); }
        };
    }

    /** Builds a codec for a 2-field value. Pass a constructor reference, e.g. {@code MyRecord::new}. */
    @SuppressWarnings("unchecked")
    public <A, C> StreamCodec<B, V> build(BiFunction<A, C, V> factory) {
        List<Step<B, V, ?>> s = List.copyOf(steps);
        return new StreamCodec<>() {
            @Override public void encode(B buf, V value) { for (var step : s) step.encode(buf, value); }
            @Override public V decode(B buf) {
                return factory.apply((A) s.get(0).decode(buf), (C) s.get(1).decode(buf));
            }
        };
    }

    /** Builds a codec for a 3-field value. Pass a constructor reference, e.g. {@code MyRecord::new}. */
    @SuppressWarnings("unchecked")
    public <A, C, D> StreamCodec<B, V> build(TriFunction<A, C, D, V> factory) {
        List<Step<B, V, ?>> s = List.copyOf(steps);
        return new StreamCodec<>() {
            @Override public void encode(B buf, V value) { for (var step : s) step.encode(buf, value); }
            @Override public V decode(B buf) {
                return factory.apply((A) s.get(0).decode(buf), (C) s.get(1).decode(buf), (D) s.get(2).decode(buf));
            }
        };
    }

    /** Builds a codec for a 4-field value. Pass a constructor reference, e.g. {@code MyRecord::new}. */
    @SuppressWarnings("unchecked")
    public <A, C, D, E> StreamCodec<B, V> build(QuadFunction<A, C, D, E, V> factory) {
        List<Step<B, V, ?>> s = List.copyOf(steps);
        return new StreamCodec<>() {
            @Override public void encode(B buf, V value) { for (var step : s) step.encode(buf, value); }
            @Override public V decode(B buf) {
                return factory.apply(
                        (A) s.get(0).decode(buf), (C) s.get(1).decode(buf),
                        (D) s.get(2).decode(buf), (E) s.get(3).decode(buf)
                );
            }
        };
    }

    @FunctionalInterface public interface Factory1<A, V> { V apply(A a); }
    @FunctionalInterface public interface TriFunction<A, B, C, V> { V apply(A a, B b, C c); }
    @FunctionalInterface public interface QuadFunction<A, B, C, D, V> { V apply(A a, B b, C c, D d); }

    private record Step<B extends ByteBuf, V, T>(StreamCodec<B, T> codec, Function<V, T> getter) {
        void encode(B buf, V value) { codec.encode(buf, getter.apply(value)); }
        Object decode(B buf) { return codec.decode(buf); }
    }
}
