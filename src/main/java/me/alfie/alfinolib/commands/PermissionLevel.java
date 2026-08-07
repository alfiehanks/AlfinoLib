package me.alfie.alfinolib.commands;

public enum PermissionLevel {
    /**All players can run command*/
    ALL,

    /**Can bypass spawn protection, but no extra commands*/
    MODERATOR,

    /**Can use command blocks, i.e /clear, /difficulty, /effect, /gamemode*/
    GAMEMASTER,

    /**Multiplayer management commands, i.e /ban, /op, /kick*/
    ADMIN,

    /**All commands, including server management, i.e /stop*/
    OWNER
}
