package me.funayd.gildedupgrade.Event;

import me.funayd.gildedupgrade.contruct.GildedItem;
import me.funayd.gildedupgrade.data.StorageManager;
import me.funayd.gildedupgrade.data.config;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import me.funayd.gildedupgrade.util.Debug;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemUpdate implements Listener {

    @EventHandler
    public void onclick(InventoryClickEvent e){
        if (!config.autoupdate) {
            HandlerList.unregisterAll(this);
            return;
        }
        update((Player) e.getWhoClicked());
    }


    public static void update(Player p){
        Inventory inv = p.getInventory();
        for (int i = 0; i<41; i++){
            ItemStack item = inv.getItem(i);
            if (isUpdated(item)) continue;
            inv.setItem(i,update(item));
            Debug.log("Updated item for"+p.getName());
        }
    }

    public static boolean isUpdated(ItemStack item){
        if (item==null||item.getType().toString().equals("AIR")) return true;

        NBTItem nbt = new NBTItem(item);
        if (nbt.getKeys().size()==0) return true;
        if (!nbt.hasKey("GUpgrade")) return true;
        String id = nbt.getCompound("GUpgrade").getString("id");
        if (!StorageManager.items.containsKey(id)) return true;
        int version = nbt.getCompound("GUpgrade").getInteger("version");
        GildedItem systemitem = StorageManager.items.get(id);
        return version == systemitem.getVersion();
    }
    public static ItemStack update(ItemStack item){
        NBTItem nbt = new NBTItem(item);
        String id = nbt.getCompound("GUpgrade").getString("id");
        GildedItem systemitem = StorageManager.items.get(id);
        Debug.log("update item "+id);
        return systemitem.getItem();
    }


}
