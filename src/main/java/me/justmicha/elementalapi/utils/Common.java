package me.justmicha.elementalapi.utils;

import lombok.SneakyThrows;
import me.justmicha.elementalapi.plugin.SimplePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class Common {

    public static final String PLAYER_DB_ENDPOINT = "https://playerdb.co/api/player/minecraft/";

    private static Map<String, BukkitTask> activeTimerTasks = new HashMap<>();


    /* String & Message Utils */
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /* NBT Utils */
    public static NamespacedKey createKey(String key) {
        return new NamespacedKey(getPlugin(), key);
    }

    public static ItemStack assignKey(ItemStack itemStack, String key, String value) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            container.set(createKey(key), PersistentDataType.STRING, value);
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public static String retrieveValue(ItemStack itemStack, String key) {
        String value = null;
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            if (container.has(createKey(key), PersistentDataType.STRING)) {
                value = container.get(createKey(key), PersistentDataType.STRING);
            }
        }

        return value;
    }

    public static boolean hasKey(ItemStack itemStack, String key) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            return container.has(createKey(key), PersistentDataType.STRING);
        }

        return false;
    }

    /* Bukkit Tasks */
    public static BukkitTask runTaskLater(long delayTicks, Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(getPlugin(), delayTicks);
    }

    public static BukkitTask runTaskTimer(String timerId, long delayTicks, Runnable runnable) {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskTimer(getPlugin(), 0, delayTicks);

        activeTimerTasks.put(timerId, task);
        return task;
    }

    public static BukkitTask runAsync(Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(getPlugin());
    }

    public static void cancelTimer(String timerId) {
        if (activeTimerTasks.containsKey(timerId))
            activeTimerTasks.get(timerId).cancel();
    }


    /* Miscellaneous Tools */
    public static void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(l -> Bukkit.getPluginManager().registerEvents(l, getPlugin()));
    }

    /* Common Player Utils */
    public static UUID getOfflineUUID(String username) {
        AtomicReference<JSONObject> data = null;
        runAsync(() -> data.set(WebClient.get(Common.PLAYER_DB_ENDPOINT + username, "").getJSONObject("data")));
        return UUID.fromString(data.get().getJSONObject("player").getString("id"));
    }

    public static void msg(Player player, String message) {
        player.sendMessage(colorize(message));
    }

    /* Logging */
    public static void info(String... info) {
        Arrays.asList(info).forEach(i -> Bukkit.getLogger().log(Level.INFO, getPrefix() + " " + i));
    }

    public static void warn(String... warnings) {
        Arrays.asList(warnings).forEach(w -> Bukkit.getLogger().log(Level.WARNING, getPrefix() + " " + w));
    }

    public static void error(String... errors) {
        Arrays.asList(errors).forEach(e -> Bukkit.getLogger().log(Level.SEVERE, getPrefix() + " " + e));
    }

    /* Getters */
    public static Plugin getPlugin() {
        return SimplePlugin.getPlugin();
    }

    public static File getDataFolder() {
        return getPlugin().getDataFolder();
    }

    public static String getDataPath() {
        return getDataFolder().getAbsolutePath();
    }

    public static SimplePlugin getSimplePlugin() {
        return SimplePlugin.getInstance();
    }

    public static String getPrefix() {
        if (getPlugin().getConfig().getString("prefix") != null)
            return getPlugin().getConfig().getString("prefix");
        return "[" + getPlugin().getName() + "]";
    }

    @SneakyThrows
    public static CommandMap getCommandMap() {
        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        return (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
    }
}
