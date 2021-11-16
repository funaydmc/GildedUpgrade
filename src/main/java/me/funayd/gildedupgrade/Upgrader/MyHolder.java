package me.funayd.gildedupgrade.Upgrader;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class MyHolder implements InventoryHolder {
    private final Player player;
    private Inventory inventory;
    public MyHolder(Player player){
        this.player = player;
    }
    public InventoryView open(Inventory inv){
        this.inventory = inv;
        return player.openInventory(inv);
    }
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    public Player getPlayer() {
        return player;
    }
}
