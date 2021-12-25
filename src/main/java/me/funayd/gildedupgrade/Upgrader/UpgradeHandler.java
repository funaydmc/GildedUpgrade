package me.funayd.gildedupgrade.Upgrader;

import me.funayd.gildedupgrade.contruct.GildedItem;
import me.funayd.gildedupgrade.data.StorageManager;
import me.funayd.gildedupgrade.data.config;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UpgradeHandler {

    GildedItem upgradedItem;
    List<ItemStack> socket = new ArrayList<>();
    ItemStack       Excess = new ItemStack(Material.AIR);
    GildedItem upgradeitem = null;
    int             maxSocket = 14;
    ItemStack       ticket = null;
    boolean         success = false;

    public UpgradeHandler(int maxSocket){
        this.maxSocket = maxSocket;
    }

    public void setUpgradeitem(ItemStack upgradeitem){
        if (!isupgradeable(upgradeitem)) return;
        NBTItem nbt = new NBTItem(upgradeitem);
        String id = nbt.getCompound("GUpgrade").getString("id");
        this.upgradeitem = StorageManager.items.get(id);
    }
    public void addSocket     (ItemStack socket)     {
        if (!isSocket(socket))     return;
        Inventory inv = Bukkit.createInventory(null,27,"null");
        if (this.socket.size()!=0) {
            for (ItemStack it : this.socket) {
                if (it == null) continue;
                inv.addItem(it);
            }
        }
        inv.addItem(socket);
        this.socket = Arrays.asList(inv.getContents());
        if (this.socket.size()>maxSocket && this.socket.get(maxSocket)!=null) {
            Excess = this.socket.get(maxSocket);
            this.socket.set(maxSocket,null);
        }
        for (int stt = 0; stt < this.socket.size(); stt++){
            if (this.socket.get(stt)==null)
                this.socket.set(stt,null);
        }
    }

    public boolean isupgradeable(ItemStack upgradeitem) {
        NBTItem nbt = new NBTItem(upgradeitem);
        if (!nbt.hasKey("GUpgrade")) return false;
        String id = nbt.getCompound("GUpgrade").getString("id");
        if (!id.startsWith("it-")) return false;
        if (!StorageManager.items.containsKey(id)) return false;
        GildedItem info = StorageManager.items.get(id);
        return info.getNext().size() != 0;
    }
    public boolean isSocket     (ItemStack socket)      {
        NBTItem nbt = new NBTItem(socket);
        return nbt.getCompound("GUpgrade").hasKey("socket-value");
    }

    public GildedItem upgrade(){
        if (upgradeitem==null) return null;
        if (socket.size()==0) return upgradeitem;
        float chance = chance();
        List<GildedItem> nextlevel = upgradeitem.getNext();
        if (ticket!=null) {
            String ticketId = new NBTItem(ticket).getCompound("GUpgrade").getString("id");
            nextlevel.clear();
            nextlevel.add(StorageManager.items.get("it-"+ticketId.split("-",2)[1]));
        }
        float random = random(chance-100,chance);
        success = random>0;
        if (success) {
            if (nextlevel.size()==1) return nextlevel.get(0);
            GildedItem[] itemArr = new GildedItem[nextlevel.size()];
            int[] cound = new int[nextlevel.size()];
            for (GildedItem i : nextlevel) {
                int stt = nextlevel.indexOf(i);
                itemArr[stt] = i;
                cound[stt] = total(cound)+i.getValue();
            }
            int max = cound[cound.length-1];
            float r = random(0,max);
            int pre = 0;
            for (int i = 0; i<cound.length;i++ ){
                if (pre<r&&r<cound[i]) {
                    upgradedItem = itemArr[i];
                    break;
                }
                else pre = cound[i];
            }
        }
        else {
            if (upgradeitem.getLine()==null||!upgradeitem.getLine().isFallback())
            upgradedItem = upgradeitem;
            else upgradedItem = upgradeitem.getLine().getItems(0);
        }

        return upgradedItem;
    }

    private int total(int[] ints){
        return Arrays.stream(ints).sum();
    }
    private static float random(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }
    public double cost(){
        if (chance()==0) return 0;
        double modifier = config.costmodifier;
        if (ticket!=null) modifier *= 2;

        int uiv = upgradeitem.getValue();    //Upgrade_Item_Value
        float udv = 0;                       //UpgradeD_item_Value
        List<GildedItem> next = upgradeitem.getNext();
        for (GildedItem i : next)
            udv += i.getValue();
        if (next.size()!=0) udv /= next.size();

        if (ticket!=null) {
            String ticketId = new NBTItem(ticket).getCompound("GUpgrade").getString("id");
            udv = StorageManager.items.get("it-"+ticketId.split("-",2)[1]).getValue();
        }
        double n = (uiv+udv)/2;
        double x = 0;
        for (ItemStack so : socket) {
            if (so==null) continue;
            int amount = so.getAmount();
            for (int i = 0; i < amount; i++) {
                x += x * 0.2 + getvalue(so);
            }
        }

        return (x*2+n)*modifier;
    }
    public float chance(){
        if (upgradeitem==null) return 0;
        if (socket.size()==0)  return 0;
        int uiv = upgradeitem.getValue();    //Upgrade_Item_Value
        float udv = 0;                       //UpgradeD_item_Value
        List<GildedItem> next = upgradeitem.getNext();
        for (GildedItem i : next)
            udv += i.getValue();
        if (next.size()!=0) udv /= next.size();

        if (ticket!=null) {
            String ticketId = new NBTItem(ticket).getCompound("GUpgrade").getString("id");
            udv = StorageManager.items.get("it-"+ticketId.split("-",2)[1]).getValue();
        }
        float n = (uiv+udv)/2;
        float chance = 0;
        for (ItemStack so : socket) {
            if (so==null) continue;
            int amount = so.getAmount();
            for (int i = 0; i < amount; i++) {
                float x = getvalue(so);
                chance += (100-chance) * ( x/(x+n) );
            }
        }
        return chance;
    }

    private int getvalue(ItemStack itemStack){
        if (isSocket(itemStack)) return new NBTItem(itemStack).getCompound("GUpgrade").getInteger("socket-value");
        return new NBTItem(itemStack).getCompound("GUpgrade").getInteger("value");
    }

}
