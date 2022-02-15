package me.funayd.gildedupgrade.generator;

import me.funayd.gildedupgrade.data.Lang;
import me.funayd.gildedupgrade.data.YamlFile;
import me.funayd.gildedupgrade.nbtapi.NBTCompound;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public static void socket(Player player, String id, int value) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() != Material.AIR) {
            NBTItem nbt = new NBTItem(item);
            if (!nbt.hasKey("GUprade")) {
                nbt.addCompound("GUprade");
            }
            NBTCompound GUprade = nbt.getCompound("GUpgrade");
            GUprade.setInteger("socket-value", value);
            nbt.applyNBT(item);

            YamlFile.SOCKET.get().set(id, value);
            player.sendMessage(ChatColor.GREEN+"Lưu, thiết lập item trên tay bạn thành socket. [ID: %socket_id%; Giá trị: %socket_value%]"
                .replace("%socket_id%", id).replace("%socket_value%", String.valueOf(value)));
        }else {
            player.sendMessage(Lang.get("noitem"));
        }
    }
} 
