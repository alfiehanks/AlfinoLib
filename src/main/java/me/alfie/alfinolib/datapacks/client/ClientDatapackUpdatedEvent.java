package me.alfie.alfinolib.datapacks.client;

import net.minecraft.world.entity.player.Player;

/**
 * Event that fires after {@link ClientDatapackManager} has been set.
 * <br><br> This is the first moment where it is safe to access the datapack client-side.
 */
public class ClientDatapackUpdatedEvent extends net.neoforged.bus.api.Event {

    private final Player player;

    public ClientDatapackUpdatedEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}

