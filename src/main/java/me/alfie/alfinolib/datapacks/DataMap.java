package me.alfie.alfinolib.datapacks;


import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record DataMap(Map<DatapackKey<?>, Object> map) {

    public static final StreamCodec<FriendlyByteBuf, DataMap> STREAM_CODEC =
            StreamCodec.of(DataMap::encode, DataMap::decode);

    public DataMap {
        map = Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(DatapackKey<T> key) {
        return (T) map.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> void encode(FriendlyByteBuf buf, DataMap dataMap) {
        buf.writeInt(dataMap.map().size());

        for(Map.Entry<DatapackKey<?>, Object> entry : dataMap.map().entrySet()) {
            DatapackKey<T> key = (DatapackKey<T>) entry.getKey();
            T value = (T) entry.getValue();

            DatapackKey.STREAM_CODEC.encode(buf, key);
            ModDatapack<?, T> datapack = DatapackRegistry.get(key);

            StreamCodec<FriendlyByteBuf, T> streamCodec = datapack.streamCodec();
            streamCodec.encode(buf, value);
        }
    }

    public static DataMap decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        Map<DatapackKey<?>, Object> result = new HashMap<>();

        for (int i = 0; i < size; i++) {
            DatapackKey<?> datapackKey = DatapackKey.STREAM_CODEC.decode(buf);
            ModDatapack<?, ?> datapack = DatapackRegistry.get(datapackKey);

            StreamCodec<FriendlyByteBuf, ?> streamCodec = datapack.streamCodec();
            Object value = streamCodec.decode(buf);
            result.put(datapackKey, value);
        }

        return new DataMap(result);
    }
}
