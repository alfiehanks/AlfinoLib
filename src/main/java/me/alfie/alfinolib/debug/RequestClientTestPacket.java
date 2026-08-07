package me.alfie.alfinolib.debug;

import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.datapacks.DataMap;
import me.alfie.alfinolib.datapacks.SyncClientDatapackPacket;
import me.alfie.alfinolib.datapacks.client.ClientDatapackManager;
import me.alfie.alfinolib.datapacks.server.ServerDatapackManager;
import me.alfie.alfinolib.debug.datapack.TestData;
import me.alfie.alfinolib.debug.datapack.TestDatapack;
import me.alfie.alfinolib.networking.NetworkPacket;
import me.alfie.alfinolib.networking.NetworkRegisterEvent;
import me.alfie.alfinolib.networking.Networking;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import me.alfie.alfinolib.networking.codec.StreamCodecBuilder;
import me.alfie.alfinolib.util.ResourceId;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestClientTestPacket() implements NetworkPacket<RequestClientTestPacket> {

    public static final Type<RequestClientTestPacket> TYPE = new Type<>(
            new ResourceId(AlfinoLib.MODID, "request_client_test").mc());
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestClientTestPacket> STREAM_CODEC =
            StreamCodec.unit(new RequestClientTestPacket());

    @Override
    public void exec(IPayloadContext context) {
        String testString = ClientDatapackManager.get(TestDatapack.KEY).testString();
        testString = testString.replace("%s", "client");
        context.player().sendSystemMessage(Component.literal(testString));
    }

    public static void register(NetworkRegisterEvent event) {
        event.register(Networking.Side.CLIENT, RequestClientTestPacket.TYPE, RequestClientTestPacket.STREAM_CODEC);
    }
}
