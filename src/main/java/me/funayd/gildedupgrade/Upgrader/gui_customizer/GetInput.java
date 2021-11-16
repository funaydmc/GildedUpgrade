package me.funayd.gildedupgrade.Upgrader.gui_customizer;

import me.funayd.gildedupgrade.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GetInput implements Listener {
    private final Player player;
    private final ConfigGui gui;

    public GetInput(Player player,ConfigGui gui){
        this.player = player;
        this.gui = gui;
        player.sendMessage(Color.vanilla("&aVui lòng nhập tên bạn muốn :)"));
        player.sendMessage(Color.vanilla("&eCancel &ađể hủy "));
    }

    @EventHandler
    public void input(AsyncPlayerChatEvent e){
        if (e.getPlayer()!=player) return;
        e.setCancelled(true);
        if (!e.getMessage().equalsIgnoreCase("cancel"))
        gui.stringInput = Color.vanilla(e.getMessage());
        Bukkit.getScheduler().runTask(gui.plugin, gui::open);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void input(PlayerQuitEvent e){
        HandlerList.unregisterAll(this);
    }
}
