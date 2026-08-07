package me.alfie.alfinolib.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public interface ModCommand {

    void build(CommandDispatcher<CommandSourceStack> dispatcher);
}
