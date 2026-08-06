package me.alfie.alfinolib;

import com.mojang.logging.LogUtils;
import me.alfie.alfinolib.datapacks.Datapacks;
import me.alfie.alfinolib.debug.DebugCommands;
import me.alfie.alfinolib.debug.datapack.TestData;
import me.alfie.alfinolib.debug.datapack.TestDatapack;
import me.alfie.alfinolib.networking.Networking;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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



        NeoForge.EVENT_BUS.addListener(TestDatapack::register);
        NeoForge.EVENT_BUS.addListener(DebugCommands::register);
    }

}
