package me.alfie.alfinolib.datapacks.client;

import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.datapacks.DataMap;
import me.alfie.alfinolib.datapacks.DatapackKey;
import me.alfie.alfinolib.datapacks.Datapacks;
import me.alfie.alfinolib.datapacks.server.ServerDatapackRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;

@Mod(value = AlfinoLib.MODID, dist = Dist.CLIENT)
public class ClientDatapackManager {

    private static DataMap dataMap = new DataMap(new HashMap<>());

    public static void setDataMap(DataMap newDataMap) {
        dataMap = newDataMap;
        MinecraftForge.EVENT_BUS.post(new ClientDatapackUpdatedEvent(Minecraft.getInstance().player));
    }

    public static void onServerLeave(ClientPlayerNetworkEvent.LoggingOut event) {
        Datapacks.LOGGER.info("Client disconnected, clearing client dataMap and unregistering DatapackRegistry");
        dataMap = new DataMap(new HashMap<>());
    }

    public static <T> T get(DatapackKey<T> key) {
        return dataMap.get(key);
    }
}
