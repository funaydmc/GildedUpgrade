package me.funayd.gildedupgrade.command;

import me.funayd.gildedupgrade.GildedUpgrade;
import me.funayd.gildedupgrade.Upgrader.GUI;
import me.funayd.gildedupgrade.Upgrader.gui_customizer.CustomGui;
import me.funayd.gildedupgrade.data.*;
import me.funayd.gildedupgrade.generator.Importer.Render;
import me.funayd.gildedupgrade.generator.setOther;
import me.funayd.gildedupgrade.nbtapi.NBTCompound;
import me.funayd.gildedupgrade.nbtapi.NBTItem;
import me.funayd.gildedupgrade.util.*;
import me.funayd.gildedupgrade.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Commands implements CommandExecutor {

    private final GildedUpgrade plugin;
    public Commands(GildedUpgrade plugin) {
        Objects.requireNonNull(plugin.getCommand("gildedupgrade")).setExecutor(this);
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length>0 && args[0].equals("registor")) return reload(sender);
        if (args.length == 0) {
            if (!(sender instanceof Player)) return false;
            new GUI((Player) sender,plugin).open();
            return false;
        }
        switch (args[0]){
            case "reload": return reload(sender);
            case "HowToStart?": return tutorial(sender);
            case "help": return help(sender);
            case "get": return get(sender,args);
            case "load": return load(sender);
            case "debug": return debug(sender);
            case "getnext": return getnext(sender);
            case "viewnbt": return viewnbt(sender);
            case "setsocket": return setSocket(sender,args);
            case "generator": return generator(sender,args);
            case "edit": return customGui(sender);
        }
        return false;
    }

    private boolean tutorial(CommandSender sender) {
        if (!sender.isOp()) {
            msg(sender,"&cNo op,No use");
            return false;
        }
        if (sender instanceof ConsoleCommandSender) {
            msg(sender,"&eThis command only used by player");
            return false;
        }
        String[] message = new String[]{
                "&0&n============================================================",
                " ",
                "&e&lBước 1:&e tạo item mẫu:&a Bạn có thể dùng myItem hoặc bất cứ plugin chỉnh sửa nào",
                " ",
                "&e&lBước 2:&e đăng ký item:&a Sử dụng lệnh /gu generator import <NAME> để tạo item",
                " ",
                "&e&lBước 3:&e cấu hình item:&a Vào file /plugins/GildedUpgrade/generator/ và mở file NAME.yml. " +
                        "(Bạn không biết cách cấu hình ư? Hãy đọc hướng dẫn sử dụng trong thư mục" +
                        " GildedUpgrade mục III-2)",
                " ",
                "&e&lBước 4:&e cấu hình line:&a Vào file /plugins/GildedUpgrade/tree.yml. Có một ví " +
                        "dụ khá dễ hiểu trong mục III-1 của hướng dẫn đó. đảm bảo dễ hiểu ;)",
                " ",
                "&e&lBước 5:&e tạo socket:&a Lấy 1 item tùy chỉnh, cầm nó trên tay và dùng lệnh /gu setsocket <VALUE>. " +
                        "(Value là cái gì ư? Đọc lại hướng dẫn sử dụng mục II-1 nào. nó rất quan trọng đó nha)",
                " ",
                "&aTest nào!. Lấy item vừa tạo ra (/gu get <TREE> <NAME> <LEVEL>) và mở giao diện lên (/gu)." +
                        " Cho item và socket vào. ah! bạn sẽ cần tiền để nâng cấp đấy. tắt chức năng này " +
                        "trong config nếu không cần. lượng tiền cần thiết và tỷ lệ thành công sẽ phụ thuộc " +
                        "vào value của socket và item, chú ý nha!",
                " ",
                "&0&n============================================================",
                "&aNếu có lỗi,thắc mắc hay yêu cầu cải tiến nào, hãy tạo chủ đề mới trên: " +
                        "&b https://github.com/thuan782004/Gilded_Upgrade/issues " +
                        "&aHoặc tham gia discord&b https://discord.com/invite/VatBrmDwmf"
        };
        for (String s:message) msg(sender,s);
        return false;
    }

    private boolean customGui(CommandSender sender) {
        if (sender instanceof Player)
        if (sender.hasPermission("gildedupgrade.edit"))
        new CustomGui((Player) sender,plugin);
        return false;
    }
    private boolean reload(CommandSender sender) {
        if (sender.hasPermission("gildedupgrade.reload")||sender instanceof ConsoleCommandSender){
            GildedUpgrade.reload();
        }
        return true;
    }
    private boolean generator(CommandSender sender, String[] args) {
        if (args.length==1||!Arrays.asList("import","remove").contains(args[1])){
            msg(sender,"/gu generator import <id>");
        }
        if (args[1].equals("import")) return render(sender,args);
        return false;
    }
    private boolean render(CommandSender sender,String[] args) {
        if (!sender.hasPermission("gildedupgrade.generator")) {
            msg(sender,"Bạn cần phải có quyền để thực hiện lệnh này (gildedupgrade.generator)");
            return false;
        }
        if (!(sender instanceof Player)) {
            msg(sender,Lang.get("onlyPlayer"));
            return false;
        }
        if (args.length<3) {
            msg(sender,"usage: /gu generator import <id>");
            return false;
        }
        if ((new File(plugin.getDataFolder(), "generator/"+args[2]+".yml").exists())) return false;
        Player p = (Player) sender;
        if (p.getInventory().getItemInMainHand().getType()== Material.AIR) return false;
        Render render = new Render(p.getInventory().getItemInMainHand(),args[2]);
        render.save();
        msg(sender,"Import sucessfull");
        return true;
    }
    private boolean setSocket(CommandSender sender, String[] args){
        if (!sender.hasPermission("gildedupgrade.setsocket")) return false;
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info(Lang.get("onlyPlayer"));
            return false;
        }
        if (args.length<2) {
            sender.sendMessage("/gu setsocket <giá trị> - thiết lập item trên tay thành socket");
            sender.sendMessage("/gu setsocket <id> <giá trị> - thiết lập item trên tay thành socket và lưu vào 'storage.socket.yml'");
            return false;
        }
        Player player = (Player) sender;
        switch (args.length) {
            case 2: return m1(player, args);
            case 3: return m2(player, args);
        }
        return true;
    }

    private boolean m1(Player player, String[] args) {
        if (!checknum.isInt(args[1])) {
            player.sendMessage(Color.vanilla("Giá trị phải là số nguyên"));
            return false;
        }
        setOther.socket(player, Integer.parseInt(args[1]));
        return true;
    }
    
    private boolean m2(Player player, String[] args) {
        String id = args[1];
        if (!checknum.isInt(args[2])) {
            player.sendMessage(Color.vanilla("Giá trị phải là số nguyên"));
            return false;
        }
        setOther.socket(player, id, Integer.parseInt(args[1]));
        return true;
    }

    private boolean viewnbt(CommandSender sender){
        if (!sender.hasPermission("gildedupgrade.viewnbt")) {
            msg(sender,"&amissing permission &egildedupgrade.viewnbt");
            return false;
        }
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info(Lang.get("onlyPlayer"));
            return false;
        }
        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType().toString().equals("AIR")) {
            p.sendMessage(Lang.get("noitem"));
            return false;
        }
        NBTItem nbt = new NBTItem(item);
        List<String> msg = getnbt.viewnbt(nbt);
        if (msg.size() == 0) msg(p,"item has no nbt");
        else msg.forEach(p::sendMessage);
        return true;
    }
    private boolean getnext(CommandSender sender) {
        if (!sender.hasPermission("gildedupgrade.getnext")) return false;
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info(Lang.get("onlyPlayer"));
            return false;
        }
        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType().toString().equals("AIR")) {
            p.sendMessage(Lang.get("noitem"));
            return false;
        }

        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("GUpgrade")) {
            p.sendMessage("Vật phẩm này không thể nâng cấp được");
            return false;
        }

        NBTCompound data = nbt.getCompound("GUpgrade");
        StorageManager.items.get(data.getString("id"))
                .getNext()
                .forEach(i ->
                p.getInventory()
                .addItem(i.getItem()));
        return true;
    }
    private boolean get(CommandSender sender, String[] args){
        if (!sender.hasPermission("gildedupgrade.get")) return false;
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info(Lang.get("onlyPlayer"));
            return false;
        }
        Player player = (Player) sender;
        if (args.length <= 2) {
            msg(sender,"/gu get <line> <level>");
            return false;
        }else {
            player.getInventory().addItem(
            StorageManager.lines.get(args[1])
            .getItems(Integer.parseInt(args[2])-1).getItem());
        }
        return false;
    }
    private boolean load(CommandSender sender){
        if (!sender.hasPermission("gildedupgrade.load")) return false;
        DataLoader.load();
        return false;
    }
    private boolean debug(CommandSender sender){
        if (!sender.hasPermission("gildedupgrade.debug")) return false;
        Debug.debugmode = !Debug.debugmode;
        Bukkit.getLogger().info(Color.vanilla("&7[&eGU-Debug&7]&e  Chế độ gỡ lỗi đã được đặt thành "+Debug.debugmode));
        return true;
    }
    private boolean help(CommandSender p){
        if (!p.hasPermission("gildedupgrade.help")) return false;
        Lang.help().forEach(m -> msg(p,m));
        return true;
    }
    private void msg(CommandSender sender, String msg){
        if (sender==null||msg==null) return;
        if (sender instanceof Player) sender.sendMessage(Color.vanilla(msg));
        else System.out.println(Color.vanilla(msg));
    }
    private String find(ConfigurationSection f, String s){
        if (f.getKeys(false).contains(s)) return s;
        for (String key : f.getKeys(false)) {
            ConfigurationSection section = Objects.requireNonNull(f.getConfigurationSection(key));
            if (!section.getKeys(true).contains(s)) continue;
            return (key+find(section,s));
        }
        return null;
    }
}
