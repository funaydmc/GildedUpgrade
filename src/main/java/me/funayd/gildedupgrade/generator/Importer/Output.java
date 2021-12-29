package me.funayd.gildedupgrade.generator.Importer;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Output {

    private final String material;
    private List<String> att     = new ArrayList<>();
    private List<String> flag    = new ArrayList<>();
    private List<String> lore    = new ArrayList<>();
    private List<String> enchant = new ArrayList<>();
    private String name;
    private String unbreak = "false";
    private String CustomModelData = "0";
    private NBTItem nbttag;

    private final File file;
    private YamlConfiguration config;

    public Output(Material material,String id){
        this.material = material.toString();
        this.file = new File(GildedUpgrade.getInstance().getDataFolder(),"generator/"+id+".yml");
        if (!this.file.exists())
            if (this.file.getParentFile().mkdir()) {
                try {
                    if (this.file.createNewFile())
                     System.out.println("Create file sucessfull");
                } catch (IOException e) {
                    System.out.println("Create file failed");
                }
            }
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    public void setAtt(List<String> att) {this.att = att;}
    public void setFlag(List<String> flag) {this.flag = flag;}
    public void setLore(List<String> lore) {this.lore = lore;}
    public void setEnchant(List<String> enchant) {this.enchant = enchant;}
    public void setName(String name) {this.name = name;}
    public void setUnbreak(String unbreak) {this.unbreak = unbreak;}
    public void setCustomModelData(String customModelData) {CustomModelData = customModelData;}
    public void setNbttag(NBTItem nbttag) {this.nbttag = nbttag;}
    public void save() throws IOException {
        config.set("value", 100);
        config.set("unbreak",unbreak);
        config.set("material",material);
        config.set("fallback", false);
        config.set("max_level", 2);
        if (!CustomModelData.equals("0"))
        config.set("custommodeldata",CustomModelData);
        if (name!=null)
        config.set("name",name);
        if (lore!=null&&lore.size()!=0)
        config.set("lore",lore);
        if (enchant!=null&&enchant.size()!=0)
        config.set("enchant",enchant);
        if (flag!=null&&flag.size()!=0)
        config.set("flag",flag);
        if (att!=null&&att.size()!=0)
        config.set("att",att);
        if (nbttag!=null) {
            NBTInvert.translate(config, nbttag);
        }
        config.set("upgrade_event.true","thanhcong");
        config.set("upgrade_event.false","thatbai");
        config.save(file);
    }
}
