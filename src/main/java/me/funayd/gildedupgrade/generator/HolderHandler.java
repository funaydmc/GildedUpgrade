package me.funayd.gildedupgrade.generator;

import me.funayd.gildedupgrade.util.generatingid;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HolderHandler {

    HashMap<String,String> holder = new HashMap<>();
    public HolderHandler(ConfigurationSection config){
        if (config==null) return;
        Set<String> set = config.getKeys(false);
        if (set.size()==0) return;
        set.forEach(k -> holder.put(k,config.getString(k)));
    }
    public String filter(String s){
        if (holder.size()==0) return s;
        HashMap<String,String> replacer = new HashMap<>();
        for (Map.Entry<String, String> entry : holder.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            String id =  generatingid.generateid(null,"replace-");
            replacer.put(id,v);
            s = s.replace(k,id);
        }
        for (Map.Entry<String, String> entry : replacer.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            s = s.replace(k,v);
        }
        return s;
    }


}
