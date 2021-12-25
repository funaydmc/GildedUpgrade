package me.funayd.gildedupgrade.util;

import me.funayd.gildedupgrade.GildedUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class Registor {
    public static void listener(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, GildedUpgrade.getInstance());
    }
}
