package me.funayd.gildedupgrade.data;

import org.bukkit.Bukkit;
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

public class LicenseKey {
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
                    if (object.get("status").toString() == "online")
                        return false;
                    return true;
                }
                return false;
            }
            Bukkit.getConsoleSender().sendMessage("§e" + plugins + ": §cJson can't be parse (Json is null), please contact Di Hoa Store");
            return false;
        } catch (IOException|org.json.simple.parser.ParseException e) {
            Bukkit.getConsoleSender().sendMessage("§e" + plugins + ": §cJson can't be parse please contact Di Hoa Store");
            Bukkit.getConsoleSender().sendMessage("§e" + plugins + ": §cGet ("+e.getClass().getSimpleName()+"#58) when get key status");
            return false;
        }
    }
    public static boolean action(String key, String action) {
        try {
            if (ip== null)
                return false;
            String query = String.valueOf(key) + "!K04!" + ip;
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
                if (action.equals("enable")) {
                    if (object.get("status").toString().equals("offline"))
                        return true;
                    return false;
                }
                if (object.get("status").toString().equals("online"))
                    return true;
                return false;
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
            if (json != null) {
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
        } catch (NoSuchAlgorithmException e) {
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

    public enum KeyState{
        ACTIVE,OTHER_ACTIVE,NULL,INCORRECT
    }
}