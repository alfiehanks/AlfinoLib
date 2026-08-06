package me.alfie.alfinolib.datapacks.server;




import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.datapacks.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Stores a map of DatapackKeys and raw ModDatapacks server-side. The ServerDatapackManager pulls the data from here and sends it to the client.
 */
public final class ServerDatapackRegistry {

    private static final Map<DatapackKey<?>, ModDatapack<?, ?>> DATAPACKS = new HashMap<>();

    public static <A, B> void register(AddReloadListenerEvent event, Supplier<ModDatapack<A, B>> factory) {
        ModDatapack<A, B> datapack = factory.get();
        DatapackKey<B> datapackKey = datapack.key();

        DatapackDefinition<B> definition = DatapackRegistry.get(datapackKey);
        if(definition == null) {
            throw new IllegalStateException(
                    "Datapack " + datapackKey + " has not been registered with DatapackRegistry. " +
                    "Register a DatapackDefinition before registering the server datapack."
            );
        }

        event.addListener(datapack);
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
