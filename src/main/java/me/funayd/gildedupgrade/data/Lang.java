package me.funayd.gildedupgrade.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Lang {

    private static List<String> help;
    private static String onlyPlayer;
    private static String noitem;

    public static void load(Plugin plugin){
        File file = new File(plugin.getDataFolder(),"lang.yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(file);
        help = c.getStringList("help");
        onlyPlayer = c.getString("onlyPlayer","only player");
        noitem = c.getString("noitem","noitem");
    }

    public static List<String> help(){
        return help;
    }
    public static String get(String s){
        if (Objects.equals(s, "onlyPlayer")) return onlyPlayer;
        if (Objects.equals(s, "noitem")) return noitem;
        else return "error lang.yml";
    }

}
