package me.funayd.gildedupgrade.data;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.Upgrader.DefaultInventory;
import me.funayd.gildedupgrade.util.Color;
import me.funayd.gildedupgrade.util.Debug;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.io.File;

public class config {

    public static boolean autoupdate;
    public static boolean reopenfailed;
    public static double reopenFailedDelay;
    public static boolean reopensucess;
    public static double reopenSucessDelay;
    public static boolean usevaut;
    public static double costmodifier;
    public static String inv_name;
    private static Inventory inv;

    public static void loadconfig(){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(
        new File(GildedUpgrade.getInstant().getDataFolder(),"config.yml"));

        autoupdate = config.getBoolean("auto_update",true);
        reopenfailed = config.getBoolean("reopen_when_failed",false);
        reopenFailedDelay = config.getDouble("reopen_when_failed_delay",10D);
        reopensucess = config.getBoolean("reopen_when_sucess",false);
        reopenSucessDelay = config.getDouble("reopen_when_sucess_delay",10D);
        Debug.debugmode = config.getBoolean("debug",false);
        usevaut = config.getBoolean("use_vault",false);
        costmodifier = config.getDouble("cost_modifier",1);
        inv_name =  Color.vanilla(config.getString("gui_name","&aGilded Upgrade"));
        inv = DefaultInventory.inventoryParse(config.getString("gui_data"));
    }

    public static Inventory getInv(){
        if (inv==null) inv = DefaultInventory.INV();
        return inv;
    }

}
