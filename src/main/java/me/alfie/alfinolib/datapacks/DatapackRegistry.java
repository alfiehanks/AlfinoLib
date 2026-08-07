package me.alfie.alfinolib.datapacks;

import me.alfie.alfinolib.datapacks.server.ServerDatapackRegistry;
import me.alfie.alfinolib.debug.datapack.TestDatapack;
import net.minecraft.core.RegistryAccess;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The common DatapackRegistry stored on client and server. Simply stores a DatapackKey and its definition.
 * The definition contains a reference to the DatapackKey and its StreamCodec, used for decoding data on the client.
 * Does not store actual data or datapacks, see ServerDatapackRegistry.
 */
public class DatapackRegistry {

    private static final Map<DatapackKey<?>, DatapackDefinition<?>> DEFINITIONS = new HashMap<>();

    public static <A, B, D extends ModDatapack<A, B>> void register(DatapackDefinition<B> definition, Function<RegistryAccess, D> factory) {
        DatapackDefinition<?> existing = DEFINITIONS.put(definition.key(), definition);

        if(existing != null) {
            throw new IllegalStateException(
                    "Duplicate datapack definition registered for " + definition.key()
            );
        }

        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            ServerDatapackRegistry.register(
                    event,
                    () -> factory.apply(event.getRegistryAccess())
            );
        });
    }

    public static void clear() {
        DEFINITIONS.clear();
    }

    @SuppressWarnings("unchecked")
    public static <T> DatapackDefinition<T> get(DatapackKey<T> key) {
        return (DatapackDefinition<T>) DEFINITIONS.get(key);
    }
}
