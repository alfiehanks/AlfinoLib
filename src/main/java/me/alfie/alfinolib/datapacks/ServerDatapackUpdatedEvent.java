package me.alfie.alfinolib.datapacks;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event that fires after {@link ServerDatapackManager} finishes pulling data and has built the {@link DataMap}.
 * <br><br> This is the first moment where it is safe to access the datapack server-side.
 */
public class ServerDatapackUpdatedEvent extends Event {

    private final MinecraftServer server;

    public ServerDatapackUpdatedEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }
}
