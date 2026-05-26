package me.alfie.alfinolib.datapacks;




import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class DatapackRegistry {

    private static final Map<DatapackKey<?>, ModDatapack<?, ?>> DATAPACKS = new HashMap<>();

    public static <A, B> void register(AddReloadListenerEvent event, Supplier<ModDatapack<A, B>> factory) {
        ModDatapack<A, B> datapack = factory.get();
        DatapackKey<B> datapackKey = datapack.key();

        event.addListener(datapack);
        DATAPACKS.put(datapackKey, datapack);
        Datapacks.LOGGER.debug("Registered datapack {}", datapackKey);
    }

    @SuppressWarnings("unchecked")
    public static <T> ModDatapack<?, T> get(DatapackKey<T> key) {
        return (ModDatapack<?, T>) DATAPACKS.get(key);
    }

    public static Set<Map.Entry<DatapackKey<?>, ModDatapack<?, ?>>> entrySet() {
        return DATAPACKS.entrySet();
    }
}
