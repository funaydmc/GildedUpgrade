package me.funayd.gildedupgrade;

import me.funayd.gildedupgrade.Event.DisableEvent;
import me.funayd.gildedupgrade.Event.ItemUpdate;
import me.funayd.gildedupgrade.Upgrader.DefaultInventory;
import me.funayd.gildedupgrade.command.Commands;
import me.funayd.gildedupgrade.command.TabComplete;
import me.funayd.gildedupgrade.data.DataLoader;
import me.funayd.gildedupgrade.data.Lang;
import me.funayd.gildedupgrade.data.LicenseKey;
import me.funayd.gildedupgrade.data.config;
import me.funayd.gildedupgrade.util.JavaScript;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public final class GildedUpgrade extends JavaPlugin {

    private static GildedUpgrade instant;
    private static Economy econ;
    private static String key;
    private static LicenseKey.KeyState active;
    private boolean loaded = false;
    private boolean checked = false;
    public static LicenseKey.KeyState getActive() {
        return active;
    }
    public static final JavaScript engine = new JavaScript();

    @Override
    public void onEnable() {
            instant = this;
            this.saveDefaultConfig();
            new Commands(instant);
            new TabComplete();
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this,this::load,0,12000);
    }
    public void load(){
        if (!loaded) {
            key = instant.getConfig().getString("license", "XXXX-XXXX-XXXX-XXXX");
            loadPlugin();
            createDefault();
            reload();
			Lang.load(this);
            Bukkit.getPluginManager().registerEvents(new ItemUpdate(), this);
            if (!setupEconomy()) config.usevaut = false;
            loaded = true;
        }
    }
    @Override
    public void onDisable() {
        Bukkit.getPluginManager().callEvent(new DisableEvent());
        if (getActive().equals(LicenseKey.KeyState.ACTIVE))
        LicenseKey.action(key, "disable");
    }
    private void loadPlugin(){
        if (key == null || key.equals("XXXX-XXXX-XXXX-XXXX")) {
            if (checked) return;
            Bukkit.getConsoleSender().sendMessage("§e" + LicenseKey.plugins + ": §eYou have not entered license key!");
            Bukkit.getConsoleSender().sendMessage("§e" + LicenseKey.plugins + ": §ePlease add it to the file config.yml");
            Bukkit.getConsoleSender().sendMessage("§e" + LicenseKey.plugins + ": §eThen reload the plugin.");
            active = LicenseKey.KeyState.NULL;
            checked = true;
            return;
        }
        if (LicenseKey.KeyStatus(key)) {
            if (LicenseKey.action(key, "enable")) {
                active = LicenseKey.KeyState.ACTIVE;
                Bukkit.getConsoleSender().sendMessage("§e" + LicenseKey.plugins + ": §aYour product key has been activated!");
                Bukkit.getConsoleSender().sendMessage("§e" + LicenseKey.plugins + ": §aThank you for using our product");
                checked = true;
            }
            else {
                active = LicenseKey.KeyState.OTHER_ACTIVE;
                if (checked) return;
                Bukkit.getConsoleSender().sendMessage("§e" + LicenseKey.plugins + ": §The key product you entered is already in use!");
                checked = true;
                return;
            }
        }
        else {
            active = LicenseKey.KeyState.INCORRECT;
            if (checked) return;
            Bukkit.getConsoleSender().sendMessage("§e" + LicenseKey.plugins + ": §cYour product key entered is incorrect");
            Bukkit.getConsoleSender().sendMessage("§e" + LicenseKey.plugins + ": §cPlugin has been disabled...");
            checked = true;
            return;
        }
        File aswf = new File(getDataFolder().getParentFile(), "\\AutoSaveWorld\\config.yml");
        if (aswf.exists()) {
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            try {
                yamlConfiguration.load(aswf);
            } catch ( Exception ignored ) {
            }
            if (yamlConfiguration.getBoolean("networkwatcher.mainthreadnetaccess.warn")) {
                yamlConfiguration.set("networkwatcher.mainthreadnetaccess.warn", false);
                try {
                    yamlConfiguration.save(aswf);
                } catch ( Exception ignored ) {
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "asw reload");
            }
        }

        checked = true;
    }
    public static void reload(){
        instant.reloadConfig();
        config.loadconfig();
        DataLoader.load();
    }
    public static GildedUpgrade getInstant() {
        return instant;
    }
    public static void createDefault(){
        File file = new File(instant.getDataFolder(),"generator");
        if (file.exists()) return;
        instant.saveResource("generator/Example.yml",false);
        instant.saveResource("HuongDanSuDung.txt",false);
        instant.saveResource("task.yml",false);
        instant.saveResource("lang.yml",false);
        instant.getConfig().set("gui_data", DefaultInventory.getBase64());
        instant.saveConfig();
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
        return econ != null;
    }
    public static Economy geteco(){
        return econ;
    }


}
