package me.justmicha.elementalapi.storage;

import lombok.SneakyThrows;
import me.justmicha.elementalapi.plugin.SimplePlugin;
import me.justmicha.elementalapi.utils.Common;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

public class YamlConfiguration {

    protected String name;
    protected File file;
    protected FileConfiguration configuration;

    public YamlConfiguration(String parentFolder, String name, boolean saveDefaults) {
        this.name = name.endsWith(".yml") ? name : name + ".yml";
        this.file = new File(Common.getPlugin().getDataFolder().getAbsolutePath() + File.separator + parentFolder, this.name);
        this.configuration = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(this.file);

        if (saveDefaults) {
            Common.getPlugin().saveResource(this.name, false);
        } else {
            verify();
        }

        SimplePlugin.getConfigurations().put(this.name, this);
    }

    public YamlConfiguration(String name, boolean saveDefaults) {
        this.name = name.endsWith(".yml") ? name : name + ".yml";
        this.file = new File(Common.getPlugin().getDataFolder(), this.name);
        this.configuration = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(this.file);

        if (saveDefaults && !file.exists()) { // prevent warnings
            Common.getPlugin().saveResource(this.name, false);
        } else {
            verify();
        }

        SimplePlugin.getConfigurations().put(this.name, this);
    }

    @SneakyThrows
    public void save() {
        this.configuration.save(file);
    }

    public void set(String path, Object value) {
        this.configuration.set(path, value);
        save();
    }

    public Object get(String path) {
        return this.configuration.get(path);
    }

    public String getString(String path) {
        return this.configuration.getString(path);
    }

    public int getInt(String path) {
        return this.configuration.getInt(path);
    }

    public double getDouble(String path) {
        return this.configuration.getDouble(path);
    }

    public boolean getBoolean(String path) {
        return this.configuration.getBoolean(path);
    }

    public List<?> getList(String path) {
        return this.configuration.getList(path);
    }

    public List<String> getStringList(String path) {
        return this.configuration.getStringList(path);
    }

    @SneakyThrows
    public void verify() {
        if (!this.file.exists()) {
            file.mkdir();
            file.createNewFile();
        }
    }

    public void reload() {
        this.configuration = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(this.file);
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getFileConfiguration() {
        return configuration;
    }

}
