package me.funayd.gildedupgrade.util;

import me.funayd.gildedupgrade.GildedUpgrade;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.mozilla.javascript.engine.RhinoScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class JavaScript {
    private final ScriptEngine engine = getEngine();
    private ScriptEngine getEngine(){
        return new RhinoScriptEngineFactory().getScriptEngine();
    }
    public String parse(String script){
        try { return String.valueOf(engine.eval(script));
        } catch ( NullPointerException | ScriptException e ) {
            ConsoleCommandSender s = Bukkit.getConsoleSender();
            s.sendMessage(ChatColor.RED+"GiledUpgrade Edror: your java version is not compatible");
            s.sendMessage(ChatColor.RED+"GiledUpgrade Error: "+System.getProperty("java.version"));
            s.sendMessage(ChatColor.RED+"GiledUpgrade Error: Engine is error -> disable plugin . . .");
            Bukkit.getPluginManager().disablePlugin(GildedUpgrade.getInstance());
            return null;
        }
    }
}
