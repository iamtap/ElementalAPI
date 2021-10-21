package me.justmicha.elementalapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.LinkedList;

public class GlobalScoreboard {

    private static Scoreboard scoreboard;
    private static LinkedList<String> content;

    public static void setTitle(String title) {
        getScoreboard().getObjective("globalScoreboard").setDisplayName(Common.colorize(title));
    }

    public static void setScore(int score, String value) {
        getScoreboard().getObjective("globalScoreboard").getScore(Common.colorize(value)).setScore(score);
    }

    public static void setContent(LinkedList<String> content) {
        for (int i = 0; i < content.size(); i++)
            getScoreboard().getObjective("globalScoreboard").getScore(content.get(i)).setScore(i);
    }

    public static void setContent(String... content) {
        for (int i = 0; i < content.length; i++)
            getScoreboard().getObjective("globalScoreboard").getScore(content[i]).setScore(i);
    }

    public static void setScoreboard(Scoreboard scoreboard) {
        GlobalScoreboard.scoreboard = scoreboard;
    }

    public static void displayAll() {
        Bukkit.getOnlinePlayers().forEach(GlobalScoreboard::display);
    }

    public static void display(Player player) {
        if (player == null)
            return;
        player.setScoreboard(getScoreboard());
    }

    public static Scoreboard getScoreboard() {

        if (scoreboard == null) {
            content = new LinkedList<>();
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("globalScoreboard", "dummy", Common.colorize("&e&l" + Common.getPlugin().getName()));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        return scoreboard;
    }
}
