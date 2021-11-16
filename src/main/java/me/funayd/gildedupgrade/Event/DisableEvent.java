package me.funayd.gildedupgrade.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DisableEvent extends Event {

    static HandlerList handlerList = new HandlerList();
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList(){
        return handlerList;
    }
}
