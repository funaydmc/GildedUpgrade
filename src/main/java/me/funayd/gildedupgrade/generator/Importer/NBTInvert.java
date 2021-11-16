package me.funayd.gildedupgrade.generator.Importer;

import me.funayd.gildedupgrade.nbtapi.NBTCompound;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class NBTInvert {

    public static void translate(YamlConfiguration config, NBTItem nbt){
        nbt.removeKey("Enchantments");
        nbt.removeKey("Unbreakable");
        nbt.removeKey("AttributeModifiers");
        nbt.removeKey("HideFlags");
        nbt.removeKey("display");
        nbt.removeKey("CustomModelData");
        nbt.removeKey("GUpgrade");
        config.createSection("nbt");
        ConfigurationSection section = config.getConfigurationSection("nbt");
        config.set("nbt",get(section,nbt));
    }

    private static ConfigurationSection get(ConfigurationSection s,NBTCompound nbt) {
        for (String key : nbt.getKeys()) {
            switch (nbt.getType(key)) {
                case NBTTagCompound:
                    s.createSection("compound-" + key);
                    ConfigurationSection s1 = s.getConfigurationSection("compound-" + key);
                    s.set("compound-" + key,get(s1, nbt.getCompound(key)));
                    break;
                case NBTTagString:
                    s.set("string-" + key, nbt.getString(key));
                    break;
                case NBTTagInt:
                    s.set("int-" + key, nbt.getInteger(key));
                    break;
                case NBTTagFloat:
                    s.set("float-" + key, nbt.getFloat(key));
                    break;
                case NBTTagDouble:
                    s.set("double-" + key, nbt.getDouble(key));
                    break;
                case NBTTagList:
                    switch (nbt.getListType(key)) {
                        case NBTTagString:
                            s.set("liststring-" + key, nbt.getStringList(key));
                            break;
                        case NBTTagInt:
                            s.set("listint-" + key, nbt.getIntegerList(key));
                            break;
                        case NBTTagDouble:
                            s.set("listdouble-" + key, nbt.getDoubleList(key));
                            break;
                        case NBTTagFloat:
                            s.set("listfloat-" + key, nbt.getFloatList(key));
                            break;
                    }
            }
        }
        return s;
    }
}
