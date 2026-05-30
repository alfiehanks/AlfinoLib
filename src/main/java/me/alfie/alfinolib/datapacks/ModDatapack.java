package me.alfie.alfinolib.datapacks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.minecraft.core.RegistryAccess;
import org.jetbrains.annotations.Nullable;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
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
public abstract class ModDatapack<A, B> extends SimpleJsonResourceReloadListener<A> {

    private final DatapackKey<B> datapackKey;
    private final StreamCodec<RegistryFriendlyByteBuf, B> streamCodec;
    private final RegistryAccess registryAccess;

    public ModDatapack(Codec<A> codec, DatapackKey<B> datapackKey, StreamCodec<RegistryFriendlyByteBuf, B> streamCodec, RegistryAccess registryAccess) {
        super(codec, FileToIdConverter.json(datapackKey.directory()));
        this.datapackKey = datapackKey;
        this.streamCodec = streamCodec;
        this.registryAccess = registryAccess;
    }

    public StreamCodec<RegistryFriendlyByteBuf, B> streamCodec() {
        return streamCodec;
    }

    public DatapackKey<B> key() {
        return datapackKey;
    }

    public abstract B getData();

    /** Returns value if non-null, otherwise defaultValue. Mirrors the parseOrDefault signature on older branches. */
    public A parseOrDefault(@Nullable A value, A defaultValue) {
        return value != null ? value : defaultValue;
    }

    @Override
    protected void apply(@NotNull Map<Identifier, A> identifierAMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {}
}
