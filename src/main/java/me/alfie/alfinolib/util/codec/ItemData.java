package me.alfie.alfinolib.util.codec;

import com.mojang.serialization.Codec;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Version-abstracted item extra data. On 1.20.1 this wraps NBT; on 1.21.1+ it wraps DataComponentPatch.
 * Method signatures and static field names are kept identical across branches.
 */
public record ItemData(DataComponentPatch components) {

    public static final Codec<ItemData> CODEC = DataComponentPatch.CODEC.xmap(ItemData::new, ItemData::components);

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemData> STREAM_CODEC = StreamCodec.of(
            (buf, data) -> DataComponentPatch.STREAM_CODEC.encode(buf, data.components()),
            buf -> new ItemData(DataComponentPatch.STREAM_CODEC.decode(buf))
    );

    public boolean isEmpty() {
        return components.isEmpty();
    }

    public static ItemData empty() {
        return new ItemData(DataComponentPatch.EMPTY);
    }
}
