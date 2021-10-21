package me.justmicha.elementalapi.storage;

import lombok.SneakyThrows;
import me.justmicha.elementalapi.utils.Common;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;

public class JsonConfiguration {

    private String name;
    private File file;
    private JSONObject json;

    public JsonConfiguration(String parentFolder, String name) {
        this.name = name.endsWith(".json") ? name : name + ".json";
        this.file = new File(Common.getPlugin().getDataFolder().getAbsolutePath() + File.separator + parentFolder, this.name);
        this.json = new JSONObject("{}");
        verify();
    }

    public JsonConfiguration(String name) {
        this.name = name.endsWith(".json") ? name : name + ".json";
        this.file = new File(Common.getPlugin().getDataFolder(), this.name);
        this.json = new JSONObject("{}");
        verify();
    }

    @SneakyThrows
    public void verify() {
        if (!this.file.exists()) {
            this.file.mkdir();
            this.file.createNewFile();
        }
        else {
            this.json = new JSONObject(new JSONTokener(new BufferedReader(new FileReader(file))));
        }
    }

    @SneakyThrows
    public void save() {
        BufferedWriter writer = Files.newBufferedWriter(this.file.toPath());
        writer.write(json.toString());
        writer.close();
    }

    public void set(String key, Object value) {
        this.json.put(key, value);
        save();
    }

    public Object get(String key) {
        return this.json.get(key);
    }

    public String getString(String key) {
        return this.json.getString(key);
    }

    public int getInt(String key) {
        return this.json.getInt(key);
    }

    public double getDouble(String key) {
        return this.json.getDouble(key);
    }

    public float getFloat(String key) {
        return this.json.getFloat(key);
    }

    public long getLong(String key) {
        return this.json.getLong(key);
    }

    public boolean getBoolean(String key) {
        return this.json.getBoolean(key);
    }

    public JSONObject getJsonObject(String key) {
        return this.json.getJSONObject(key);
    }

    public JSONObject getJson() {
        return json;
    }
}
