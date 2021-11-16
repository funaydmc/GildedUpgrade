package me.funayd.gildedupgrade.Upgrader.gui_customizer;

import me.funayd.gildedupgrade.Event.DisableEvent;
import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.Upgrader.MyHolder;
import me.funayd.gildedupgrade.util.Color;
import me.funayd.gildedupgrade.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class ConfirmGui implements Listener {
    final Player owner;
    final MyHolder holder;
    final Plugin plugin;
    final CustomGui customGui;

    public ConfirmGui(CustomGui gui){
        this.plugin = gui.plugin;
        this.owner = gui.owner;
        this.holder = new MyHolder(owner);
        customGui = gui;
        holder.open(getInventory());
        Bukkit.getPluginManager().registerEvents(this,gui.plugin);
    }
    public void open(){
    }
    private Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(holder,9, ChatColor.RED+
                "Do you want to save these changes?");

        ItemStack no = XMaterial.RED_STAINED_GLASS_PANE.parseItem();
        ItemMeta im1 = no.getItemMeta(); assert im1 != null;
        im1.setDisplayName(Color.vanilla("&cNO"));
        List<String> lore1 = Collections.singletonList(Color.vanilla("&eDon't save this change"));
        im1.setLore(lore1); no.setItemMeta(im1);

        ItemStack yes = XMaterial.BLUE_STAINED_GLASS_PANE.parseItem();
        ItemMeta im2 = yes.getItemMeta(); assert im2 != null;
        im2.setDisplayName(Color.vanilla("&bYes"));
        List<String> lore2 = Collections.singletonList(Color.vanilla("&eYes, save my change"));
        im2.setLore(lore2); yes.setItemMeta(im2);

        ItemStack sure = XMaterial.GREEN_STAINED_GLASS_PANE.parseItem();
        ItemMeta im3 = sure.getItemMeta(); assert im3 != null;
        im3.setDisplayName(Color.vanilla("&aChắc chắn rồi!"));
        List<String> lore3 = Collections.singletonList(Color.vanilla("&aSao lại không nhỉ?"));
        im3.setLore(lore3); sure.setItemMeta(im3);

        inv.setItem(2,no);
        inv.setItem(4,yes);
        inv.setItem(6,sure);
        return inv;
    }
    @EventHandler
    public void Click(InventoryClickEvent e){
        if (e.getWhoClicked()!=owner) return;
        if (e.getInventory().getHolder()!=holder) return;
        e.setCancelled(true);
        if (e.getRawSlot()==2){
            e.getWhoClicked().closeInventory();
        }
        if (e.getRawSlot()==4||e.getRawSlot()==6){
            confirm();
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(ChatColor.GREEN+"đã lưu thay đổi. /gu reload để cập nhật thay đổi ");
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if (e.getPlayer()!=owner) return;
        if (e.getInventory().getHolder()!=holder) return;
        HandlerList.unregisterAll(this);
    }
    private void confirm(){
        customGui.handle.save();
    }
    @EventHandler
    public void onDisable(DisableEvent e){
        owner.closeInventory();
    }
}
