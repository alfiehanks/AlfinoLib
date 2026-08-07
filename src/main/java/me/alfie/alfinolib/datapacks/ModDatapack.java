package me.alfie.alfinolib.datapacks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import me.alfie.alfinolib.datapacks.server.ServerDatapackRegistry;
import me.alfie.alfinolib.debug.datapack.TestDatapack;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Base class for all datapacks.
 *
 * @param <A> Raw data type decoded from JSON using the {@link Codec}
 * @param <B> Final processed data type used by your mod
 */
public abstract class ModDatapack<A, B> extends SimpleJsonResourceReloadListener {

    private final DatapackDefinition<B> definition;
    private final Codec<A> codec;
    private final RegistryAccess registryAccess;

    public ModDatapack(Codec<A> codec, DatapackDefinition<B> definition, RegistryAccess registryAccess) {
        super(new Gson(), definition.key().directory());
        this.definition = definition;
        this.codec = codec;
        this.registryAccess = registryAccess;
    }

    public StreamCodec<FriendlyByteBuf, B> streamCodec() {
        return definition.streamCodec();
    }

    public DatapackKey<B> key() {
        return definition.key();
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
        return codec.parse(RegistryOps.create(JsonOps.INSTANCE, registryAccess), element)
                .resultOrPartial(error -> Datapacks.LOGGER.error("Failed to parse JSON for datapack {}: {}", definition.key(), error))
                .orElse(defaultValue);
    }
}
