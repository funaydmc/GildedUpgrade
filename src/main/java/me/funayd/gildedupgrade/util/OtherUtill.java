package me.funayd.gildedupgrade.util;

import me.funayd.gildedupgrade.GildedUpgrade;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OtherUtill {
    private String key;
    private final GildedUpgrade plugin;
    private final ConsoleCommandSender console;
    private KeyState keyState;
    public void setKey(String key){this.key=key;}
    public OtherUtill(String key){
        this.plugin = GildedUpgrade.getInstance();
        this.console = Bukkit.getConsoleSender();
        this.key = key;
        if (getStatus().equals(KeyState.ACTIVE))
        msg("&aGildedUpgrade has been activated perfectly. Have a good day.");
        else {
            console.sendMessage(keyState.getDeca());
            console.sendMessage(new String[]{
                    ChatColor.RED + "Only one item tree will work.",
                    ChatColor.RED + "Plugin is running in trial mode.",
            });
        }
    }
    public String getKey(){
        plugin.reloadConfig();
        key = plugin.getConfig().getString("license");
        if (key==null||key.equals("XXXX-XXXX-XXXX-XXXX")) key=null;
        return key;
    }
    public void msg(String msg){
        if (msg!=null&&console!=null){
            console.sendMessage(ChatColor.translateAlternateColorCodes('&',msg));
        }
    }
    public KeyState getStatus(){
        keyState = key==null?
                   KeyState.NULL:
                   LicenseKey.KeyStatus(key)?
                   LicenseKey.action(key,"enable")?
                   KeyState.ACTIVE:
                   (keyState== KeyState.ACTIVE)?
                   KeyState.ACTIVE:
                   KeyState.OTHER_ACTIVE:
                   KeyState.INCORRECT;
        return keyState;
    }
    public enum KeyState {
        ACTIVE(ChatColor.GREEN+"The plugin has been activated."),
        OTHER_ACTIVE(ChatColor.YELLOW+"Plugin has been activated by another server."),
        NULL(ChatColor.RED+"Key is missing. Please fill it in config.yml"),
        INCORRECT(new String[]{
                ChatColor.RED+"Wrong key. Please check again or request support",
                ChatColor.RED+"from the publisher if you believe this is a bug.",
                ChatColor.RED+"(https://discord.com/invite/VatBrmDwmf)"});
        private final String[] deca;
        KeyState(String dec){this.deca = new String[]{dec};}
        KeyState(String[] dec){this.deca = dec;}
        public String[] getDeca() {return deca;}
    }
    public void disable(){
        if (keyState.equals(OtherUtill.KeyState.ACTIVE))
            LicenseKey.action(key, "disable");
    }

    public static class LicenseKey {
        public static String plugins = "GildedUpgrade";
        private static final String ip = getIpaddress();
        public static boolean KeyStatus(String key) {
            try {
                if (ip == null)
                    return false;
                String query = key + "!K04!" + ip;
                HttpURLConnection connection = (HttpURLConnection)(new URL("https://dihoastore-mc.tk/api/api.php?query=" + getMd5(query) + "&plugin=" + plugins + "&license=" + key + "&ip=" + ip)).openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setConnectTimeout(2000);
                connection.connect();
                BufferedReader stream = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                String inputLine;
                while ((inputLine = stream.readLine()) != null)
                    sb.append(inputLine);
                stream.close();
                connection.disconnect();
                String json = sb.toString().replace(" ", "");
                if (json != null) {
                    JSONParser parser = new JSONParser();
                    JSONObject object = (JSONObject)parser.parse(json);
                    if (object.get("error").toString().equals("yes"))
                        return false;
                    if (object.get("error").toString().equals("no")) {
                        return object.get("status").toString() != "online";
                    }
                    return false;
                }
                Bukkit.getConsoleSender().sendMessage("§e" + plugins + ": §cJson can't be parse (Json is null), please contact Di Hoa Store");
                return false;
            } catch ( IOException |org.json.simple.parser.ParseException e) {
                Bukkit.getConsoleSender().sendMessage("§e" + plugins + ": §cJson can't be parse please contact Di Hoa Store");
                Bukkit.getConsoleSender().sendMessage("§e" + plugins + ": §cGet ("+e.getClass().getSimpleName()+"#56) when get key status");
                return false;
            }
        }
        public static boolean action(String key, String action) {
            try {
                if (ip== null)
                    return false;
                String query = key + "!K04!" + ip;
                URL url = new URL("https://dihoastore-mc.tk/api/api.php?query=" + getMd5(query) + "&plugin=" + plugins + "&license=" + key + "&action=" + action + "&ip=" + ip);
                BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();
                String inputLine;
                while ((inputLine = stream.readLine()) != null)
                    sb.append(inputLine);
                stream.close();
                String json = sb.toString().replace(" ", "");
                if (json != null) {
                    JSONParser parser = new JSONParser();
                    JSONObject object = (JSONObject)parser.parse(json);
                    if (object.get("error").toString().equals("yes")) {
                        Bukkit.getConsoleSender().sendMessage("§c" + plugins + ": §cKey have an error (JsonObject got error). please contact Di Hoa Store");
                        return false;
                    }
                    if (action.equals("enable"))
                    return object.get("status").toString().equals("offline");
                    return object.get("status").toString().equals("online");
                }
                return false;
            } catch (IOException|org.json.simple.parser.ParseException e) {
                Bukkit.getConsoleSender().sendMessage(
                        "§e" + plugins + ": §cJson can't be parse (ParseException#94), please contact Di Hoa Store"
                );
                Bukkit.getConsoleSender().sendMessage("§c" + plugins + ": §aGet ("+e.getClass().getSimpleName()+"#58) when "+action+" key");
                return false;
            }
        }
        public static String getVersion() {
            try {
                URL url = new URL("https://dihoastore-mc.tk/api/update.php?plugin=" + plugins);
                BufferedReader stream = new BufferedReader(new InputStreamReader(
                        url.openStream()));
                StringBuilder sb = new StringBuilder();
                String inputLine;
                while ((inputLine = stream.readLine()) != null)
                    sb.append(inputLine);
                stream.close();
                String json = sb.toString();
                if (!json.equals("")) {
                    JSONParser parser = new JSONParser();
                    JSONObject object = (JSONObject)parser.parse(json);
                    if (object.get("error") == "yes")
                        return "error string";
                    return object.get("version").toString();
                }
                return "Json is null";
            } catch (IOException|org.json.simple.parser.ParseException e) {
                return "parser error";
            }
        }
        public static String getMd5(String input) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(input.getBytes());
                BigInteger no = new BigInteger(1, messageDigest);
                String hashtext = no.toString(16);
                while (hashtext.length() < 32)
                    hashtext = "0" + hashtext;
                return hashtext;
            } catch ( NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        private static String getIpaddress() {
            String ip = null;
            try {
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));
                ip = in.readLine();
            } catch ( IOException ex) {
                Bukkit.getConsoleSender().sendMessage("§c" + plugins + ": §aCan't get your ip address");
            }
            return ip;
        }

    }
}
