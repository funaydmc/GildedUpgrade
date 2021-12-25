package me.funayd.gildedupgrade.util;

import me.funayd.gildedupgrade.nbtapi.*;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasherAttributeModifier {

    String path;
    String name;
    Attribute attribute;
    double amount;
    Operation operation;
    Set<EquipmentSlot> equipmentSlot;

    public BasherAttributeModifier(String path) {
        this.path = path;
        this.name = returnName();
        this.amount = returnAmount();
        this.operation = returnOperation();
        this.equipmentSlot = returnEquipmentSlot();
        this.attribute = returnAttribute(name);
    }

    public void addModifier(ItemStack item) {
        if (equipmentSlot.isEmpty()) {
            equipmentSlot.add(EquipmentSlot.HAND);
        }else {
            for (EquipmentSlot eq : equipmentSlot) {
                addAttribute(eq, item);
            }
        }
    }

    private void addAttribute(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        String v = Bukkit.getServer().getVersion();
        Pattern p = Pattern.compile("\\(MC: .*?\\)");
        Matcher m = p.matcher(v);
        double version = 0;
        if(m.find())
            version = Double.parseDouble(m.group()
                    .subSequence(1, m.group().length()-1)
                    .toString().split(" ")[1]
                    .split("\\.",2)[1]);
        if (version>=14) {
            AttributeModifier modifier =
            new AttributeModifier(UUID.randomUUID(), name, amount, operation, equipmentSlot);
            ItemMeta meta = itemStack.getItemMeta();
            assert meta != null;
            meta.addAttributeModifier(attribute, modifier);
            itemStack.setItemMeta(meta);
        }
        else {
            NBTItem nbt = new NBTItem(itemStack);
            NBTCompoundList att = nbt.getCompoundList("AttributeModifiers");
            NBTListCompound damage = att.addCompound();
            damage.setString("AttributeName",toLegacyAtt(attribute));
            damage.setString("Name", toLegacyAtt(attribute));
            damage.setDouble("Amount", amount);
            damage.setInteger("Operation", operation.ordinal());
            damage.setInteger("UUIDLeast", (int)UUID.randomUUID().getLeastSignificantBits());
            damage.setInteger("UUIDMost", (int)UUID.randomUUID().getMostSignificantBits());
            damage.setString("Slot", toLegacySlot(equipmentSlot));
            nbt.applyNBT(itemStack);
        }
    }

    private String toLegacyAtt(Attribute a){
        String base = a.name().replaceFirst("_",".").toLowerCase();
        String[] as = base.split("_");
        for (int i=1;i<as.length;i++){
            String temp = as[i];
            as[i] = temp.substring(0, 1).toUpperCase() + temp.substring(1);
        }
        return String.join("",as);
    }
    private String toLegacySlot(EquipmentSlot e){
        switch (e){
            case HAND: return "mainhand";
            case OFF_HAND: return "offhand";
            default: return e.name().toLowerCase();
        }
    }

    private Attribute returnAttribute(String str) {
        if (!isAttribute(str)) {
            return Attribute.GENERIC_ATTACK_DAMAGE;
        }
        String s;
        if (str.equals("jump_strength")) {
            s = ("horse_" + str).toUpperCase();
        }else if (str.equals("spawn_reinforcements")) {
            s = ("zombie_" + str);
        }else {
            s = ("generic_" + str).toUpperCase();
        }
        return Attribute.valueOf(s);
    }

    private boolean isAttribute(String str) {
        String s;
        if (str.equals("jump_strength")) {
            s = ("horse_" + str).toUpperCase();
        }else if (str.equals("spawn_reinforcements")) {
            s = ("zombie_" + str);
        }else {
            s = ("generic_" + str).toUpperCase();
        }
        try {
            Attribute.valueOf(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String returnName() {
        if (path == null || path.isEmpty()) {
            return "null";
        }
        String[] a = path.split(";");
        for (String s : a){
            if (s==null) continue;
            if (s.startsWith("name:")) return s.replace("name:","");
        }
        return null;
    }

    private double returnAmount() {
        if (path == null || path.isEmpty()) {
            return 0;
        }
        String[] a = path.split(";");
        for (String s : a){
            if (s==null) continue;
            if (s.startsWith("amount:"))
                return Double.parseDouble(s.replace("amount:", ""));
        }
        return 0;
    }

    private Operation returnOperation() {
        if (path == null || path.isEmpty()) {
            return Operation.ADD_NUMBER;
        }
        String[] a = path.split(";");
        for (String s : a){
            if (s==null) continue;
            if (s.startsWith("operation:")) {
                int opera = Integer.parseInt(s.replace("operation:", ""));
                return getOperation(opera);
            }
        }
        return getOperation(0);
    }

    private Set<EquipmentSlot> returnEquipmentSlot() {
        Set<EquipmentSlot> equipmentSlots;
        if (path == null || path.isEmpty()) {
            return new HashSet<>();
        }
        String[] a = path.split(";");
        for (String s : a){
            if (s==null) continue;
            if (s.startsWith("equipments:")) {
                equipmentSlots = getEquipmentSlot(s.replace("equipments:", ""));
                return equipmentSlots;
            }
        }
        return getEquipmentSlot("hand");
    }

    private Set<EquipmentSlot> getEquipmentSlot(String string) {
        Set<EquipmentSlot> equipmentSlots = new HashSet<>();
        List<String> list = getList(string);
        if (list.contains("any")) {
            equipmentSlots.addAll(Arrays.asList(EquipmentSlot.values()));
            return equipmentSlots;
        }
        a(list, "hand", equipmentSlots);
        a(list, "off_hand", equipmentSlots);
        a(list, "head", equipmentSlots);
        a(list, "chest", equipmentSlots);
        a(list, "legs", equipmentSlots);
        a(list, "feet", equipmentSlots);
        return equipmentSlots;
    }

    private void a(List<String> list, String e, Set<EquipmentSlot> set) {
        if (list.contains(e)) {
            if (isEquipmentSlot(e.toUpperCase())) {
                set.add(EquipmentSlot.valueOf(e.toUpperCase()));
            }
        }
    }

    private boolean isEquipmentSlot(String string) {
        try {
            EquipmentSlot.valueOf(string);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    private List<String> getList(String string) {
        List<String> list = new ArrayList<>();
        if (string == null || string.isEmpty()) {
            return list;
        }
        for (int i = 0; i <= countChar(string) + 1; i++) {
            if (countChar(string) == 0) {
                list.add(string);
                break;
            }
            String str = string.substring(0, string.indexOf(','));
            string = string.replace(str + ',', "");
            list.add(str);
        }
        return list;
    }

    private int countChar(String string) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ',') {
                count += 1;
            }
        }
        return count;
    }

    private boolean isOperation(String string) {
        if (!isInteger(string)) {
            return false;
        }
        int opera = Integer.parseInt(string);
        if (opera == 1) {
            return true;
        }else if (opera == 2) {
            return true;
        }else return opera == 3;
    }

    private Operation getOperation(int opera) {
        if (opera == 1) {
            return Operation.ADD_NUMBER; // final = base + opera
        }else if (opera == 2) {
            return Operation.ADD_SCALAR; // final = base * opera

        }else if (opera == 3) {
            return Operation.MULTIPLY_SCALAR_1; // final = base * ( opera + 1 )
        }else {
            return Operation.ADD_NUMBER;
        }
    }

    private boolean isInteger(String i) {
        try {
            Integer.parseInt(i);
        }catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
