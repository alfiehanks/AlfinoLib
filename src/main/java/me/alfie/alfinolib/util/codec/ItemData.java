package me.alfie.alfinolib.util.codec;

import com.mojang.serialization.Codec;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Objects;

/**
 * Version-abstracted item extra data. On 1.20.1 this wraps NBT; on 1.21.1+ it wraps DataComponentPatch.
 * Method signatures and static field names are kept identical across branches.
 */
public record ItemData(CompoundTag tag) {

    public static final Codec<ItemData> CODEC = CompoundTag.CODEC.xmap(ItemData::new, ItemData::tag);

    public static final StreamCodec<FriendlyByteBuf, ItemData> STREAM_CODEC = StreamCodec.of(
            (buf, data) -> buf.writeNbt(data.tag()),
            buf -> new ItemData(Objects.requireNonNullElseGet(buf.readNbt(), CompoundTag::new))
    );

    public boolean isEmpty() {
        return tag == null || tag.isEmpty();
    }

    public static ItemData empty() {
        return new ItemData(new CompoundTag());
    }
}
