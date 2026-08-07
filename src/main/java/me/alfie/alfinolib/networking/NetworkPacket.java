package me.alfie.alfinolib.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface NetworkPacket<P extends CustomPacketPayload> extends CustomPacketPayload {

    void exec(IPayloadContext context);

}
