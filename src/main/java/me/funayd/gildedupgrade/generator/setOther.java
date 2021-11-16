package me.funayd.gildedupgrade.generator;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.data.Lang;
import me.funayd.gildedupgrade.nbtapi.NBTCompound;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import me.funayd.gildedupgrade.util.Color;
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
        compound.setString("id","so-socket");
        compound.setInteger("value",value);
        nbt.applyNBT(item);
    }
    public static void ticket(Player p,String generator){
        File folder = new File(GildedUpgrade.getInstant().getDataFolder(), "generator");
        List<String> list = Arrays.asList(Objects.requireNonNull(folder.list()));
        if (!list.contains(generator+".yml")) {
            p.sendMessage(Color.vanilla("&eillegal generator"));
            return;
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType().toString().equals("AIR")) {
            p.sendMessage(Lang.get("noitem"));
            return;
        }
        NBTItem nbt = new NBTItem(item);
        if (!nbt.getKeys().contains("GUpgrade")) nbt.addCompound("GUpgrade");
        NBTCompound compound = nbt.getCompound("GUpgrade");
        compound.setString("id","ti-"+generator);
        nbt.applyNBT(item);
    }

}
