package me.funayd.gildedupgrade.runtask;

import me.funayd.gildedupgrade.data.StorageManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class getTask {
    private final String id;
    public final List<String> task = new ArrayList<>();

    public getTask(String id){
        YamlConfiguration config = StorageManager.taskconfig;
        this.id = id;
        if (config.isList(id)) task.addAll(config.getStringList(id));
        else task.add(config.getString(id));
    }

    public String getId() {
        return id;
    }

    public void excute(Player p){
        if (task.size()==0) return;
        task.forEach(s -> new Task(s).excute(p));
    }

}
