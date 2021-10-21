package me.justmicha.elementalapi.jda;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class JDACommand {

    public String label;
    public List<String> aliases;
    public Member member;
    public TextChannel channel;

    public JDACommand(String label, List<String> aliases) {
        this.label = label;
        this.aliases = aliases;
        this.aliases.add(label);
    }

    public abstract void onCommand(String label, TextChannel channel, Member member, String[] args);

    public void message(String message) {
        this.channel.sendMessage(message).queue();
    }

    public void typeMessage(String message) {
        this.channel.sendTyping().queue();
        this.channel.sendMessage(message).queue();
    }

    public void embedMessage(String title, String author, Color color, String... messages) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(title)
                .setAuthor(author)
                .setColor(color)
                .build();

        for (String message : Arrays.asList(messages))
            embed.getFields().add(new MessageEmbed.Field("", message, true));
        this.channel.sendMessageEmbeds(embed);
    }

    public void unregister(Map<String, JDACommand> commandMap) {
        commandMap.remove(getLabel());
    }

    /* Getters & Setters */
    public String getLabel() {
        return label;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Member getMember() {
        return member;
    }

    public TextChannel getChannel() {
        return channel;
    }
}
