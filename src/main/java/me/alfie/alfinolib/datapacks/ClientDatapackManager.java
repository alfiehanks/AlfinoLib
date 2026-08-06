package me.alfie.alfinolib.datapacks;

import me.alfie.alfinolib.AlfinoLib;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;

@Mod(value = AlfinoLib.MODID, dist = Dist.CLIENT)
public class ClientDatapackManager {

    private static DataMap dataMap = new DataMap(new HashMap<>());

    public static void setDataMap(DataMap newDataMap) {
        dataMap = newDataMap;
        NeoForge.EVENT_BUS.post(new ClientDatapackUpdatedEvent(Minecraft.getInstance().player));
    }

    public static void onServerLeave(ClientPlayerNetworkEvent.LoggingOut event) {
        Datapacks.LOGGER.info("Client disconnected, clearing client dataMap and unregistering DatapackRegistry");
        dataMap = new DataMap(new HashMap<>());
        DatapackRegistry.unregister();
    }

    public static <T> T get(DatapackKey<T> key) {
        return dataMap.get(key);
    }
}
