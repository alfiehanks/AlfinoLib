package me.alfie.alfinolib.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.alfie.alfinolib.debug.RequestClientTestPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    private static final ArrayList<ModCommand> COMMANDS = new ArrayList<>();

    public static void init(IEventBus modEventBus) {
        MinecraftForge.EVENT_BUS.addListener(Commands::registerCommands);

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
