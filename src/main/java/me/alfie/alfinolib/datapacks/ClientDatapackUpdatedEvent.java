package me.alfie.alfinolib.datapacks;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event that fires after {@link ClientDatapackManager} has been set.
 * <br><br> This is the first moment where it is safe to access the datapack client-side.
 */
public class ClientDatapackUpdatedEvent extends Event {

    private final Player player;

    public ClientDatapackUpdatedEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}

