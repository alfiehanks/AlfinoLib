package me.alfie.alfinolib.datapacks;

import me.alfie.alfinolib.networking.codec.StreamCodec;
import me.alfie.alfinolib.util.ResourceId;
import net.minecraft.network.FriendlyByteBuf;

/**
 * This key is used to register datapacks, retrieve data from {@link DataMap} and look up the corresponding {@link ModDatapack}.
 * @param modid the modid that owns the datapack
 * @param directory the datapack directory at {@code data/<modid>/<directory>}, all files within this folder will be searched
 * @param <T> the type of data this datapack provides - this must match field B in {@link ModDatapack}
 */
public record DatapackKey<T> (String modid, String directory) {

    public static final StreamCodec<FriendlyByteBuf, DatapackKey<?>> STREAM_CODEC =
            StreamCodec.of(DatapackKey::encode, DatapackKey::decode);

    private static void encode(FriendlyByteBuf buf, DatapackKey<?> datapackKey) {
        buf.writeUtf(datapackKey.modid());
        buf.writeUtf(datapackKey.directory());
    }

    private static DatapackKey<?> decode(FriendlyByteBuf buf) {
        String modid = buf.readUtf();
        String dir = buf.readUtf();
        return new DatapackKey<>(modid, dir);
    }

    public ResourceId id() {
        return new ResourceId(modid, directory);
    }

}
