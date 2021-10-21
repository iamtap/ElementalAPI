package me.justmicha.elementalapi.storage;

import me.justmicha.elementalapi.utils.Common;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.entity.Player;

import java.io.File;

public class UserConfiguration extends YamlConfiguration {

    private String userId;
    private UserType userType;

    /* Constructors */
    public UserConfiguration(Player player) {
        super(Common.getDataPath() + File.separator + "minecraftUsers", player.getUniqueId().toString(), false);
        this.userId = player.getUniqueId().toString();
        this.userType = UserType.MINECRAFT;
    }

    public UserConfiguration(Member member) {
        super(Common.getDataPath() + File.separator + "discordUsers", member.getId(), false);
        this.userId = member.getId();
        this.userType = UserType.DISCORD;
    }

    /* Getters & Setters */
    public String getUserId() {
        return userId;
    }

    public UserType getUserType() {
        return userType;
    }
}

enum UserType {
    MINECRAFT,
    DISCORD
}
