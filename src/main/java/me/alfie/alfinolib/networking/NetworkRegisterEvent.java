package me.alfie.alfinolib.networking;

import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

/**
 * Event to register packets. Posted during FMLCommonSetupEvent
 */
public class NetworkRegisterEvent extends Event implements IModBusEvent {

    private final PacketRegistrar registrar;

    public NetworkRegisterEvent(PacketRegistrar registrar) {
        this.registrar = registrar;
    }

    public <P extends NetworkPacket<P>> void register(Class<P> type, StreamCodec<FriendlyByteBuf, P> codec) {
        registrar.register(type, codec);
    }

    @FunctionalInterface
    public interface PacketRegistrar {
        <P extends NetworkPacket<P>> void register(Class<P> type, StreamCodec<FriendlyByteBuf, P> codec);
    }


}
