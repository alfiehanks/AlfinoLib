package me.alfie.alfinolib.datapacks;

import com.mojang.logging.LogUtils;
import me.alfie.alfinolib.datapacks.client.ClientDatapackManager;
import me.alfie.alfinolib.datapacks.server.ServerDatapackManager;
import me.alfie.alfinolib.networking.NetworkRegisterEvent;
import me.alfie.alfinolib.networking.Networking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

public final class Datapacks {

    public static final Logger LOGGER = LogUtils.getLogger();

    //Setup

    /**
     * Fired when the mod loads - internal use only.
     * @param modEventBus
     */
    public static void init(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(ServerDatapackManager::onServerStart);
        NeoForge.EVENT_BUS.addListener(ServerDatapackManager::onServerFinished);
        NeoForge.EVENT_BUS.addListener(ServerDatapackManager::onServerReload);
        NeoForge.EVENT_BUS.addListener(ServerDatapackManager::onServerStop);

        if (FMLEnvironment.dist.isClient()) {
            NeoForge.EVENT_BUS.addListener(ClientDatapackManager::onServerLeave);
        }

        modEventBus.addListener(Datapacks::registerPacket);
    }

    private static void registerPacket(NetworkRegisterEvent event) {
        event.register(Networking.Side.CLIENT, SyncClientDatapackPacket.TYPE, SyncClientDatapackPacket.STREAM_CODEC);
    }
}
