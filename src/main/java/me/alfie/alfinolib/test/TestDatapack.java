package me.alfie.alfinolib.test;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.datapacks.DatapackKey;
import me.alfie.alfinolib.datapacks.DatapackRegistry;
import me.alfie.alfinolib.datapacks.ModDatapack;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import me.alfie.alfinolib.util.codec.ItemCost;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class TestDatapack extends ModDatapack<ItemCost, ItemCost> {

    public static DatapackKey<ItemCost> KEY = new DatapackKey<>(AlfinoLib.MODID, "test");
    ItemCost DATA;


    public TestDatapack(RegistryAccess registryAccess) {
        super(ItemCost.CODEC, KEY, ItemCost.STREAM_CODEC, registryAccess);
    }

    @Override
    public ItemCost getData() {
        return DATA;
    }

    @Override
    protected void apply(@NotNull Map<Identifier, ItemCost> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        AlfinoLib.LOGGER.debug("Map: {}", map);
        DATA = parseOrDefault(map.get(Identifier.parse("alfinolib:test")), ItemCost.EMPTY);
    }

    public static void register(AddServerReloadListenersEvent event) {
        DatapackRegistry.register(event, () -> new TestDatapack(event.getRegistryAccess()));
    }
}
