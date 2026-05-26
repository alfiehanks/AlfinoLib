package me.alfie.alfinolib.networking;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.networking.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * Main networking class
 */
public final class Networking {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(AlfinoLib.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int packetId = 0;

    public static <P extends NetworkPacket<P>> void sendToServer(P packet) {
        INSTANCE.sendToServer(packet);
    }

    public static <P extends NetworkPacket<P>> void sendToClient(ServerPlayer serverPlayer, P packet) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
    }

    //Packet StreamCodec must use FriendlyByteBuf
    static <P extends NetworkPacket<P>> void registerPacket(Class<P> type, StreamCodec<FriendlyByteBuf, P> codec) {
        PacketCodec<P, FriendlyByteBuf> packetCodec = PacketCodec.from(codec);

        INSTANCE.registerMessage(
                packetId++,
                type,
                packetCodec::encode,
                packetCodec::decode,
                Networking::handle);

        Networking.LOGGER.debug("Registered packet for {}", type);
    }

    private static <P extends NetworkPacket<P>> void handle(P packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(
                () -> packet.exec(contextSupplier.get())
        );
        contextSupplier.get().setPacketHandled(true);
    }

    /**
     * Forge expects the packet's codec as {@code <Packet, Buffer>}, however, StreamCodec uses {@code <Buffer, Packet>}
     * This interface is used internally to wrap the packet's StreamCodec.
     * @param <B>
     * @param <P>
     */
    private interface PacketCodec<P extends NetworkPacket<P>, B extends ByteBuf> {
        void encode(P packet, B buf);
        P decode(B buf);

        static <P extends NetworkPacket<P>, B extends ByteBuf> PacketCodec<P, B> from(StreamCodec<B, P> codec) {
            return new PacketCodec<>() {
                @Override public void encode(P packet, B buf) { codec.encode(buf, packet); }
                @Override public P decode(B buf) { return codec.decode(buf); }
            };
        }
    }

    //Setup

    /**
     * Fired when the mod loads - internal use only.
     * @param modEventBus
     */
    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(Networking::postNetworkRegisterEvent);
    }

    private static void postNetworkRegisterEvent(FMLCommonSetupEvent event) {
        Networking.LOGGER.debug("Listening for packet registrations...");
        ModLoader.get().postEvent(new NetworkRegisterEvent(Networking::registerPacket));
    }
}
