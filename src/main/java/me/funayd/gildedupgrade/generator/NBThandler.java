package me.funayd.gildedupgrade.generator;

import me.funayd.gildedupgrade.nbtapi.NBTCompound;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;

public class NBThandler {

    public static void applynbt(HolderHandler holder,NBTCompound nbt, ConfigurationSection data, int level){
        if (data==null) return;
        Set<String> key = data.getKeys(false);
        if (key.size()==0) return;
        key.forEach(k -> {
            String[] eject = k.split("-",2);
            switch (eject[0]){
                case "compound":
                    nbt.addCompound(eject[1]);
                    NBTCompound compound = nbt.getCompound(eject[1]);
                    ConfigurationSection smalldata = data.getConfigurationSection(k);
                    applynbt(holder,compound,smalldata,level);
                    break;
                case "string":
                    nbt.setString(eject[1], Generator.adapter(holder,data.getString(k),level));
                    break;
                case "int":
                    nbt.setInteger(eject[1], Integer.parseInt(Generator.adapter(holder,String.valueOf(data.getInt(k)), level)));
                    break;
                case "double":
                    nbt.setDouble(eject[1],Double.parseDouble(Generator.adapter(holder,String.valueOf(data.getDouble(k)), level)));
                    break;
                case "liststring":
                    List<String> list1 = data.getStringList(k);
                    for (String s : list1) list1.set(list1.indexOf(s),Generator.adapter(holder,s,level));
                    nbt.getStringList(eject[1]).addAll(list1);
                    break;
                case "listint":
                    List<Integer> list2 = data.getIntegerList(k);
                    for (int s : list2) list2.set(list2.indexOf(s), Integer.parseInt(Generator.adapter(holder,String.valueOf(s),level)));
                    nbt.getIntegerList(eject[1]).addAll(list2);
                    break;
                case "listfloat":
                    List<Float> list3 = data.getFloatList(k);
                    for (float s : list3) list3.set(list3.indexOf(s), Float.parseFloat(Generator.adapter(holder,String.valueOf(s),level)));
                    nbt.getFloatList(eject[1]).addAll(list3);
                    break;
                case "listdouble":
                    List<Double> list4 = data.getDoubleList(k);
                    for (double s : list4) list4.set(list4.indexOf(s), Double.parseDouble(Generator.adapter(holder,String.valueOf(s),level)));
                    nbt.getDoubleList(eject[1]).addAll(list4);
                    break;

            }
        });
    }
}
