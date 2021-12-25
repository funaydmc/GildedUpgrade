package me.funayd.gildedupgrade;

import me.funayd.gildedupgrade.Event.DisableEvent;
import me.funayd.gildedupgrade.Event.ItemUpdate;
import me.funayd.gildedupgrade.Upgrader.DefaultInventory;
import me.funayd.gildedupgrade.command.Commands;
import me.funayd.gildedupgrade.command.TabComplete;
import me.funayd.gildedupgrade.data.*;
import me.funayd.gildedupgrade.nbtapi.utils.MinecraftVersion;
import me.funayd.gildedupgrade.util.OtherUtill;
import me.funayd.gildedupgrade.util.JavaScript;
import me.funayd.gildedupgrade.util.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;


public final class GildedUpgrade extends JavaPlugin {

    public static OtherUtill activator;

    private static GildedUpgrade instance;
    private static Economy econ;
    public static final JavaScript engine = new JavaScript();
    @Override
    public void onEnable() {
        instance = this;
        MinecraftVersion.getVersion();
        this.saveDefaultConfig();
        new Commands(instance);
        new TabComplete();
        createDefault();
        reload();
        Lang.load(this);
        if (!setupEconomy()) config.usevaut = false;
        Bukkit.getPluginManager().registerEvents(new ItemUpdate(), this);

    }
    @Override
    public void onDisable() {
        Bukkit.getPluginManager().callEvent(new DisableEvent());
        activator.disable();
    }
    public static void reload(){
        instance.reloadConfig();
        String key = instance.getConfig().getString("license", "XXXX-XXXX-XXXX-XXXX");
        activator = new OtherUtill(key);
        StorageManager.clear();
        config.loadconfig();
        DataLoader.load();
        YamlFile.ITEMS.reload();
        YamlFile.LINES.reload();
        Bukkit.getPluginManager().callEvent(new DisableEvent());
    }
    public static GildedUpgrade getInstance() {
        return instance;
    }
    public static void createDefault(){
        File file = new File(instance.getDataFolder(),"generator");
        if (file.exists()) return;
        instance.saveResource("generator/Example.yml",false);
        instance.saveResource("HuongDanSuDung.txt",false);
        instance.saveResource("task.yml",false);
        instance.saveResource("lang.yml",false);
        instance.saveResource("gui.yml",false);
        YamlConfiguration a = guiConfig();
        a.set("gui_data", DefaultInventory.getBase64());
        saveGui(a);
    }
    public static YamlConfiguration guiConfig(){
        return YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(),"gui.yml"));
    }
    public static void saveGui(YamlConfiguration a){
        try {
            a.save(new File(instance.getDataFolder(),"gui.yml"));
            Logger.Default("saved default gui to gui.yml");
        } catch ( IOException e ) {
            Logger.Default("Error when try to save default gui config");
        }
    }
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }
    public static Economy geteco(){
        return econ;
    }

    public static OtherUtill getActivator() {
        return activator;
    }
}
