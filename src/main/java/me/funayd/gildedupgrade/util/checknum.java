package me.funayd.gildedupgrade.util;
@SuppressWarnings("unused")
public class checknum {     // khi nào cần thì dùng
    public static boolean isFloat(String str) {
        if (str==null) return false;
        try {
            float x = Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isInt(String str) {

        if (str==null) return false;
        try {
            float x = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
