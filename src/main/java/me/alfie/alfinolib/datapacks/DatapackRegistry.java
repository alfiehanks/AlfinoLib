package me.alfie.alfinolib.datapacks;




import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class DatapackRegistry {

    private static final Map<DatapackKey<?>, ModDatapack<?, ?>> DATAPACKS = new HashMap<>();

    public static <A, B> void register(AddServerReloadListenersEvent event, Supplier<ModDatapack<A, B>> factory) {
        ModDatapack<A, B> datapack = factory.get();
        DatapackKey<B> datapackKey = datapack.key();

        event.addListener(datapackKey.id().mc(), datapack);
        DATAPACKS.put(datapackKey, datapack);
        Datapacks.LOGGER.debug("Registered datapack {}", datapackKey);
    }

    /**
     * Clear the datapack hashmap, only intended for client to prevent caching datapack keys between worlds/servers.
     */
    static void unregister() {
        DATAPACKS.clear();
    }

    @SuppressWarnings("unchecked")
    public static <T> ModDatapack<?, T> get(DatapackKey<T> key) {
        return (ModDatapack<?, T>) DATAPACKS.get(key);
    }

    public static Set<Map.Entry<DatapackKey<?>, ModDatapack<?, ?>>> entrySet() {
        return DATAPACKS.entrySet();
    }
}
