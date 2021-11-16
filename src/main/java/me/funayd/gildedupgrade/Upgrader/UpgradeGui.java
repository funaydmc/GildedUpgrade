package me.funayd.gildedupgrade.Upgrader;

import me.funayd.gildedupgrade.Hook.VaultEco;
import me.funayd.gildedupgrade.contruct.GildedItem;
import me.funayd.gildedupgrade.data.config;
import me.funayd.gildedupgrade.util.Color;
import me.funayd.gildedupgrade.util.NBTutil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UpgradeGui{

    private final Inventory invFrame = config.getInv();
    private final HashMap<Integer, ItemStack> item = new HashMap<>();
    private final GUI gui;

    public UpgradeGui(GUI gui) {
        this.gui = gui;
        int size = invFrame.getSize();
        for (int i = 0; i < size; i++) item.put(i, invFrame.getItem(i));
    }

    public int socketSize() {
        return (int) item.values().stream().filter(a -> Objects.equals(NBTutil.get(a, "GU-gui"), "socket")).count();
    }
    public List<Integer> itemSlots() {
        List<Integer> l = new ArrayList<>();
        item.forEach((k, v) -> {
            if (Objects.equals(NBTutil.get(v, "GU-gui"), "item")) l.add(k);
        });
        return l;
    }
    public List<Integer> socketSlots() {
        List<Integer> l = new ArrayList<>();
        item.forEach((k, v) -> {
            if (Objects.equals(NBTutil.get(v, "GU-gui"), "socket")) l.add(k);
        });
        return l;
    }
    public List<Integer> confirmSlots() {
        List<Integer> l = new ArrayList<>();
        item.forEach((k, v) -> {
            if (Objects.equals(NBTutil.get(v, "GU-gui"), "confirm")) l.add(k);
        });
        return l;
    }
    public ItemStack[] parse() {
        InputHandler handler = gui.handler;
        ItemStack[] content = invFrame.getContents().clone();
        GildedItem ii = handler.handler.upgradeitem;
        ItemStack item = (ii == null) ? null : ii.getItem();
        List<ItemStack> socket = handler.handler.socket;
        if (item != null)
            for (int i = 0; i < content.length; i++) {
                if (Objects.equals(NBTutil.get(content[i], "GU-gui"), "item")) {
                    content[i] = item;
                    break;
                }
            }
        for (ItemStack it : socket) {
            if (it == null || it.getType() == Material.AIR) continue;
            for (int i = 0; i < content.length; i++) {
                if (Objects.equals(NBTutil.get(content[i], "GU-gui"), "socket")) {
                    content[i] = it;
                    break;
                }
            }
        }
        for (int a=0;a<content.length;a++){
            if (content[a]==null) continue;
            ItemStack i = content[a].clone();
            ItemMeta meta = i.getItemMeta();
            if (meta==null) continue;
            String name = filter(meta.getDisplayName());
            List<String> lore = meta.getLore();
            if (lore!=null) {
                for (String l : lore) {
                    lore.set(lore.indexOf(l), filter(l));;
                }
                meta.setLore(lore);
            }
            meta.setDisplayName(name);
            i.setItemMeta(meta);
            content[a]=i;
        }
        return content;
    }
    public Inventory getInventory() {
        InventoryHolder holder = gui.holder;
        Inventory inv = Bukkit.createInventory(holder, invFrame.getSize(), config.inv_name);
        inv.setContents(parse());
        return inv;
    }
    private String filter(String string) {
        if (string==null) return null;
        if (string.equals(" ")) return " ";
        return string.replace("%cost%",cost()).replace("%chance%",chance());
    }
    private String cost(){
        double cost = Math.round(gui.handler.handler.cost()*100)/100D;
        if (cost> VaultEco.getbal(gui.owner))
            return Color.vanilla("&c"+cost);
        return Color.vanilla("&a"+cost);
    }
    private String chance() {
        return String.valueOf(gui.handler.getchance());
    }
}

