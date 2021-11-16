package me.funayd.gildedupgrade.generator.Importer;

import me.funayd.gildedupgrade.nbtapi.NBTItem;
import me.funayd.gildedupgrade.util.NBTutil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Render {

    Output output;

    public Render(ItemStack input,String id){
        if (input==null||input.getType()== Material.AIR) return;
        output = new Output(input.getType(),id);
        if (!input.hasItemMeta()) {
            try {output.save();
            } catch (IOException ignored) {}
            return;
        }
        ItemMeta itemMeta = input.getItemMeta();
        assert itemMeta != null;
        if (itemMeta.hasDisplayName())
        output.setName(inverse(itemMeta.getDisplayName()));
        if (itemMeta.hasLore()){
            List<String> lore = itemMeta.getLore();
            assert lore != null;
            for (String l : lore)
            lore.set(lore.indexOf(l),inverse(l));
            output.setLore(lore);
        }
        output.setUnbreak(String.valueOf(NBTutil.isUnbreak(input)));
        if (NBTutil.hasCMD(input))
        output.setCustomModelData(String.valueOf(NBTutil.getCMD(input)));
        if (itemMeta.hasEnchants()){
            Map<Enchantment, Integer> enchant = itemMeta.getEnchants();
            List<String> outenchant = new ArrayList<>();
            enchant.forEach((e,l) -> outenchant.add(e.getName()+":"+l));
            output.setEnchant(outenchant);
        }
        Set<ItemFlag> flags = itemMeta.getItemFlags();
        if (flags.size() != 0){
            List<String> flag = new ArrayList<>();
            flags.forEach(f -> flag.add(f.toString()));
            output.setFlag(flag);
        }
        if (NBTutil.hasAtt(input)){
            List<String> att =  NBTutil.getAtt(input);
            output.setAtt(att);
        }
        output.setNbttag(new NBTItem(input));

    }
    private String inverse(String s){
        if (s==null) return null;
        return s.replace(ChatColor.COLOR_CHAR, '&');
    }

    public void save(){
        try {
            output.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
