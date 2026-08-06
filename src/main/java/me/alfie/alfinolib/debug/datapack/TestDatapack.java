package me.alfie.alfinolib.debug.datapack;

import com.google.gson.JsonElement;
import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.datapacks.DatapackDefinition;
import me.alfie.alfinolib.datapacks.DatapackKey;
import me.alfie.alfinolib.datapacks.server.ServerDatapackRegistry;
import me.alfie.alfinolib.datapacks.ModDatapack;
import me.alfie.alfinolib.util.ResourceId;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TestDatapack extends ModDatapack<TestData, TestData> {

    public static final DatapackKey<TestData> KEY = new DatapackKey<>(AlfinoLib.MODID, "test_datapack");
    public static final DatapackDefinition<TestData> DEFINITION = new DatapackDefinition<>(KEY, TestData.STREAM_CODEC);

    private TestData data;

    public TestDatapack(RegistryAccess registryAccess) {
        super(TestData.CODEC, DEFINITION, registryAccess);
    }

    @Override
    public TestData getData() {
        return data;
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        ResourceId key = new ResourceId(AlfinoLib.MODID, "test_datapack");
        data = parseOrDefault(map.get(key.mc()), new TestData("Something went wrong!", 0));
    }

    public static void register(AddReloadListenerEvent event) {
        ServerDatapackRegistry.register(event, () -> new TestDatapack(event.getRegistryAccess()));
    }
}
