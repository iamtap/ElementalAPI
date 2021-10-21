package me.justmicha.elementalapi.plugin;

import lombok.SneakyThrows;
import me.justmicha.elementalapi.command.SimpleCommand;
import me.justmicha.elementalapi.jda.JDACommand;
import me.justmicha.elementalapi.jda.JDAHandler;
import me.justmicha.elementalapi.storage.YamlConfiguration;
import me.justmicha.elementalapi.utils.Common;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class SimplePlugin extends JavaPlugin {

    protected static JDA jda = null;

    private static SimplePlugin instance;
    private static Plugin plugin;

    private static Map<String, YamlConfiguration> configurations;
    private static Map<String, SimpleCommand> simpleCommandMap;

    public void init() {
    }

    public abstract void onStart();

    public abstract void onStop();

    @Override
    @Deprecated
    public void onEnable() {
        /* Initialization */
        instance = this;
        plugin = this;
        configurations = new HashMap<>();
        simpleCommandMap = new HashMap<>();

        /* Start Methods */
        init();
        onStart();
    }

    @Override
    @Deprecated
    public void onDisable() {
        if (jda != null)
            jda.shutdownNow();

        onStop();
    }

    @SneakyThrows
    public void defaultJDA(String token) {
        jda = JDABuilder.createDefault(token).build();
    }

    /**
     * Uses the default plugin.yml for command information.
     */
    public void registerCommand(String label, CommandExecutor executor) {
        getCommand(label).setExecutor(executor);
    }


    public void registerJDACommands(JDACommand... commands) {
        JDAHandler.registerCommands(commands);
    }

    public void registerEvents(Listener... listeners) {
        Common.registerEvents(listeners);
    }

    public void registerJDAEvents(ListenerAdapter... listeners) {
        jda.addEventListener(listeners);
    }

    /**
     * Registers a command to the CommandMap without the plugin definition file (plugin.yml).
     */
    public void registerToMap(SimpleCommand simpleCommand) {
        CommandMap commandMap = Common.getCommandMap();
        Command command = commandMap.getCommand(simpleCommand.getName());

        if (command == null)
            return;

        if (command.isRegistered())
            command.unregister(commandMap);

        // TODO add tab-completion

        simpleCommandMap.put(simpleCommand.getName(), simpleCommand);
        commandMap.register(simpleCommand.getLabel(), simpleCommand);
    }

    @Override
    public void saveDefaultConfig() {
        configurations.put("config.yml", new YamlConfiguration("config.yml", true));
    }

    @Override
    @Deprecated
    public FileConfiguration getConfig() {
        return super.getConfig();
    }


    public YamlConfiguration getDefaultConfig() {
        return new YamlConfiguration("config.yml", true);
    }

    public static SimplePlugin getInstance() {
        return instance;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static Map<String, YamlConfiguration> getConfigurations() {
        return configurations;
    }

    public static Map<String, SimpleCommand> getSimpleCommandMap() {
        return simpleCommandMap;
    }

    public static JDA getJda() {
        return jda;
    }
}
