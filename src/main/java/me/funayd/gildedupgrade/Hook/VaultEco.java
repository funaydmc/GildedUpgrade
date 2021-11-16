package me.funayd.gildedupgrade.Hook;

import me.funayd.gildedupgrade.GildedUpgrade;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class VaultEco {

    static Economy eco = GildedUpgrade.geteco();

    public static double getbal(Player p) {
        return eco.getBalance(p);
    }
    public static boolean take(Player p,double amount){
        if (!eco.has(p,amount)) return false;
        eco.withdrawPlayer(p,amount);
        return true;
    }


}
