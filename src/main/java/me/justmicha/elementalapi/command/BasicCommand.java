package me.justmicha.elementalapi.command;

import me.justmicha.elementalapi.exception.SimpleException;
import me.justmicha.elementalapi.menu.Menu;
import me.justmicha.elementalapi.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class BasicCommand implements CommandExecutor {

    protected CommandSender sender;
    protected Command command;
    protected String label;
    protected String[] args;

    public abstract void onCommand();

    @Override
    @Deprecated
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        this.sender = commandSender;
        this.command = command;
        this.label = s;
        this.args = strings;

        try { onCommand(); }
        catch (SimpleException ignored) {}

        return true;
    }

    /**
     * Interrupts the execution of the command.
     */
    public void end() throws SimpleException {
        throw new SimpleException();
    }

    /**
     * Interrupts the execution of the command.
     *
     * @param message Sent to the sender when before execution ends.
     */
    public void end(String message) {
        sender.sendMessage(Common.colorize(message));
        end();
    }

    /**
     * @return {@code true} if CommandSender is a player.
     * Returns: {@code false} if the sender is not a player.
     */
    public boolean isPlayer() {
        return (sender instanceof Player);
    }

    /**
     * Stops command execution for console.
     */
    public void checkConsole() {
        if (!isPlayer())
            end("&cYou must be a player to run this command.");
    }

    /**
     * Send a message to the sending user.
     *
     * @param message The colorized message to be sent to the sender.
     */
    public void msg(String message) {
        sender.sendMessage(Common.colorize(message));
    }

    /**
     * Displays a menu on a players screen.
     *
     * @param menu The menu to display.
     */
    public void displayMenu(Menu menu) {
        getPlayer().openInventory(menu.getInventory());
    }

    /**
     * Automatically blocks console from running past this point.
     *
     * @return The sending player.
     */
    public Player getPlayer() {
        checkConsole();
        return (Player) sender;
    }

    /**
     * Grabs the targeted player from currently
     * online players.
     *
     * @param username The username of the targeting player.
     * @return The targeted player.
     */
    public Player getTarget(String username) {
        Player target = Bukkit.getPlayerExact(username);
        if (target == null)
            end("&cPlayer '" + username + "' could not be found.");
        return target;
    }

    /**
     * Grabs a player's UUID from the internet and
     * uses it to search the local storage.
     *
     * @param username The username of the requested target.
     * @return The targeted player.
     */
    public Player getTargetOnline(String username) {
        Common.runAsync(() -> {
            Player onlineTarget = Bukkit.getPlayer(Common.getOfflineUUID(username));

            if (onlineTarget == null)
                end("&cPlayer '" + username + "' could not be found.");
        });

        return null;
    }
}
