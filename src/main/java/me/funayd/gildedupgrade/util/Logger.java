package me.funayd.gildedupgrade.util;

import org.bukkit.Bukkit;

public class Logger {

    public static void Info(String logger) {
        Bukkit.getLogger().info(Color.vanilla("[GU-inf0] "+logger));
    }
    public static void warn(String logger) {
        Bukkit.getLogger().warning(Color.vanilla("&c[GU-warn] "+logger));
    }
    @SuppressWarnings("unused")
    public static void Default(String logger) {
        Bukkit.getLogger().info(Color.vanilla("[Gilded-Upgrade] "+logger));
    }

}
