package me.alfie.alfinolib.datapacks;

import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.networking.NetworkPacket;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import me.alfie.alfinolib.networking.codec.StreamCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncClientDatapackPacket(DataMap dataMap) implements NetworkPacket<SyncClientDatapackPacket> {

    public static final Type<SyncClientDatapackPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(AlfinoLib.MODID, "sync_client_datapack"));
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncClientDatapackPacket> STREAM_CODEC =
            StreamCodecBuilder.<RegistryFriendlyByteBuf, SyncClientDatapackPacket>create()
                    .add(DataMap.STREAM_CODEC, SyncClientDatapackPacket::dataMap)
                    .build(values -> new SyncClientDatapackPacket((DataMap) values.get(0)));

    @Override
    public void exec(IPayloadContext context) {
        ClientDatapackManager.setDataMap(dataMap());
        Datapacks.LOGGER.info("Received sync packet on client, updated ClientDatapackManager.");
    }

}
