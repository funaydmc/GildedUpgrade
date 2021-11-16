package me.funayd.gildedupgrade.Upgrader;

import me.funayd.gildedupgrade.Event.DisableEvent;
import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.data.config;
import me.funayd.gildedupgrade.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Objects;

public class GUI implements Listener {
    final Player owner;
    final InputHandler handler;
    final MyHolder holder;
    final Plugin plugin;
    final UpgradeGui guiHandler = new UpgradeGui(this);

    private final List<Integer> item;
    private final List<Integer> socket;
    private final List<Integer> confirm;

    public GUI(Player player, Plugin plugin){
        this.plugin = plugin;
        this.owner = player;
        this.holder = new MyHolder(player);
        int max = guiHandler.socketSize();
        item = guiHandler.itemSlots();
        socket = guiHandler.socketSlots();
        confirm = guiHandler.confirmSlots();
        handler = new InputHandler(player,max);
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }
    public void open(){
        holder.open(guiHandler.getInventory());
    }
    public void reload(Inventory inv){
        inv.setContents(guiHandler.parse());
    }

    @EventHandler
    public void onDisable(DisableEvent e){
        owner.closeInventory();
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if (e.getPlayer()!=owner) return;
        if (e.getInventory().getHolder()!=holder) return;
        if (!handler.process)
        {
            Inventory inv = e.getPlayer().getInventory();

            ItemStack item = handler.takeitem();
            if (item != null) inv.addItem(item);
            handler.takesocket().stream().filter
                    (Objects::nonNull).forEachOrdered(inv::addItem);
        }
        else
        {
            if (handler.sucess && config.reopensucess) reopen((long) config.reopenSucessDelay);
            if (!handler.sucess && config.reopenfailed) reopen((long) config.reopenFailedDelay);
        }
        HandlerList.unregisterAll(this);
    }
    private void reopen(long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                GildedUpgrade.getInstant(),()
                -> new GUI(owner, plugin).open(),
                delay);
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (e.getPlayer()==owner) {
            owner.sendMessage("Your Gui Listener is still active");
            owner.sendMessage("please send this error to admin");
        }
        HandlerList.unregisterAll(this);
    }
    @EventHandler
    public void onclick(InventoryClickEvent e){
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (e.getInventory().getHolder()!=holder) return;
        e.setCancelled(true);

        int slot = e.getRawSlot();  if (slot<0) return;
        ItemStack item = e.getCurrentItem();
        if (item==null||item.getType()== Material.AIR) return;
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();

        if (this.item.contains(slot)) takeitem(p,inv);
        if (this.confirm.contains(slot)) upgradeConfirm(p);
        if (socket.contains(slot)) takesocket(p,inv,slot);
        if (slot>=54) {
            ItemStack itemclone = item.clone();
            itemclone.setAmount(1);
            ItemStack itemclone1 = item.clone();
            itemclone1.setAmount(1);
            if (!Objects.equals(handler.addall(itemclone), "null"))
            item.setAmount(item.getAmount()-1);
        }
        reload(inv);
    }
    private void takeitem(Player p,Inventory inv){
        p.getInventory().addItem(handler.takeitem());
        reload(inv);
    }
    private void takesocket(Player p,Inventory inv,int slot){
        p.getInventory().addItem(handler.takesocket(socket.indexOf(slot)));
        reload(inv);
    }
    private void upgradeConfirm(Player p){
        if (handler.handler.upgradeitem==null||handler.handler.socket.size()==0) return;
        ItemStack u = handler.upgrade();
        if (u==null){
            owner.sendMessage(Color.vanilla("&cYou not have enough money to do that"));
            handler.process = false;
            owner.closeInventory();
            return;
        }
        p.getInventory().addItem(u);
        p.closeInventory();
    }
}
