package me.funayd.gildedupgrade.util;

import me.funayd.gildedupgrade.GildedUpgrade;
import org.bukkit.Bukkit;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class JavaScript {
    private final ScriptEngine engine = getEngine();

    private ScriptEngine getEngine(){
        return new NashornScriptEngineFactory().getScriptEngine();
    }
    public String parse(String script){
        try { return String.valueOf(engine.eval(script));
        } catch ( NullPointerException | ScriptException e ) {
            System.out.println("GiledUpgrade Error: Engine is error -> disable plugin . . .");
            Bukkit.getPluginManager().disablePlugin(GildedUpgrade.getInstant());
            return null;
        }
    }
}
