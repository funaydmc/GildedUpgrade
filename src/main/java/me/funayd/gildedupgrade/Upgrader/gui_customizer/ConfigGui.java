package me.funayd.gildedupgrade.Upgrader.gui_customizer;

import me.funayd.gildedupgrade.Event.DisableEvent;
import me.funayd.gildedupgrade.Upgrader.MyHolder;
import me.funayd.gildedupgrade.util.Color;
import me.funayd.gildedupgrade.util.NBTutil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ConfigGui implements Listener {
    final Player owner;
    final MyHolder holder;
    final Plugin plugin;
    final CustomGui customGui;
    public ItemStack item;

    public ConfigGui(CustomGui gui, ItemStack currentItem){
        this.plugin = gui.plugin;
        this.owner = gui.owner;
        this.holder = new MyHolder(owner);
        this.item = currentItem;
        customGui = gui;
        open();
    }
    private Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(holder,18, ChatColor.BLACK+ "Option ?");
        ItemStack item = item(Material.DIAMOND_SWORD,"upgrade item slot");
        ItemStack socket = item(Material.DIAMOND,"socket slot");
        ItemStack filler = item(Material.WATER_BUCKET,"filler slot");
        ItemStack confirm = item(Material.SLIME_BALL,"confirm slot");
        ItemStack name = item(Material.NAME_TAG,"Gui's display name"," ",ChatColor.GRAY+customGui.handle.getName()," ", ChatColor.YELLOW +"click to change");
        ItemStack size = item(Material.SHEARS, "Gui's size",ChatColor.AQUA+String.valueOf(customGui.handle.getRow())," ",ChatColor.YELLOW+"click to change");
        if (this.item!=null) {
            if (NBTutil.get(this.item, "GU-gui") == null) {
                setGlowing(filler);
            } else switch (NBTutil.get(this.item, "GU-gui")) {
                case "item":
                    setGlowing(item);
                    break;
                case "socket":
                    setGlowing(socket);
                    break;
                case "confirm":
                    setGlowing(confirm);
                    break;
            }

            inv.setItem(1, item);
            inv.setItem(2, socket);
            inv.setItem(10, filler);
            inv.setItem(11, confirm);
        }
        inv.setItem(6,name);
        inv.setItem(15,size);

        return inv;
    }

    String stringInput;
    int intInput=0;
    public void open(){
        if (stringInput!=null){
            setName(stringInput);
            stringInput = null;
        }
        if (intInput!=0){
            customGui.handle.setRow(intInput);
            intInput = 0;
        }
        holder.open(getInventory());
        Bukkit.getPluginManager().registerEvents(this,customGui.plugin);
    }
    private ItemStack item(Material material, String name,String... lore){
        ItemStack sure = new ItemStack(material);
        ItemMeta im3 = sure.getItemMeta(); assert im3 != null;
        if (name!=null)
        im3.setDisplayName(Color.vanilla(name));
        if (lore!=null)
        im3.setLore(Arrays.asList(lore));
        sure.setItemMeta(im3);
        return sure;
    }
    private void setGlowing(ItemStack item){
        item.addUnsafeEnchantment(Enchantment.DURABILITY,10);
    }
    private void setType(String type){
        item = NBTutil.set(item,"GU-gui",(type.equals("filler"))?null:type);
    }
    private void setName(String name){
        customGui.handle.setName(name);
    }
    private void reload(Inventory inv){
        inv.setContents(getInventory().getContents());
    }

    @EventHandler
    public void Click(InventoryClickEvent e){
        if (e.getWhoClicked()!=owner) return;
        if (e.getInventory().getHolder()!=holder) return;
        e.setCancelled(true);
        switch (e.getRawSlot()){
            case 1:setType("item"); reload(e.getInventory()); break;
            case 2:setType("socket");reload(e.getInventory()); break;
            case 10:setType("filler");reload(e.getInventory()); break;
            case 11:setType("confirm");reload(e.getInventory()); break;
            case 6:
                temp = true;
                owner.closeInventory();
                Bukkit.getPluginManager().registerEvents(new GetInput(owner,this),plugin);
                break;
            case 15:
                temp = true;
                owner.closeInventory();
                Bukkit.getPluginManager().registerEvents(new GetSize(this),plugin);
                break;
        }
        reload(e.getInventory());
    }
    boolean temp = false;
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if (e.getPlayer()!=owner) return;
        if (e.getInventory().getHolder()!=holder) return;
        HandlerList.unregisterAll(this);
        if (temp) temp = false;
        else Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, customGui::open, 3);
    }
    @EventHandler
    public void onDisable(DisableEvent e){
        owner.closeInventory();
    }
}
