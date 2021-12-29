package me.funayd.gildedupgrade.data;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.util.Logger;
import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public enum YamlFile {
    ITEMS("storage/items.yml"),
    LINES("storage/lines.yml");

    private final String name;
    private FileConfiguration config;
    private File file;
    private final Plugin plugin = GildedUpgrade.getInstance();

    YamlFile(String name) {
        this.name = name;
        create();
    }

    private void create() {
        file = new File(plugin.getDataFolder(), getName());
        saveDefault();
        reload();
    }

    public String getName() {
        return name;
    }

    public void save() {
        try {
            get().save(file);
            System.out.println("saving "+name);
        }catch (IOException e) {
            Logger.Info("&eCould not save config to " + file);
        }
    }

    public void saveDefault() {
        if (!file.exists()) {
            plugin.saveResource(getName(), false);
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);

        final InputStream configStream = plugin.getResource(getName());
        if (configStream == null)  return;
        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(configStream, Charsets.UTF_8)));
    }

    public FileConfiguration get() {
        if (config == null) reload();
        return config;
    }
}
