package me.alfie.alfinolib.debug;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.datapacks.server.ServerDatapackManager;
import me.alfie.alfinolib.debug.datapack.TestData;
import me.alfie.alfinolib.debug.datapack.TestDatapack;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class DebugCommands {

    private static LiteralArgumentBuilder<CommandSourceStack> simpleCommand(String commandName, int permission,
                                                                           Command<CommandSourceStack> executor) {
        return Commands.literal(AlfinoLib.MODID).then(Commands.literal(commandName)
                .requires(source -> source.hasPermission(permission))
                .executes(executor));
    }

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        registerTestDatapack(dispatcher);
    }

    private static void registerTestDatapack(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                simpleCommand(
                        "testDatapack",
                        4,
                        context -> {

                            //TestData clientTest = ClientDatapackManager.get(TestDatapack.KEY);
                            TestData serverTest = ServerDatapackManager.get(TestDatapack.KEY);

                           // context.getSource().sendSystemMessage(Component.literal("Client: " + clientTest.testString() + " " + clientTest.testInt())
                            //        .withStyle(ChatFormatting.GREEN));
                            context.getSource().sendSystemMessage(Component.literal("Server: " + serverTest.testString() + " " + serverTest.testInt())
                                    .withStyle(ChatFormatting.AQUA));

                            return 1;
                        }
                )
        );
    }

}
