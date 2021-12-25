package me.funayd.gildedupgrade.data;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.contruct.GildedItem;
import me.funayd.gildedupgrade.contruct.Line;
import me.funayd.gildedupgrade.contruct.Tree;
import me.funayd.gildedupgrade.runtask.getTask;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class StorageManager {
    public static HashMap<String, GildedItem> items = new HashMap<>();
    public static HashMap<String, Line> lines = new HashMap<>();
    public static HashMap<String, Tree> trees = new HashMap<>();
    public static HashMap<String, getTask> task = new HashMap<>();
    public static void clear(){
        items.clear();
        lines.clear();
        trees.clear();
        task.clear();
    }

    public static YamlConfiguration taskconfig = YamlConfiguration.loadConfiguration(new File(GildedUpgrade.getInstance().getDataFolder(), "task.yml"));
}
