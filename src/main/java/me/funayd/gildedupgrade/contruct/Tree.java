package me.funayd.gildedupgrade.contruct;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.data.DataLoader;
import me.funayd.gildedupgrade.data.StorageManager;
import me.funayd.gildedupgrade.util.Debug;
import me.funayd.gildedupgrade.util.Logger;
import me.funayd.gildedupgrade.util.OtherUtill;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class Tree {

    private final HashMap<Line, Set<Line>> tree = new HashMap<>();
    private final String id;

    public Tree(String id){
        this.id = id;
    }

    public Tree load(ConfigurationSection section){
        Debug.log("load tree "+id);
        if (section==null) {
            Debug.log("this tree is null ?");
            return this;
        }
        recursive(section);
        save();
        return this;
    }

    private void recursive(ConfigurationSection c){
        Debug.log(" scaning "+c.getCurrentPath());
        Set<String> data = c.getKeys(false);
        loadLine(c.getName(),c);
        data.forEach(s -> loadLine(s,c));
    }
    private void loadLine(String s, ConfigurationSection c){
        if (c.isString(s)) {
            if (tree.keySet().size()==0){
                tree.put(Line.get(s,this),new HashSet<>());
                return;
            }
            return;
        }
        Set<String> lineid = Objects.requireNonNull(c.getConfigurationSection(s)).getKeys(false);
        Set<Line> lines = new HashSet<>();
        for (String ss : lineid) {
            lines.add(Line.get(ss,this));
            if (!GildedUpgrade.getActivator().getKeyState().equals(OtherUtill.KeyState.ACTIVE)) break;
        }
        tree.put(Line.get(s,this),lines);
        recursive(Objects.requireNonNull(c.getConfigurationSection(s)));
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
