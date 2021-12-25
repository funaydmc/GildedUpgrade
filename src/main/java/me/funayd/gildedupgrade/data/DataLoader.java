package me.funayd.gildedupgrade.data;

import me.funayd.gildedupgrade.util.OtherUtill;
import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.contruct.Line;
import me.funayd.gildedupgrade.contruct.Tree;
import me.funayd.gildedupgrade.runtask.getTask;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Objects;
import java.util.Set;

public class DataLoader {

    public static void load(){
        YamlFile.LINES.reload();
        YamlFile.ITEMS.reload();
        ConfigurationSection s = GildedUpgrade.getInstance().getConfig().getConfigurationSection("tree_constructs");
        if (s!=null) {
            for (String k : s.getKeys(false)) {
                StorageManager.trees.put(k, new Tree(k).load(s.getConfigurationSection(k)));
                if (!GildedUpgrade.getActivator().getStatus().equals(OtherUtill.KeyState.ACTIVE)) {
                    Bukkit.getConsoleSender().sendMessage(new String[]{
                            ChatColor.RED+"Only one item tree will work.",
                            ChatColor.RED+"Plugin is running in trial mode."
                    }
                    );
                    Bukkit.getConsoleSender().sendMessage(GildedUpgrade.getActivator().getStatus().getDeca());
                }
            }
        }
        File gfl = new File(GildedUpgrade.getInstance().getDataFolder(),"generator");
        if (gfl.exists()){
            for (String s1 : Objects.requireNonNull(gfl.list())) {
                String[] s2 = s1.split("\\.");
                if (s2.length==2){
                    if (!StorageManager.lines.containsKey(s2[0])){
                        new Line(s2[0],null);
                    }
                }
            }
        }
        Set<String> task_id = StorageManager.taskconfig.getKeys(false);
        if (task_id.size()==0) return;
        task_id.forEach(t -> StorageManager.task.put(t,new getTask(t)));
    }

}
