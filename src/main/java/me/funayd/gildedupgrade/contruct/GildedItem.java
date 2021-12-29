package me.funayd.gildedupgrade.contruct;

import me.funayd.gildedupgrade.data.StorageManager;
import me.funayd.gildedupgrade.data.YamlFile;
import me.funayd.gildedupgrade.util.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GildedItem {

    private ItemStack item;
    private String address;
    private int version;
    private int value;
    private String ID;
    private int level;

    public GildedItem(){
    }
    public GildedItem get(String id){
        FileConfiguration config = YamlFile.ITEMS.get();
        if (!config.contains(id)){
            Logger.Info(" Failed to load item "+id);
            return null;
        }
        ConfigurationSection it  = config.getConfigurationSection(id);
        item    = it.getItemStack("item");
        address = it.getString   ("address");
        version = it.getInt      ("version");
        value   = it.getInt      ("value");
        ID      = id;
        StorageManager.items.put(id,this);
        return this;
    }
    public void save(){
        String id = this.ID;
        YamlFile.ITEMS.get().set(id+".item"   ,item   );
        YamlFile.ITEMS.get().set(id+".address",address);
        YamlFile.ITEMS.get().set(id+".version",version);
        YamlFile.ITEMS.get().set(id+".value"  ,value  );
        YamlFile.ITEMS.save();
        YamlFile.ITEMS.reload();
    }
    public List<GildedItem> getNext(){
        Line line = StorageManager.lines.get(address);
        if (line.getItems().indexOf(this)<line.getItems().size()-1)
        return Collections.singletonList(line.getItems(line.getItems().indexOf(this)+1));
        Set<Line> nextline = line.getTree().getnext(line);
        List<GildedItem> nextitem = new ArrayList<>();
        if (nextline.size()==0) return nextitem;
        nextline.forEach(l -> nextitem.add(l.getItems(0)));
        return nextitem;
    }
    public GildedItem item(ItemStack item){
        this.item = item;
        return this;
    }
    public GildedItem address(String address){
        this.address = address;
        return  this;
    }
    public GildedItem version(int version){
        this.version = version;
        return this;
    }
    public GildedItem value(int value){
        this.value = value;
        return this;
    }
    public GildedItem level(int level){
        this.level = level;
        return this;
    }
    public GildedItem id(String id){
        StorageManager.items.put(id,this);
        this.ID = id;
        return this;
    }
    public String getAddress() {
        return address;
    }
    public Line getLine(){
        if (!StorageManager.lines.containsKey(address)) return null;
        return StorageManager.lines.get(address);
    }
    public ItemStack getItem() {
        return item;
    }
    public int getValue() {
        return value;
    }
    public int getVersion() {
        return version;
    }
    public String getID() {
        return ID;
    }
    public int getLevel() {
        return level;
    }
}

