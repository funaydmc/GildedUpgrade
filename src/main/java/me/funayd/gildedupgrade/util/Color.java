package me.funayd.gildedupgrade.util;

import org.bukkit.ChatColor;

public class Color {
    public static String vanilla(String message) {
        if (message==null) return null;
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
