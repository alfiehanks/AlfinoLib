package me.alfie.alfinolib.datapacks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Base class for all datapacks.
 *
 * @param <A> Raw data type decoded from JSON using the {@link Codec}
 * @param <B> Final processed data type used by your mod
 */
public abstract class ModDatapack<A, B> extends SimpleJsonResourceReloadListener {

    private final DatapackKey<B> datapackKey;
    private final StreamCodec<RegistryFriendlyByteBuf, B> streamCodec;
    private final Codec<A> codec;

    public ModDatapack(Codec<A> codec, DatapackKey<B> datapackKey, StreamCodec<RegistryFriendlyByteBuf, B> streamCodec) {
        super(new Gson(), datapackKey.directory());
        this.datapackKey = datapackKey;
        this.streamCodec = streamCodec;
        this.codec = codec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, B> streamCodec() {
        return streamCodec;
    }

    public DatapackKey<B> key() {
        return datapackKey;
    }

    public abstract B getData();

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {}

    /**
     * Helper method to parse a {@link JsonElement} into the specified codec type {@link A}.
     *
     * @param element the json element
     * @param defaultValue the value to default to if parsing fails
     * @return data of type {@link A}
     */
    public A parseOrDefault(JsonElement element, A defaultValue) {
        return codec.parse(JsonOps.INSTANCE, element)
                .resultOrPartial(error -> Datapacks.LOGGER.error("Failed to parse JSON for datapack {}: {}", datapackKey, error))
                .orElse(defaultValue);
    }
}
