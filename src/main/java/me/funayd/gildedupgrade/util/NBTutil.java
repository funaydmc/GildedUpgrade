package me.funayd.gildedupgrade.util;

import me.funayd.gildedupgrade.nbtapi.NBTCompoundList;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NBTutil {
    public static boolean hasCMD(ItemStack itemStack){
        return new NBTItem(itemStack).hasKey("CustomModelData");
    }
    public static boolean hasAtt(ItemStack itemStack){
        return new NBTItem(itemStack).hasKey("AttributeModifiers");
    }
    public static boolean isUnbreak(ItemStack itemStack){
        NBTItem nbt = new NBTItem(itemStack);
        return nbt.hasKey("Unbreakable") && nbt.getBoolean("Unbreakable");
    }
    public static int getCMD(ItemStack itemStack){
        NBTItem nbt = new NBTItem(itemStack);
        if (!nbt.hasKey("CustomModelData")) return 0;
        return nbt.getInteger("CustomModelData");
    }
    public static List<String> getAtt(ItemStack itemStack){
        if (!hasAtt(itemStack)) return null;
        List<String> map = new ArrayList<>();
        NBTCompoundList atts = new NBTItem(itemStack).getCompoundList("AttributeModifiers");
        atts.forEach(att -> map.add(
                "name:"+att.getString("Name")
                +";amount:"+att.getDouble("Amount")
                +";operation:"+att.getInteger("Operation")
                +";equipments:"+att.getString("Slot")));
        if (map.size()==0) return null;
        return map;
    }

    public static ItemStack set(ItemStack item, String key, String value){
        if (item==null) return null;
        NBTItem a = new NBTItem(item);
        if (value!=null)
        a.setString(key,value);
        else if (a.hasKey(key)) a.removeKey(key);
        a.applyNBT(item);
        return item;
    }
    public static String get(ItemStack item,String key){
        if (item==null) {
            return null;
        }
        NBTItem a = new NBTItem(item);
        if (!a.hasKey(key)){
            return null;
        }
        return a.getString(key);
    }
}
