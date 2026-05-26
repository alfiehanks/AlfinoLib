package me.alfie.alfinolib.datapacks;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;

public class ClientDatapackManager {

    private static DataMap dataMap = new DataMap(new HashMap<>());

    public static void setDataMap(DataMap newDataMap) {
        dataMap = newDataMap;
        NeoForge.EVENT_BUS.post(new ClientDatapackUpdatedEvent(Minecraft.getInstance().player));
    }

    public static <T> T get(DatapackKey<T> key) {
        return dataMap.get(key);
    }
}
