package me.TheJokerDev.futureholograms.listeners;

import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.TheJokerDev.futureholograms.holo.*;
import org.bukkit.event.*;

public class WorldListeners implements Listener
{
    @EventHandler
    public void onWorldChange(final PlayerChangedWorldEvent e) {
        final Player p = e.getPlayer();
        final World w = e.getPlayer().getWorld();
        for (final FHologram holo : HologramsManager.getHolograms()) {
            if (holo.getHologram(p) == null) {
                return;
            }
            if (holo.getHologram(p).isDeleted()) {
                return;
            }
            if (holo.getLocation().getWorld().getName().equals(w.getName())) {
                holo.showTo(p, p);
            }
            else {
                holo.hideTo(p, p);
            }
        }
    }
}
