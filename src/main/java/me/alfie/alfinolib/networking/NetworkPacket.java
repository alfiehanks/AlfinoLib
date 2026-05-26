package me.alfie.alfinolib.networking;

import net.minecraftforge.network.NetworkEvent;

public interface NetworkPacket<P> {

    void exec(NetworkEvent.Context context);
}
