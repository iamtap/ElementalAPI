package me.justmicha.elementalapi.command;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    public CommandSender sender;
    public String label;
    public String[] args;

    public abstract void onCommand(String label, CommandSender sender, String[] args);
}
