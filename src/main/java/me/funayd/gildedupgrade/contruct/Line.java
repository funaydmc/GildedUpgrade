package me.funayd.gildedupgrade.contruct;

import me.funayd.gildedupgrade.data.StorageManager;
import me.funayd.gildedupgrade.data.YamlFile;
import me.funayd.gildedupgrade.generator.Generator;
import me.funayd.gildedupgrade.runtask.getTask;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Line {

    private String id;
    private final Tree tree;
    private final List<GildedItem> items;
    private boolean fallback;
    private HashMap<String, getTask> task = new HashMap<>();

    public Line(String id,Tree tree){
        this.id = id;
        this.tree = tree;
        Generator generator = new Generator(new StringBuilder(id).delete(0,3).toString());
        this.items = generator.active(this);
        if (this.items==null) return;
        StorageManager.lines.put(id,this);
    }



    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }

    public boolean isFallback() {
        return fallback;
    }

    public List<GildedItem> getItems() {
        return items;
    }

    public GildedItem getItems(int level){
        return items.get(level);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tree getTree() {
        return tree;
    }

    public void save() {
        YamlFile f = YamlFile.LINES;
        FileConfiguration c = f.get();
        List<String> item = new ArrayList<>();
        this.items.forEach(i -> {
            i.save();
            item.add(i.getID());
        });
        c.set(this.id+".tree",this.tree.getId());
        c.set(this.id+".item",item);
        f.save();
        f.reload();
    }


    public void setTask(HashMap<String, getTask> task) {
        this.task = task;
    }

    public HashMap<String, getTask> getTask() {
        return task;
    }
}