package me.alfie.alfinolib.datapacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The common DatapackRegistry stored on client and server. Simply stores a DatapackKey and its definition.
 * The definition contains a reference to the DatapackKey and its StreamCodec, used for decoding data on the client.
 * Does not store actual data or datapacks, see ServerDatapackRegistry.
 */
public class DatapackRegistry {

    private static final Map<DatapackKey<?>, DatapackDefinition<?>> DEFINITIONS = new HashMap<>();

    public static void register(DatapackDefinition<?> definition) {
        DatapackDefinition<?> existing = DEFINITIONS.put(definition.key(), definition);

        if(existing != null) {
            throw new IllegalStateException(
                    "Duplicate datapack definition registered for " + definition.key()
            );
        }
    }

    public static void clear() {
        DEFINITIONS.clear();
    }

    @SuppressWarnings("unchecked")
    public static <T> DatapackDefinition<T> get(DatapackKey<T> key) {
        return (DatapackDefinition<T>) DEFINITIONS.get(key);
    }
}
