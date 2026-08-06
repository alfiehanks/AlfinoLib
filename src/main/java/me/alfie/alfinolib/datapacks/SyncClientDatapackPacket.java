package me.alfie.alfinolib.datapacks;

import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.datapacks.client.ClientDatapackManager;
import me.alfie.alfinolib.networking.NetworkPacket;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import me.alfie.alfinolib.networking.codec.StreamCodecBuilder;
import me.alfie.alfinolib.util.ResourceId;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncClientDatapackPacket(DataMap dataMap) implements NetworkPacket<SyncClientDatapackPacket> {

    public static final Type<SyncClientDatapackPacket> TYPE = new Type<>(
            new ResourceId(AlfinoLib.MODID, "sync_client_datapack").mc());
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncClientDatapackPacket> STREAM_CODEC =
            StreamCodecBuilder.<RegistryFriendlyByteBuf, SyncClientDatapackPacket>create()
                    .add(DataMap.STREAM_CODEC, SyncClientDatapackPacket::dataMap)
                    .build(SyncClientDatapackPacket::new);

    @Override
    public void exec(IPayloadContext context) {
        ClientDatapackManager.setDataMap(dataMap());
        Datapacks.LOGGER.info("Received sync packet on client, updated ClientDatapackManager.");
    }
}
