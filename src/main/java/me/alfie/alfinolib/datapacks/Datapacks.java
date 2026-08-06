package me.alfie.alfinolib.datapacks;

import com.mojang.logging.LogUtils;
import me.alfie.alfinolib.datapacks.client.ClientDatapackManager;
import me.alfie.alfinolib.datapacks.server.ServerDatapackManager;
import me.alfie.alfinolib.networking.NetworkRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import me.alfie.alfinolib.networking.Networking;
import org.slf4j.Logger;

public final class Datapacks {

    public static final Logger LOGGER = LogUtils.getLogger();

    //Setup

    /**
     * Fired when the mod loads - internal use only.
     * @param modEventBus
     */
    public static void init(IEventBus modEventBus) {
        MinecraftForge.EVENT_BUS.addListener(ServerDatapackManager::onServerStart);
        MinecraftForge.EVENT_BUS.addListener(ServerDatapackManager::onServerFinished);
        MinecraftForge.EVENT_BUS.addListener(ServerDatapackManager::onServerReload);
        MinecraftForge.EVENT_BUS.addListener(ServerDatapackManager::onServerStop);

        if (FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.addListener(ClientDatapackManager::onServerLeave);
        }

        modEventBus.addListener(Datapacks::registerPacket);
    }

    private static void registerPacket(NetworkRegisterEvent event) {
        event.register(SyncClientDatapackPacket.class, SyncClientDatapackPacket.STREAM_CODEC);
    }
}
