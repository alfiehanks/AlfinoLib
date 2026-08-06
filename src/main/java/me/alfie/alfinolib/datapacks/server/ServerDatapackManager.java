package me.alfie.alfinolib.datapacks.server;

import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.datapacks.*;
import me.alfie.alfinolib.networking.Networking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;

import java.util.HashMap;
import java.util.Map;

public final class ServerDatapackManager {

    private static ServerDatapackManager INSTANCE;
    private static MinecraftServer SERVER;

    private DataMap dataMap;

    /**
     * Gets the processed datapack data for a given key.
     * @param key the datapack key
     * @return the processed datapack data
     * @param <T> the expected return type
     */
    public static <T> T get(DatapackKey<T> key) {
        return getInstance().dataMap.get(key);
    }

    public static ServerDatapackManager getInstance() {
        if(INSTANCE == null) {
            Datapacks.LOGGER.error("Server not initialised, unable to get instance of ServerDatapackManager.");
            throw new IllegalStateException("Server not initialised!");
        }
        return INSTANCE;
    }

    public MinecraftServer getServer() {
        return SERVER;
    }

    /**Initialise the ServerDatapackManager*/
    public static void onServerStart(ServerAboutToStartEvent event) {
        SERVER = event.getServer();
        INSTANCE = new ServerDatapackManager();
        Datapacks.LOGGER.debug("ServerDatapackManager initialised.");
    }

    /**Pull data after server loads*/
    public static void onServerFinished(ServerStartedEvent event) {
        Datapacks.LOGGER.debug("Server finished loading, attempting to pull datapacks...");
        pullData();
    }

    public static void onServerReload(OnDatapackSyncEvent event) {
        if(event.getPlayer() == null) {
            Datapacks.LOGGER.debug("Reloading datapacks, attempting to pull datapacks...");
            pullData();

            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                sendSyncPacket(player);
            }
        } else {
            sendSyncPacket(event.getPlayer());
        }
    }

    public static void onServerStop(ServerStoppedEvent event) {
        SERVER = null;
        INSTANCE = null;
        Datapacks.LOGGER.debug("ServerDatapackManager uninitialised, datapacks unloaded.");
    }

    /**
     * Pull data from each datapack and build a {@link DataMap}.
     */
    private static void pullData() {
        Map<DatapackKey<?>, Object> result = new HashMap<>();

        for (Map.Entry<DatapackKey<?>, ModDatapack<?, ?>> entry : ServerDatapackRegistry.entrySet()) {
            ModDatapack<?, ?> datapack = entry.getValue();
            result.put(entry.getKey(), datapack.getData());
            Datapacks.LOGGER.debug("Successfully pulled data for {}", datapack);
        }

        getInstance().dataMap = new DataMap(result);
        MinecraftForge.EVENT_BUS.post(new ServerDatapackUpdatedEvent(getInstance().getServer()));
    }

    private static void sendSyncPacket(ServerPlayer player) {
        Datapacks.LOGGER.debug("Sending sync packet to {}", player);

        Networking.sendToClient(player, new SyncClientDatapackPacket(getInstance().dataMap));
    }



}
