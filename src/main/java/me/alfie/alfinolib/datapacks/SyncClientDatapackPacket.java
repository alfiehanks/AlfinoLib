package me.alfie.alfinolib.datapacks;

import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.datapacks.client.ClientDatapackManager;
import me.alfie.alfinolib.networking.NetworkPacket;
import me.alfie.alfinolib.networking.NetworkRegisterEvent;
import me.alfie.alfinolib.networking.Networking;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import me.alfie.alfinolib.networking.codec.StreamCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SyncClientDatapackPacket(DataMap dataMap) implements NetworkPacket<SyncClientDatapackPacket> {

    public static final StreamCodec<FriendlyByteBuf, SyncClientDatapackPacket> STREAM_CODEC =
            StreamCodecBuilder.<FriendlyByteBuf, SyncClientDatapackPacket>create()
                    .add(DataMap.STREAM_CODEC, SyncClientDatapackPacket::dataMap)
                    .build(SyncClientDatapackPacket::new);

    @Override
    public void exec(NetworkEvent.Context context) {
        ClientDatapackManager.setDataMap(dataMap());
        Datapacks.LOGGER.info("Received sync packet on client, updated ClientDatapackManager.");
    }

    public static void register(NetworkRegisterEvent event) {
        event.register(SyncClientDatapackPacket.class, SyncClientDatapackPacket.STREAM_CODEC);
    }
}
