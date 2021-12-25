package me.funayd.gildedupgrade.generator;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.data.Lang;
import me.funayd.gildedupgrade.nbtapi.NBTCompound;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import me.funayd.gildedupgrade.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class setOther {

    public static void socket(Player p,int value){
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType().toString().equals("AIR")) {
            p.sendMessage(Lang.get("noitem"));
            return;
        }
        NBTItem nbt = new NBTItem(item);
        if (!nbt.getKeys().contains("GUpgrade")) nbt.addCompound("GUpgrade");
        NBTCompound compound = nbt.getCompound("GUpgrade");
        compound.setInteger("socket-value",value);
        nbt.applyNBT(item);
        p.sendMessage(ChatColor.GREEN+"Đã thiết lập item trên tay bạn thành socket có giá trị "+value);
    }

}
