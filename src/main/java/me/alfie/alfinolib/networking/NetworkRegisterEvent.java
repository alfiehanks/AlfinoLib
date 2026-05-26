package me.alfie.alfinolib.networking;

import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Event to register packets. Posted during FMLCommonSetupEvent
 */
public class NetworkRegisterEvent extends Event implements IModBusEvent {

    private final PacketRegistrar registrar;
    private final PayloadRegistrar payloadRegistrar;

    public NetworkRegisterEvent(PacketRegistrar registrar, PayloadRegistrar payloadRegistrar) {
        this.registrar = registrar;
        this.payloadRegistrar = payloadRegistrar;
    }

    public <P extends NetworkPacket<P>> void register(Networking.Side playToSide,
                                                      CustomPacketPayload.Type<P> type,
                                                      StreamCodec<RegistryFriendlyByteBuf, P> codec) {
        registrar.register(playToSide, type, codec, payloadRegistrar);
    }

    @FunctionalInterface
    public interface PacketRegistrar {
        <P extends NetworkPacket<P>> void register(Networking.Side playToSide,
                                                   CustomPacketPayload.Type<P> type,
                                                   StreamCodec<RegistryFriendlyByteBuf, P> codec,
                                                   PayloadRegistrar payloadRegistrar);
    }


}
