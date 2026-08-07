package me.alfie.alfinolib;

import com.mojang.logging.LogUtils;
import me.alfie.alfinolib.commands.Commands;
import me.alfie.alfinolib.datapacks.DatapackRegistry;
import me.alfie.alfinolib.datapacks.Datapacks;
import me.alfie.alfinolib.debug.command.CheckDatapackSyncCommand;
import me.alfie.alfinolib.debug.datapack.TestDatapack;
import me.alfie.alfinolib.networking.Networking;
import net.minecraftforge.common.MinecraftForge;
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
        Commands.init(modEventBus);

        Commands.register(new CheckDatapackSyncCommand());

        DatapackRegistry.register(TestDatapack.DEFINITION, TestDatapack::new);
    }

}
