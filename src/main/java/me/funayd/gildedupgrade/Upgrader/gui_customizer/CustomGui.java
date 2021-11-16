package me.funayd.gildedupgrade.Upgrader.gui_customizer;

import me.funayd.gildedupgrade.Event.DisableEvent;
import me.funayd.gildedupgrade.Upgrader.MyHolder;
import me.funayd.gildedupgrade.data.config;
import me.funayd.gildedupgrade.util.Cloner;
import me.funayd.gildedupgrade.util.Color;
import me.funayd.gildedupgrade.util.NBTutil;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomGui implements Listener {
    final Player owner;
    final MyHolder holder;
    final Plugin plugin;
    final CustomizerHandle handle;
    static final String pre = ChatColor.GRAY + "//slot type: ";

    public CustomGui(Player player, Plugin plugin){
        this.plugin = plugin;
        this.owner = player;
        this.holder = new MyHolder(player);
        this.handle = new CustomizerHandle();
        handle.setInventory(getInventory());
        updateInventory(handle.getInventory());
        open();
    }
    public void open(){
        holder.open(handle.getInventory());
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }
    private @NotNull Inventory getInventory(){
        Inventory clone = Bukkit.createInventory(holder,handle.getRow(), Color.vanilla("&6Shift+Right click to config"));
        clone.setContents(Cloner.contentClone(config.getInv().getContents()));
        return clone;
    }
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getWhoClicked()!=owner) return;
        if (e.getInventory().getHolder()!=holder) return;
        handle.setInventory(e.getInventory());
        updateInventory(e.getInventory());
        if (e.getClick().isRightClick()&&e.getClick().isShiftClick()){
            HandlerList.unregisterAll(this);
            e.getWhoClicked().closeInventory();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,()->
            new ConfigGui(this,e.getCurrentItem()),3);
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if (e.getPlayer()!=owner) return;
        if (e.getInventory().getHolder()!=holder) return;
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,()->new ConfirmGui(this).open(),2);
    }
    @EventHandler
    public void onDisable(DisableEvent e){
        owner.closeInventory();
    }
    public void updateInventory(Inventory inv){
        if (inv==null) return;
        ItemStack[] content = inv.getContents();
        for (ItemStack i:content){
            showType(i);
        }
        inv.setContents(content);
    }
    public void showType(ItemStack item){
        if (item==null) return;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta==null?new ArrayList<>():(meta.getLore()==null)?new ArrayList<>():meta.getLore();
        String type = NBTutil.get(item,"GU-gui"); // item/socket/confirm/filler
        update(lore,type);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
    public static boolean hasShow(List<String> lore){
        if (lore==null||lore.size()<2) return false;
        return  (lore.get(lore.size()-1).startsWith(pre));
    }
    public void update(List<String> lore,String type){
        if (lore==null) lore = new ArrayList<>();
        if (type==null) type="filler";
        if (hasShow(lore)){
            lore.set(lore.size()-1,pre+type);
        }else{
            lore.add(" ");
            lore.add(pre+type);
        }
    }
}
