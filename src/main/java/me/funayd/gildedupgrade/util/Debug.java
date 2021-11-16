package me.funayd.gildedupgrade.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Debug {

    public static boolean debugmode = false;

    public static void log(String log){
        String msg = ChatColor.translateAlternateColorCodes('&',"&7[&eGU-Debug&7]&e  "+log);
        if (debugmode)
        Bukkit.getLogger().info(msg);
    }

}
