package me.funayd.gildedupgrade.data;

import me.funayd.gildedupgrade.contruct.Tree;
import me.funayd.gildedupgrade.runtask.getTask;

import java.util.Set;

public class DataLoader {

    public static void load(){
        YamlFile.TREES.reload();
        YamlFile.LINES.reload();
        YamlFile.ITEMS.reload();
        YamlFile.TREES.get().getKeys(false).forEach(k -> StorageManager.trees.put(k, new Tree(k).load()));
        Set<String> task_id = StorageManager.taskconfig.getKeys(false);
        if (task_id.size()==0) return;
        task_id.forEach(s -> StorageManager.task.put(s,new getTask(s)));
    }

}
