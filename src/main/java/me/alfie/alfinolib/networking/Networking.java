package me.alfie.alfinolib.networking;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * Main networking class
 */
public final class Networking {

    private static final Logger LOGGER = LogUtils.getLogger();

    public enum Side {
        CLIENT,
        SERVER
    }

    public static <P extends NetworkPacket<P>> void sendToServer(P packet) {
        ClientPacketDistributor.sendToServer(packet);
    }

    public static <P extends NetworkPacket<P>> void sendToClient(ServerPlayer serverPlayer, P packet) {
        PacketDistributor.sendToPlayer(serverPlayer, packet);
    }

    //Packet StreamCodec must use RegistryFriendlyByteBuf
    static <P extends NetworkPacket<P>> void registerPacket(Side playToSide,
                                                            CustomPacketPayload.Type<P> type,
                                                            StreamCodec<RegistryFriendlyByteBuf, P> codec,
                                                            PayloadRegistrar registrar) {
        switch (playToSide) {
            case CLIENT -> registrar.playToClient(
                    type, codec.toMinecraftStreamCodec(),
                    NetworkPacket::exec);

            case SERVER -> registrar.playToServer(
                    type, codec.toMinecraftStreamCodec(),
                    NetworkPacket::exec);
        }

        Networking.LOGGER.info("Registered packet for {} on {}", type.id(), playToSide.name());
    }

    //Setup
    /**
     * Fired when the mod loads - internal use only.
     * @param modEventBus
     */
    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(Networking::postNetworkRegisterEvent);
    }

    private static void postNetworkRegisterEvent(RegisterPayloadHandlersEvent event) {
        Networking.LOGGER.debug("Listening for packet registrations...");
        ModLoader.postEvent(new NetworkRegisterEvent(Networking::registerPacket, event.registrar("1")));
    }
}
