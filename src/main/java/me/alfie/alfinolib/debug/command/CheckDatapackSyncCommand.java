package me.alfie.alfinolib.debug.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.alfie.alfinolib.AlfinoLib;
import me.alfie.alfinolib.commands.ModCommand;
import me.alfie.alfinolib.commands.PermissionLevel;
import me.alfie.alfinolib.datapacks.client.ClientDatapackManager;
import me.alfie.alfinolib.datapacks.server.ServerDatapackManager;
import me.alfie.alfinolib.debug.RequestClientTestPacket;
import me.alfie.alfinolib.debug.datapack.TestDatapack;
import me.alfie.alfinolib.networking.Networking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.Permissions;

public class CheckDatapackSyncCommand implements ModCommand {

    @Override
    public void build(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(AlfinoLib.MODID)
                        .requires(Commands.hasPermission(new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER)))
                        .then(Commands.literal("debug")
                            .then(Commands.literal("checkDatapackSync")
                                    .then(Commands.argument("side", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            builder.suggest("server");
                                            builder.suggest("client");
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            String side = StringArgumentType.getString(context, "side");

                                            String testString = "";
                                            switch (side) {
                                                case "server":
                                                    testString = ServerDatapackManager.get(TestDatapack.KEY).testString();
                                                    testString = testString.replace("%s", "server");
                                                    context.getSource().sendSystemMessage(Component.literal(testString));
                                                    break;
                                                case "client":
                                                    if(context.getSource().isPlayer()) {
                                                        Networking.sendToClient(context.getSource().getPlayer(), new RequestClientTestPacket());
                                                    } else {
                                                        context.getSource().sendFailure(Component.literal("Cannot run client test on a server!"));
                                                        return 0;
                                                    }

                                                    break;
                                                default:
                                                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                                                            .dispatcherUnknownArgument()
                                                            .create();
                                            }



                                            return 1;
                                        })))
                ));
    }
}
