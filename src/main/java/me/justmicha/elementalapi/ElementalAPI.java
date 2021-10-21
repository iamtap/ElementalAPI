package me.justmicha.elementalapi;

import me.justmicha.elementalapi.jda.JDAHandler;
import me.justmicha.elementalapi.menu.MenuHandler;
import me.justmicha.elementalapi.utils.Common;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElementalAPI extends JavaPlugin implements Listener {

    // TODO create advanced stacks (custom items)

    @Override
    public void onEnable() {

        /* Registering API Events */
        Common.registerEvents(new MenuHandler(), new JDAHandler(), this);
    }
}
