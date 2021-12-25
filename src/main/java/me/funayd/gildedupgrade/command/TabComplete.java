package me.funayd.gildedupgrade.command;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.contruct.GildedItem;
import me.funayd.gildedupgrade.data.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class TabComplete implements TabCompleter {

    public TabComplete() {
        Objects.requireNonNull(Bukkit.getPluginCommand("gildedupgrade")).setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            if (sender.isOp()||sender.hasPermission("gildedupgrade.admin")) tab.add("HowToStart?");
            if (sender.hasPermission("gildedupgrade.get"))
                if ("get".contains(args[0]))
            tab.add("get");
            if (sender.hasPermission("gildedupgrade.load"))
                if ("load".contains(args[0]))
            tab.add("load");
            if (sender.hasPermission("gildedupgrade.help"))
                if ("help".contains(args[0]))
            tab.add("help");
            if (sender.hasPermission("gildedupgrade.debug"))
                if ("debug".contains(args[0]))
            tab.add("debug");
            if (sender.hasPermission("gildedupgrade.getnext"))
                if ("getnext".contains(args[0]))
            tab.add("getnext");
            if (sender.hasPermission("gildedupgrade.viewnbt"))
                if ("viewnbt".contains(args[0]))
            tab.add("viewnbt");
            if (sender.hasPermission("gildedupgrade.setsocket"))
                if ("setsocket".contains(args[0]))
            tab.add("setsocket");
            if (sender.hasPermission("gildedupgrade.reload"))
                if ("reload".contains(args[0]))
            tab.add("reload");
            if (sender.hasPermission("gildedupgrade.edit"))
                if ("edit".contains(args[0]))
            tab.add("edit");
            if (sender.hasPermission("gildedupgrade.admin"))
                if ("generator".contains(args[0]))
            tab.add("generator");
        }
        if (args.length > 1) {
            if (sender.hasPermission("gildedupgrade.get"))
            if (args[0].equals("get")) {
                if (args.length==2) {
                    return new ArrayList<>(StorageManager.lines.keySet());
                }
                if (args.length==3) {
                    if (!StorageManager.lines.containsKey(args[1])) return Collections.singletonList("line không tồn lại");
                    List<GildedItem> info = StorageManager.lines.get(args[1]).getItems();
                    List<String> name = new ArrayList<>();
                    info.forEach(i -> name.add(String.valueOf(info.indexOf(i)+1)));
                    return name;
                }
            }
            if (sender.hasPermission("gildedupgrade.setticket"))
            if (args[0].equals("setTicket")){
                if (args.length==2) {
                    File folder = new File(GildedUpgrade.getInstance().getDataFolder(), "generator");
                    List<String> ids = Arrays.asList(Objects.requireNonNull(folder.list()));
                    for (String id : ids) {
                        ids.set(ids.indexOf(id),id.replace(".yml",""));
                    }
                    return ids;
                }
            }
            if (sender.hasPermission("gildedupgrade.admin"))
            if (args[0].equals("generator")){
                return Collections.singletonList("import");
            }
        }
        return tab;
    }
}
