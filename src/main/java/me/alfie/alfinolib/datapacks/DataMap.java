package me.alfie.alfinolib.datapacks;


import me.alfie.alfinolib.datapacks.server.ServerDatapackRegistry;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record DataMap(Map<DatapackKey<?>, Object> map) {

    public static final StreamCodec<RegistryFriendlyByteBuf, DataMap> STREAM_CODEC =
            StreamCodec.of(DataMap::encode, DataMap::decode);

    public DataMap {
        map = Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(DatapackKey<T> key) {
        return (T) map.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> void encode(RegistryFriendlyByteBuf buf, DataMap dataMap) {
        //Encode server-side

        buf.writeInt(dataMap.map().size());

        for(Map.Entry<DatapackKey<?>, Object> entry : dataMap.map().entrySet()) {
            DatapackKey<T> key = (DatapackKey<T>) entry.getKey();
            T value = (T) entry.getValue();

            DatapackKey.STREAM_CODEC.encode(buf, key);

            //ModDatapack<?, T> datapack = ServerDatapackRegistry.get(key);
            DatapackDefinition<T> definition = DatapackRegistry.get(key);

            StreamCodec<RegistryFriendlyByteBuf, T> streamCodec = definition.streamCodec();
            streamCodec.encode(buf, value);
        }
    }

    public static DataMap decode(RegistryFriendlyByteBuf buf) {
        //Decode client-side

        int size = buf.readInt();
        Map<DatapackKey<?>, Object> result = new HashMap<>();

        for (int i = 0; i < size; i++) {
            DatapackKey<?> datapackKey = DatapackKey.STREAM_CODEC.decode(buf);
            DatapackDefinition<?> definition = DatapackRegistry.get(datapackKey);

            StreamCodec<RegistryFriendlyByteBuf, ?> streamCodec = definition.streamCodec();

            try {
                Object value = streamCodec.decode(buf);
                result.put(datapackKey, value);
            } catch (Exception e) {
                throw new IllegalStateException("Failed decoding key " + datapackKey + " using codec " + streamCodec, e);
            }
        }

        return new DataMap(result);
    }
}
