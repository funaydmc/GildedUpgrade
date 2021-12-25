package me.funayd.gildedupgrade.Upgrader.gui_customizer;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.data.config;
import me.funayd.gildedupgrade.util.byteConvert;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomizerHandle {
    public CustomizerHandle(){
        load();
    }
    private Inventory inventory;
    private String name;

    public Inventory getInventory() {
        return inventory;
    }
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setRow(int size) {
        Inventory inv = Bukkit.createInventory(inventory.getHolder(),size,name);
        ItemStack[] content = Arrays.copyOfRange(inventory.getContents(),0,size);
        inv.setContents(content);
        setInventory(inv);
    }
    public int getRow() {
        return config.getInv().getSize();
    }
    public void load(){
        setInventory(config.getInv());
        setName(config.inv_name);
    }
    public void remo(){
        ItemStack[] itemStacks = inventory.getContents();
        for (ItemStack it:itemStacks){
            if (it==null) continue;
            if (it.getItemMeta()==null) continue;
            if (it.getItemMeta().getLore()==null) continue;
            ItemMeta im = it.getItemMeta();
            List<String> lore = im.getLore();
            if (!CustomGui.hasShow(lore)) continue;
            if (lore.size()<2) continue;
            lore.remove(lore.size()-1);
            lore.remove(lore.size()-1);
            im.setLore(lore);
            it.setItemMeta(im);
        }
        inventory.setContents(itemStacks);
    }
    public void save(){
        File cFile = new File(GildedUpgrade.getInstance().getDataFolder(),"gui.yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(cFile);
        remo();
        c.set("gui_data", byteConvert.inventoryToString(inventory));
        c.set("gui_name",name);
        try {
            c.save(cFile);
        } catch ( IOException e ) {
            System.out.println("Error when trying to save config");
        }
    }


}
