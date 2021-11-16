package me.funayd.gildedupgrade.contruct;

import me.funayd.gildedupgrade.data.StorageManager;
import me.funayd.gildedupgrade.data.YamlFile;
import me.funayd.gildedupgrade.util.Debug;
import me.funayd.gildedupgrade.util.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Tree {

    private final HashMap<Line, Set<Line>> tree = new HashMap<>();
    private final String id;

    public Tree(String id){
        this.id = id;
    }

    public Tree load(){
        Debug.log("load tree "+id);
        FileConfiguration data = YamlFile.TREES.get();
        ConfigurationSection t = data.getConfigurationSection(id);
        recursive(t);
        save();
        return this;
    }

    private void recursive(ConfigurationSection c){
        Debug.log(" scaning "+c.getCurrentPath());
        Set<String> data = c.getKeys(false);
        data.forEach(s -> {
            if (c.isString(s)) {
                if (tree.keySet().size()==0){
                    tree.put(new Line(s,this),new HashSet<>());
                    return;
                }
                return;
            }
            Set<String> lineid = c.getConfigurationSection(s).getKeys(false);
            Set<Line> lines = new HashSet<>();
            lineid.forEach(ss -> lines.add(new Line(ss,this)));
            tree.put(new Line(s,this),lines);
            recursive(c.getConfigurationSection(s));
        });
    }

    public Set<Line> getnext(Line line){
        return tree.get(line);
    }

    public List<String> getnext(String line){
        List<String> next = new ArrayList<>();
        if (!StorageManager.lines.containsKey(line)) return next;
        Line lines = StorageManager.lines.get(line);
        if (!tree.containsKey(lines)) return next;
        tree.get(lines).forEach(l -> next.add(l.getId()));
        return next;
    }

    public List<String> getline(){

        List<String> allLine = new ArrayList<>();
        tree.forEach((k,v) -> {
            if (!allLine.contains(k.getId()))
            allLine.add(k.getId());
            v.forEach(l -> {
                if (!allLine.contains(l.getId())) allLine.add(l.getId());
            });
        });
        return allLine;
    }

    public void save(){
        Set<Line> allLine = new HashSet<>();
        tree.forEach((k,v) -> {
            allLine.add(k);
            allLine.addAll(v);
        });
        allLine.forEach(Line::save);
        Logger.Info("save tree");
    }

    public String getId() {
        return id;
    }
}
