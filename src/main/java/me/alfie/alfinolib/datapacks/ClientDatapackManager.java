package me.alfie.alfinolib.datapacks;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;

public class ClientDatapackManager {

    private static DataMap dataMap = new DataMap(new HashMap<>());

    public static void setDataMap(DataMap newDataMap) {
        dataMap = newDataMap;
        MinecraftForge.EVENT_BUS.post(new ClientDatapackUpdatedEvent(Minecraft.getInstance().player));
    }

    public static <T> T get(DatapackKey<T> key) {
        return dataMap.get(key);
    }
}
