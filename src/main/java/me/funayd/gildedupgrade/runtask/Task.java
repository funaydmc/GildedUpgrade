package me.funayd.gildedupgrade.runtask;

import me.funayd.gildedupgrade.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;

public class Task {

    String script;
    TaskType type;

    public Task(String script){
        this.script = script.replace("\n", "");
        type();
        this.script = removePrefix();
    }

    private enum TaskType{
        MESSAGE("[msg]"),
        SOUND("[sound]"),
        COMMAND("[cmd]"),
        OPCOMMAND("[opcmd]"),
        CONSOLE("[console]"),
        TITLE("[title]"),
        GLOBAL("[global]");

        final String type;
        TaskType(String prefix) {
            this.type = prefix;
        }
    }
    private void type(){
        if (script.startsWith("[msg]")) {
            this.type = TaskType.MESSAGE;
            return;
        }
        if (script.startsWith("[sound]")) {
            this.type = TaskType.SOUND;
            return;
        }
        if (script.startsWith("[cmd]")) {
            this.type = TaskType.COMMAND;
            return;
        }
        if (script.startsWith("[opcmd]")){
            this.type = TaskType.OPCOMMAND;
            return;
        }
        if (script.startsWith("[title]")){
            this.type = TaskType.TITLE;
            return;
        }
        if (script.startsWith("[global]")){
            this.type = TaskType.GLOBAL;
            return;
        }
        if (script.startsWith("[console]")){
            this.type = TaskType.CONSOLE;
        }
    }
    private String removePrefix(){
        StringBuilder builder = new StringBuilder(script);
        int length = type.type.length();
        builder.delete(0,length);
        return builder.toString();
    }
    public void excute(Player p){
        switch (type){
            case MESSAGE:
                p.sendMessage(Color.vanilla(script));
                break;
            case COMMAND:
                p.performCommand(script);
                break;
            case CONSOLE:
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),script);
                break;
            case GLOBAL:
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                players.forEach(pp -> pp.sendMessage(Color.vanilla(script)));
                break;
            case OPCOMMAND:
                if (p.isOp()) {
                    p.performCommand(script);
                    break;
                }
                p.setOp(true);
                try {p.performCommand(script);
                } catch (Exception ignored){};
                p.setOp(false);
                break;
            case TITLE:
                if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1])<12) break;
                String title;
                String subtitle = "";
                int fadein = 20;
                int stay = 60;
                int fadeout = 20;
                String[] splited = this.script.split(";");
                title = Color.vanilla(splited[0]);
                if (splited.length>1 && splited[1]!=null)
                subtitle = Color.vanilla(splited[1]);
                if (splited.length>2 && splited[2]!=null)
                fadein = Integer.parseInt(splited[2]);
                if (splited.length>3 && splited[3]!=null)
                stay = Integer.parseInt(splited[3]);
                if (splited.length>4 && splited[4]!=null)
                fadeout = Integer.parseInt(splited[4]);
                p.sendTitle(title,subtitle,fadein,stay,fadeout);
                break;
            case SOUND:
                this.script = this.script.replace(" ","");
                String[] splited1 = this.script.split(";");
                String sound = splited1[0].toUpperCase();
                float pitch = 1;
                float volume = 1;
                if (splited1.length>1&&splited1[1]!=null) pitch  = Float.parseFloat(splited1[1]);
                if (splited1.length>2&&splited1[2]!=null) volume = Float.parseFloat(splited1[2]);
                p.playSound(p.getLocation(),Sound.valueOf(sound),volume,pitch);
                break;
        }
    }

}
