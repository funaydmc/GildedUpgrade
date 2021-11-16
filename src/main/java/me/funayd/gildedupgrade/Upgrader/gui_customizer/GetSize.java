package me.funayd.gildedupgrade.Upgrader.gui_customizer;

import me.funayd.gildedupgrade.Event.DisableEvent;
import me.funayd.gildedupgrade.Upgrader.MyHolder;
import me.funayd.gildedupgrade.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class GetSize implements Listener {
        final Player owner;
        final MyHolder holder;
        final Plugin plugin;
    private final ConfigGui gui;

    public GetSize(ConfigGui gui){
            this.plugin = gui.plugin;
            this.owner = gui.owner;
            this.holder = new MyHolder(owner);
            this.gui = gui;
            holder.open(getInventory());
        }
        private Inventory getInventory(){
            Inventory inv = Bukkit.createInventory(holder,27, ChatColor.BLACK+ "Option ?");

            ItemStack s9 = item(Material.GHAST_TEAR,Color.vanilla("&e9"),9);
            ItemStack s18 = item(Material.GHAST_TEAR,Color.vanilla("&e18"),18);
            ItemStack s27 = item(Material.GHAST_TEAR,Color.vanilla("&e27"),27);
            ItemStack s36 = item(Material.GHAST_TEAR,Color.vanilla("&e36"),36);
            ItemStack s45 = item(Material.GHAST_TEAR,Color.vanilla("&e45"),45);
            ItemStack s54 = item(Material.GHAST_TEAR,Color.vanilla("&e54"),54);
            ItemStack cancel = item(Material.REDSTONE,Color.vanilla("&cCancel"),1);

            inv.setItem(10,s9);
            inv.setItem(11,s18);
            inv.setItem(12,s27);
            inv.setItem(13,s36);
            inv.setItem(14,s45);
            inv.setItem(15,s54);
            inv.setItem(16,cancel);

            return inv;
        }
        private ItemStack item(Material material, @Nullable String name, int amount){
            ItemStack sure = new ItemStack(material);
            ItemMeta im3 = sure.getItemMeta(); assert im3 != null;
            if (name!=null)
                im3.setDisplayName(Color.vanilla(name));
            sure.setItemMeta(im3);
            sure.setAmount(amount);
            return sure;
        }

        @EventHandler
        public void Click(InventoryClickEvent e){
            if (e.getWhoClicked()!=owner) return;
            if (e.getInventory().getHolder()!=holder) return;
            e.setCancelled(true);
            if (e.getRawSlot()<27){
                if (e.getCurrentItem()==null) return;
                if (e.getRawSlot()==16) {
                    close();
                    return;
                }
                gui.intInput = e.getCurrentItem().getAmount();
                close();
            }
        }
        @EventHandler
        public void onClose(InventoryCloseEvent e){
            if (e.getPlayer()!=owner) return;
            if (e.getInventory().getHolder()!=holder) return;
            close();
        }
        @EventHandler
        public void onDisable(DisableEvent e){
            owner.closeInventory();
        }
        public void close(){
            HandlerList.unregisterAll(this);
            Bukkit.getScheduler().runTask(plugin, owner::closeInventory);
            Bukkit.getScheduler().runTask(plugin, gui::open);
        }

}
