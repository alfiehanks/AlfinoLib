package me.alfie.alfinolib.datapacks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import me.alfie.alfinolib.networking.codec.StreamCodec;
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

    private final DatapackDefinition<B> definition;
    private final Codec<A> codec;
    private final RegistryAccess registryAccess;

    public ModDatapack(Codec<A> codec, DatapackDefinition<B> definition, RegistryAccess registryAccess) {
        super(codec, FileToIdConverter.json(definition.key().directory()));
        this.definition = definition;
        this.codec = codec;
        this.registryAccess = registryAccess;
    }

    public StreamCodec<RegistryFriendlyByteBuf, B> streamCodec() {
        return definition.streamCodec();
    }

    public DatapackKey<B> key() {
        return definition.key();
    }

    public abstract B getData();

    @Override
    protected void apply(@NotNull Map<Identifier, A> identifierAMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {}

    /** Returns value if non-null, otherwise defaultValue. Mirrors the parseOrDefault signature on older branches. */
    public A parseOrDefault(@Nullable A value, A defaultValue) {
        return value != null ? value : defaultValue;
    }
}