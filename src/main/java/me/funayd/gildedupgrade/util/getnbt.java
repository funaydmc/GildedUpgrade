package me.funayd.gildedupgrade.util;

import me.funayd.gildedupgrade.nbtapi.NBTCompound;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class getnbt {  // cái này là để debug thôi
    public static List<String> viewnbt(NBTCompound nbt){
        List<String> data = new ArrayList<>();
        if (nbt.getKeys()==null||nbt.getKeys().size()==0) return data;
        for (String key : nbt.getKeys()) {
            switch (nbt.getType(key)){
                case NBTTagCompound:
                        data.add(ChatColor.AQUA+"compound-"+key+":");
                        List<String> temp = viewnbt(nbt.getCompound(key));
                        List<String> temp1 = new ArrayList<>();
                        temp.forEach(s -> {
                             String t = ChatColor.AQUA+"    ";
                             temp1.add(t+s);
                        });
                        data.addAll(temp1);
                    break;
                case NBTTagString:
                    data.add(ChatColor.AQUA+"string-"+key+": "+nbt.getString(key));
                    break;
                case NBTTagInt:
                    data.add(ChatColor.AQUA+"int-"+key+": "+nbt.getInteger(key));
                    break;
                case NBTTagFloat:
                    data.add(ChatColor.AQUA+"float-"+key+": "+nbt.getFloat(key));
                    break;
                case NBTTagDouble:
                    data.add(ChatColor.AQUA+"double-"+key+": "+nbt.getDouble(key));
                    break;

                case NBTTagList:
                    switch (nbt.getListType(key)){
                        case NBTTagString:
                            data.add(ChatColor.AQUA+"liststring-"+key+": ");
                            List<String> textComponents = new ArrayList<>();
                            nbt.getStringList(key).forEach(string ->
                                    textComponents.add(ChatColor.AQUA+"- "+string));
                            data.addAll(textComponents);
                            break;
                        case NBTTagInt:
                            data.add(ChatColor.AQUA+"listint-"+key+": ");
                            List<Integer> integerList =  nbt.getIntegerList(key);
                            List<String> stringList  =  new ArrayList<>();
                            integerList.forEach(i -> stringList.add(ChatColor.AQUA+"- "+ i));
                            data.addAll(stringList);
                            break;
                        case NBTTagDouble:
                            data.add(ChatColor.AQUA+"listdouble-"+key+": ");
                            List<Double> doubleList =  nbt.getDoubleList(key);
                            List<String> stringList1  =  new ArrayList<>();
                            doubleList.forEach(i -> stringList1.add(ChatColor.AQUA+"- "+ i));
                            data.addAll(stringList1);
                            break;
                        case NBTTagFloat:
                            data.add(ChatColor.AQUA+"listfloat-"+key+": ");
                            List<Float> floatlist =  nbt.getFloatList(key);
                            List<String> stringList2  =  new ArrayList<>();
                            floatlist.forEach(i -> stringList2.add(ChatColor.AQUA+"- "+ i));
                            data.addAll(stringList2);
                            break;
                        case NBTTagCompound:
                            data.add(ChatColor.AQUA+"listcompound-"+key+": ");
                            List<String> compoundlist = new ArrayList<>();
                            nbt.getCompoundList(key).forEach(compound -> {
                                compoundlist.add(ChatColor.AQUA + "<+>");
                                List<String> CTemp = viewnbt(compound);
                                List<String> CTemp1 = new ArrayList<>();
                                CTemp.forEach(s -> {
                                    String t = ChatColor.AQUA+"    ";
                                    CTemp1.add(t+s);
                                });
                                compoundlist.addAll(CTemp1);
                            });
                            data.addAll(compoundlist);
                            break;
            }
        }
    }
        return data;
    }
}
