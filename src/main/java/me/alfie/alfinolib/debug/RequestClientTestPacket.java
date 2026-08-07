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
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

public record RequestClientTestPacket() implements NetworkPacket<RequestClientTestPacket> {

    public static final StreamCodec<FriendlyByteBuf, RequestClientTestPacket> STREAM_CODEC =
            StreamCodec.unit(new RequestClientTestPacket());

    @Override
    public void exec(NetworkEvent.Context context) {
        String testString = ClientDatapackManager.get(TestDatapack.KEY).testString();
        testString = testString.replace("%s", "client");

        Minecraft.getInstance().player.sendSystemMessage(Component.literal(testString));
    }

    public static void register(NetworkRegisterEvent event) {
        event.register(RequestClientTestPacket.class, RequestClientTestPacket.STREAM_CODEC);
    }
}
