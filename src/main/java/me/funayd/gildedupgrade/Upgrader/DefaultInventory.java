package me.funayd.gildedupgrade.Upgrader;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.util.Color;
import me.funayd.gildedupgrade.util.NBTutil;
import me.funayd.gildedupgrade.util.XMaterial;
import me.funayd.gildedupgrade.util.byteConvert;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultInventory {
    private static final int upgradeItemSlot = 10;
    private static final int confirm = 19;
    private static final List<Integer> socket = Arrays.asList(
            12,13,14,15,16,
            21,22,23,24,25
    );
    private static ItemStack filler(){
        ItemStack item = XMaterial.GLASS_PANE.parseItem();
        ItemMeta im = item.getItemMeta();
        assert im != null;
        im.setDisplayName(" ");
        item.setItemMeta(im);
        return item;
    }
    private static ItemStack item(){
        ItemStack item = XMaterial.BLUE_STAINED_GLASS_PANE.parseItem();
        ItemMeta im = item.getItemMeta();
        assert im != null;
        im.setDisplayName(Color.vanilla("&6Item"));
        List<String> lore = Arrays.asList(" ", Color.vanilla("&7Click on Item in the inventory to select"));
        im.setLore(lore);
        item.setItemMeta(im);
        NBTutil.set(item,"GU-gui","item");
        return item;
    }
    private static ItemStack confirm(){
        ItemStack item = XMaterial.GREEN_STAINED_GLASS_PANE.parseItem();
        ItemMeta im = item.getItemMeta();
        assert im != null;
        im.setDisplayName(Color.vanilla("&6Confirm"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(Color.vanilla("&aChance: %chance%"));
        lore.add(Color.vanilla("&aCost: %cost%"));
        lore.add(Color.vanilla("&aClick to confirm"));
        im.setLore(lore);
        item.setItemMeta(im);
        NBTutil.set(item,"GU-gui","confirm");
        return item;
    }
    private static ItemStack socket(){
        ItemStack item = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
        ItemMeta im = item.getItemMeta();
        assert im != null;
        im.setDisplayName(Color.vanilla("&6Socket"));
        List<String> lore = Arrays.asList(" ", Color.vanilla("&7Click on socket in the inventory to select"));
        im.setLore(lore);
        item.setItemMeta(im);
        NBTutil.set(item,"GU-gui","socket");
        return item;
    }
    public static Inventory INV(){
        Inventory inv = Bukkit.createInventory(null,36, Color.vanilla("&6Gilded Upgrade"));
        for (int i=0;i<36;i++) inv.setItem(i,filler());
        for (int i : socket)   inv.setItem(i,socket());
        inv.setItem(upgradeItemSlot,item());
        inv.setItem(confirm,confirm());
        return inv;
    }
    public static String getBase64(){
        return byteConvert.inventoryToString(INV());
    }
    public static Inventory inventoryParse(String base64){
        return byteConvert.stringToInventory(base64);
    }
}
