package me.TheJokerDev.futureholograms.listeners;

import org.bukkit.scheduler.*;
import org.bukkit.entity.*;
import me.TheJokerDev.futureholograms.holo.*;
import me.TheJokerDev.futureholograms.*;
import org.bukkit.plugin.*;
import me.TheJokerDev.futureholograms.utils.*;
import org.bukkit.command.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class LoginListeners implements Listener
{
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        new BukkitRunnable() {
            public void run() {
                for (final FHologram holo : HologramsManager.getHolograms()) {
                    holo.loadPlayer(p);
                    if (holo.getLocation() != null) {
                        new BukkitRunnable() {
                            public void run() {
                                holo.spawn(p);
                            }
                        }.runTask((Plugin)Main.getPlugin());
                        if (holo.getLocation().getWorld() == null) {
                            return;
                        }
                        if (!holo.getLocation().getWorld().getName().equals(p.getWorld().getName())) {
                            holo.hideTo(p, p);
                        }
                    }
                }
            }
        }.runTaskAsynchronously((Plugin)Main.getPlugin());
        if (p.isOp() && p.hasPermission("futureholograms.admin") && Main.hasUpdate() && Utils.getConfig().getBoolean("update.remind")) {
            Utils.sendMessage((CommandSender)p, "{prefix}&aThere is a new update available! &bVersion: " + Main.getPlugin().getNewVersion(), "{prefix}&7Go to fix / improve this plugin.", "{prefix}&ehttps://www.spigotmc.org/resources/futureholograms.94642/");
        }
        if (p.getName().equals("TheJokerDev") || p.getUniqueId().equals(UUID.fromString("11ccbfb1-9bab-4baf-b567-b8304b3f00b3"))) {
            Utils.sendMessage((CommandSender)p, "{prefix}&aThis server is using your plugin! &eVersion: &8(&b" + Main.getPlugin().getDescription().getVersion() + "&8)&7.", "{center}&e&lINFORMATION", "", "{center}&e" + HologramsManager.getHolograms().length + "&7 holograms founded!", "");
        }
    }
    
    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        for (final FHologram holo : HologramsManager.getHolograms()) {
            if (holo.getLocation() != null) {
                holo.deleteHologram(p);
            }
        }
    }
}
