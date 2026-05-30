package me.alfie.alfinolib;

import com.mojang.logging.LogUtils;
import me.alfie.alfinolib.datapacks.DatapackRegistry;
import me.alfie.alfinolib.datapacks.Datapacks;
import me.alfie.alfinolib.networking.NetworkRegisterEvent;
import me.alfie.alfinolib.networking.Networking;
import me.alfie.alfinolib.test.TestDatapack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AlfinoLib.MODID)
public class AlfinoLib {

    public static final String MODID = "alfinolib";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AlfinoLib(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        Networking.init(modEventBus);
        Datapacks.init(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(TestDatapack::register);
        MinecraftForge.EVENT_BUS.addListener(AlfinoLib::onJoin);
    }

    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        LOGGER.debug("Got data: {}", DatapackRegistry.get(TestDatapack.KEY).getData().getItemStacks());
        event.getEntity().getInventory().add(DatapackRegistry.get(TestDatapack.KEY).getData().getItemStacks().get(0));
    }

}
