package me.alfie.alfinolib;

import com.mojang.logging.LogUtils;
import me.alfie.alfinolib.datapacks.DatapackRegistry;
import me.alfie.alfinolib.datapacks.Datapacks;
import me.alfie.alfinolib.networking.NetworkRegisterEvent;
import me.alfie.alfinolib.networking.Networking;
import me.alfie.alfinolib.test.TestDatapack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;

@Mod(AlfinoLib.MODID)
public class AlfinoLib {

    public static final String MODID = "alfinolib";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AlfinoLib(IEventBus modEventBus, ModContainer modContainer) {
        Networking.init(modEventBus);
        Datapacks.init(modEventBus);

        NeoForge.EVENT_BUS.addListener(TestDatapack::register);
        NeoForge.EVENT_BUS.addListener(AlfinoLib::onJoin);
    }

    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        LOGGER.debug("Got data: {}", DatapackRegistry.get(TestDatapack.KEY).getData().getItemStacks());
        event.getEntity().getInventory().add(DatapackRegistry.get(TestDatapack.KEY).getData().getItemStacks().get(0));
    }

}
