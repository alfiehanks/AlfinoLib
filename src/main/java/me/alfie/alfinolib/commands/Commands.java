package me.alfie.alfinolib.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.alfie.alfinolib.datapacks.Datapacks;
import me.alfie.alfinolib.datapacks.SyncClientDatapackPacket;
import me.alfie.alfinolib.datapacks.client.ClientDatapackManager;
import me.alfie.alfinolib.datapacks.server.ServerDatapackManager;
import me.alfie.alfinolib.debug.RequestClientTestPacket;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    private static final ArrayList<ModCommand> COMMANDS = new ArrayList<>();

    public static void init(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(Commands::registerCommands);

        modEventBus.addListener(RequestClientTestPacket::register);
    }

    /**
     * Internal use only
     * @param event
     */
    private static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        for(ModCommand command : COMMANDS) {
            command.build(dispatcher);
        }
    }

    public static void register(ModCommand command) {
        COMMANDS.add(command);
    }

}
