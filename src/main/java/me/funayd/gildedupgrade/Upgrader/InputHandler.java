package me.funayd.gildedupgrade.Upgrader;

import me.funayd.gildedupgrade.Hook.VaultEco;
import me.funayd.gildedupgrade.contruct.GildedItem;
import me.funayd.gildedupgrade.data.config;
import me.funayd.gildedupgrade.runtask.getTask;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class InputHandler {

    Player player;
    UpgradeHandler handler;
    boolean sucess = true;
    boolean process = false;
    public InputHandler(Player player,int maxSocket){
        this.player = player;
        this.handler = new UpgradeHandler(maxSocket);
    }
    public String   addall(ItemStack item){
        if (item==null||item.getType().toString().equals("AIR")) return "null";
        if (handler.isupgradeable(item)) return additem(item);
        if (handler.isSocket(item))      return addsocket(item);
        return "null";
    }
    public String   additem(ItemStack item){
        if (handler.upgradeitem!=null)
        return "null";

        handler.setUpgradeitem(item.clone());
        item.setAmount(item.getAmount()-1);
        return "item";
    }
    public String   addsocket(ItemStack item){
        handler.addSocket(item);    // try to add socket to handler
        if (handler.Excess.equals(item))     // if excess is equals with item, it's mean no item added
            return "null";

        if (item.isSimilar(handler.Excess)){        // if excess just similar, it's mean some item was added
            item.setAmount(handler.Excess.getAmount());   // set amount as excess number (take)
            handler.Excess = new ItemStack(Material.AIR);      // reset excess
            return "socket";
        }
        item.setAmount(0);      // if excess is default, it's mean all item was added -> take item
        return "socket";
    }

    public float            getchance(){
       return Math.round(handler.chance() * 100) / 100F;
    }

    public ItemStack        takeitem(){
        if (handler.upgradeitem == null) return new ItemStack(Material.AIR);
        ItemStack returnitem = handler.upgradeitem.getItem();
        handler.upgradeitem = null;
        return returnitem;
    }

    public ItemStack        takesocket(int slot){
        if (handler.socket.size()==0) return new ItemStack(Material.AIR);
        if (slot>=handler.socket.size()) return new ItemStack(Material.AIR);
        ItemStack item = handler.socket.get(slot);
        if (item==null) return new ItemStack(Material.AIR);
        handler.socket.set(slot,null);
        return item;
    }
    public List<ItemStack>  takesocket(){
        return handler.socket;
    }
    public ItemStack        upgrade(){
        if (config.usevaut) {
            double cost = handler.cost();
            if (VaultEco.getbal(player)<cost)
                return null;
            VaultEco.take(player,cost);
        }
        GildedItem resurt = handler.upgrade();
        u = resurt;
        sucess = handler.success;
        process = true;
        dotask();
        if (resurt!=null)
        return resurt.getItem();
        else return new ItemStack(Material.AIR);
    }
    GildedItem u;
    private void dotask(){
        if (u==null) return;
        HashMap<String, getTask> t = u.getLine().getTask();
        if (t==null) return;
        int level = u.getLine().getItems().indexOf(u)+1;
        String task = String.valueOf(sucess);
        if (t.containsKey(task+level)) task += level;
        t.get(task).excute(player);
    }

}
