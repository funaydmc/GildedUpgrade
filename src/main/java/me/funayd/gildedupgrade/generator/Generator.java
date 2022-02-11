package me.funayd.gildedupgrade.generator;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.contruct.GildedItem;
import me.funayd.gildedupgrade.contruct.Line;
import me.funayd.gildedupgrade.data.YamlFile;
import me.funayd.gildedupgrade.nbtapi.NBTCompound;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import me.funayd.gildedupgrade.runtask.getTask;
import me.funayd.gildedupgrade.util.*;
import me.funayd.gildedupgrade.util.generatingid;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Generator {

    private static GildedItem[] items;
    YamlConfiguration data;

    private int max;
    private List<String> att;
    private ConfigurationSection NBTTag;
    private List<String> flag;
    private List<String> lore;
    private String name;
    private String CustomModelData;
    private String material;
    private String value;
    private String unBreak;
    private boolean fallback;
    private List<String> enchant;
    private HolderHandler holder;
    private ConfigurationSection rawTask;
    private final HashMap<String, getTask> task = new HashMap<>();

    public Generator(String id){
        Logger.Info(" Load generator "+id);
        this.data = getGenerator(id);
        if (data==null) return;
        loadData();

        if (value==null||material==null){
            Logger.warn("generator "+id+" is illegal");
            return;
        }
        items = new GildedItem[max];
        for (int i=0 ; i<max; i++){
            int level = i+1;
            items[i] = loadItem(level);
        }
        Logger.Info(" Loaded "+items.length+ " item");

    }
    private void loadData(){
        value           = data.getString    ("value");
        material        = data.getString    ("material");
        max             = data.getInt       ("max_level",10);
        CustomModelData = data.getString    ("custommodeldata");
        name            = data.getString    ("name");
        lore            = data.getStringList("lore");
        enchant         = data.getStringList("enchant");
        flag            = data.getStringList("flag");
        att             = data.getStringList("att");
        unBreak = data.getString("unbreak","false");
        fallback        = data.getBoolean("fallback",false);
        NBTTag = data.getConfigurationSection("nbt");
        rawTask = data.getConfigurationSection("upgrade_event");
        holder          = new HolderHandler(data.getConfigurationSection("holder"));
        rawTask.getKeys(false).forEach(k -> task.put(k,new getTask(rawTask.getString(k))));
    }
    private GildedItem loadItem(int level){
        int valueZ;
        Material materialZ;
        int CustomModelDataZ;
        String nameZ;
        List<String> loreZ = new ArrayList<>();
        List<ItemFlag> flagZ = new ArrayList<>();
        HashMap<Enchantment,Integer> enchantmentsZ = new HashMap<>();
        List<BasherAttributeModifier> attZ = new ArrayList<>();
        boolean unbbreakZ = Boolean.parseBoolean(adapter(unBreak,level));

        valueZ = Math.round(Float.parseFloat(adapter(value,level)));
        if (valueZ<0) valueZ = 0;

        materialZ = Material.getMaterial(adapter(material,level).toUpperCase());
        if (materialZ==null) return null;

        if (checknum.isFloat(adapter(CustomModelData,level)))
        CustomModelDataZ = Math.round(Float.parseFloat(adapter(CustomModelData,level)));
        else CustomModelDataZ = 0;

        nameZ = adapter(name,level);

        if (lore!=null && lore.size()>0) {
        for (String l : lore) loreZ.add(adapter(l, level));
        loreZ = loreAdapter(loreZ);      }

        if (flag!=null && flag.size()>0) {
        for (String fl : flag) {
            ItemFlag itemFlag;
            try { itemFlag = ItemFlag.valueOf(adapter(fl,level).toUpperCase());
            } catch (IllegalArgumentException e){continue;}
            flagZ.add(itemFlag);
        }
        }

        if (enchant!=null && enchant.size()!=0)
        for (String en : enchant) {
            //noinspection deprecation
            Enchantment en1 = Enchantment.getByName(adapter(en.split(":")[0].toLowerCase(), level));
            if (en1 == null) continue;
            int en2;
            if (checknum.isFloat(adapter(en.split(":")[1], level)))
            en2 = Math.round(Float.parseFloat(adapter(en.split(":")[1], level)));
            else continue;
            if (en2 < 0) continue;
            enchantmentsZ.put(en1, en2);
        }
        if (att.size()!=0)
        att.forEach(s -> attZ.add(new BasherAttributeModifier(adapter(s,level))));


        ItemStack item = new ItemStack(materialZ);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(materialZ);
        assert meta != null;
        if (nameZ!=null)
        meta.setDisplayName(nameZ);
        if (loreZ!=null&& Objects.requireNonNull(loreZ).size()>0)
        meta.setLore(loreZ);
        if (CustomModelDataZ!=0)
        if (flagZ.size() != 0)
        flagZ.forEach(meta::addItemFlags);
        item.setItemMeta(meta);
        if (enchantmentsZ.size()!=0)
        item.addUnsafeEnchantments(enchantmentsZ);
        NBTItem nbt = new NBTItem(item);
        NBThandler.applynbt(holder,nbt, NBTTag,level);
        if (CustomModelDataZ!=0) nbt.setInteger("CustomModelData",CustomModelDataZ);
        if (unbbreakZ) nbt.setBoolean("Unbreakable",true);
        nbt.applyNBT(item);
        if (attZ.size()!=0)
        attZ.forEach(a -> a.addModifier(item));
        return new GildedItem()
                .item(item)
                .value(valueZ)
                .level(level);
    }
    public List<GildedItem> active(Line line){
        if (items.length==0) return null;
        line.setTask(task);
        // xử lý từng item info trong mảng đã có
        String address = line.getId();
        line.setFallback(fallback);

        for (int i = 0; i<items.length; i++){
            GildedItem thisiteminfo = items[i];
            thisiteminfo.address(address);   //gán địa chỉ cho item
            if (!YamlFile.LINES.get().contains(address)){
                if (line.getTree()!=null)
                YamlFile.LINES.get().set(address+".tree",line.getTree().getId());
                YamlFile.LINES.save();
                YamlFile.LINES.reload();
            }
            List<String> item = YamlFile.LINES.get().getStringList(address+".item");
            GildedItem tempitem;
            if (i<item.size())
            tempitem = new GildedItem().get(item.get(i));
            else tempitem = null;
            if (tempitem != null) { // nếu item đã tồn tại
                int oldversion = tempitem.getVersion();
                //kiểm tra phiên bản
                ItemStack titi = tempitem.getItem().clone();
                NBTItem titiNBT = new NBTItem(titi);
                titiNBT.removeKey("GUpgrade");
                titiNBT.applyNBT(titi);
                if (titi.isSimilar(thisiteminfo.getItem()))
                    thisiteminfo.version(oldversion);  //để phiên bản như cũ nếu không có gì thay đổi
                else thisiteminfo.version(oldversion+1);//nếu không khớp thì tăng thêm 1
                thisiteminfo.id(tempitem.getID());  //id như cũ
                Debug.log("item already exist, oldversion: "+oldversion);
            }
            else { //nếu item không tồn tại
                thisiteminfo.version(0);  //phiên bản đầu tiên
                Debug.log("create new item");
                //tạo id ngẫu nhiên
                String id = generatingid.generateid(YamlFile.ITEMS.get(), "it-");
                thisiteminfo.id(id);

                Debug.log("create new item: "+id);
            }
            ItemStack nItem = thisiteminfo.getItem();
            NBTItem nbt = new NBTItem(nItem);
            if (!nbt.hasKey("GUpgrade")) nbt.addCompound("GUpgrade");
            NBTCompound compound = nbt.getCompound("GUpgrade");
            compound.setString("id",thisiteminfo.getID());
            compound.setInteger("version",thisiteminfo.getVersion());
            compound.setInteger("level",thisiteminfo.getLevel());
            Debug.log("loaded item: "+thisiteminfo.getID()+" version "+thisiteminfo.getVersion());
            nbt.applyNBT(nItem);
            thisiteminfo.item(nItem);
            items[i] = thisiteminfo;
        }

        Logger.Info(" Complete generation for line "+line.getId());
        return Arrays.asList(items);
    }
    private YamlConfiguration getGenerator(String id){
        Debug.log("Get generator : "+id);
        File file = new File(GildedUpgrade.getInstance().getDataFolder(), "generator/"+id+".yml");
        if (!file.exists()) return null;
        return YamlConfiguration.loadConfiguration(file);
    }
    public String adapter(String input,int level){
        return adapter(holder,input,level);
    }
    public static String adapter(HolderHandler holder, String input,int level){
        if (input==null) return null;
        Debug.log("adapter translating: \""+input+"\" with level "+level);
        input = input.replace("{level}",String.valueOf(level));
        Matcher m = Pattern.compile("@(.*?)@").matcher(input);  //bất cứ thứ gì nằm giữa 2 dấu @
        while (m.find()) {
            String a = m.group();
            String y = a; //lấy phần tử khớp*
            y = y.replace("@","");   //loại bỏ phần thừa
            y = GildedUpgrade.engine.parse(y);    //tính giá trị biểu thức
            input = input.replace(a,y);      //ép vào String gốc
        }
        input = holder.filter(input);
        return Color.vanilla(input);
    }
    private static List<String> loreAdapter(List<String> lore) {
        if (lore == null || lore.size() == 0) return lore;
        LinkedList<String> MyLore = new LinkedList<>(lore);
        for (int i = lore.size()-1; i >= 0; i--) {
            String l = MyLore.get(i);
            if (l.equals("empty")){
                MyLore.remove(i);
                MyLore = new LinkedList<>(loreAdapter(MyLore));
            }
            if (l.contains("/n")) {
                String[] lplus = l.split("/n");
                MyLore.remove(i);
                MyLore.addAll(i, Arrays.asList(lplus));
                MyLore = new LinkedList<>(loreAdapter(MyLore));
            }
        }
        return MyLore;
    }
}
