package me.justmicha.elementalapi.command;

import me.justmicha.elementalapi.exception.SimpleException;
import me.justmicha.elementalapi.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class GroupCommand extends Command {

    private CommandSender sender;
    private String label;
    private String[] args;

    protected GroupCommand(String label) {
        super(label, Common.colorize("&7No Description"), Common.colorize("&c/" + label), Arrays.asList(label.split("\\|")));
        setPermission(Common.getPlugin().getName() + ".command." + getClass().getSimpleName().toLowerCase());
        setPermissionMessage(Common.colorize("&cYou are missing permission '" + getPermission() + "'."));
    }

    public abstract void onCommand();

    public void checkCase(String subCommand, SubCommand command) {
        if (args[0].equalsIgnoreCase(subCommand)) {
            command.label = this.label;
            command.sender = this.sender;
            command.args = this.args;
            command.onCommand(label, sender, args);
        }
    }

    @Override @Deprecated
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {

        this.sender = commandSender;
        this.label = s;
        this.args = strings;

        try { onCommand(); }
        catch (SimpleException ignored) {}

        return true;
    }
}
