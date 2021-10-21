package me.justmicha.elementalapi.jda;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDAHandler extends ListenerAdapter implements Listener {

    public static final String CMD_PREFIX = "!";

    private static Map<String, JDACommand> jdaCommandMap;

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        if (event.getType().equals(ServerLoadEvent.LoadType.STARTUP))
            if (jdaCommandMap == null)
                jdaCommandMap = new HashMap<>();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String label = message.split(" ")[0];
        String[] args = Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length);

        for (JDACommand command : jdaCommandMap.values()) {
            if (command.getAliases().contains(label)) {
                command.onCommand(label, event.getChannel(), event.getMember(), args);
                break;
            }
        }

    }

    public static Map<String, JDACommand> getJdaCommandMap() {
        return jdaCommandMap;
    }

    public static void registerCommands(JDACommand... commands) {
        Arrays.asList(commands).forEach(c -> jdaCommandMap.put(CMD_PREFIX + c.getLabel(), c));
    }

    public static JDACommand getCommand(String label) {
        return jdaCommandMap.get(label);
    }
}
