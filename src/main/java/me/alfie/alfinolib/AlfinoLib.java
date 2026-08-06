package me.alfie.alfinolib;

import com.mojang.logging.LogUtils;
import me.alfie.alfinolib.datapacks.Datapacks;
import me.alfie.alfinolib.debug.DebugCommands;
import me.alfie.alfinolib.debug.datapack.TestData;
import me.alfie.alfinolib.debug.datapack.TestDatapack;
import me.alfie.alfinolib.networking.Networking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(AlfinoLib.MODID)
public class AlfinoLib {

    public static final String MODID = "alfinolib";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AlfinoLib(IEventBus modEventBus, ModContainer modContainer) {
        Networking.init(modEventBus);
        Datapacks.init(modEventBus);



        NeoForge.EVENT_BUS.addListener(TestDatapack::register);
        NeoForge.EVENT_BUS.addListener(DebugCommands::register);
    }

}
