package me.TheJokerDev.futureholograms.holo;

import me.TheJokerDev.futureholograms.utils.*;
import java.util.*;
import me.TheJokerDev.other.*;

public class HologramsManager
{
    public static HashMap<String, FHologram> hologramHashMap;
    
    public static void initHolograms() {
        if (!HologramsManager.hologramHashMap.isEmpty()) {
            for (final FHologram hologram : HologramsManager.hologramHashMap.values()) {
                hologram.deleteAll();
            }
        }
        HologramsManager.hologramHashMap = new HashMap<String, FHologram>();
        final FileConfigurationUtil config = Utils.getFile("holograms.yml");
        for (final String key : config.getKeys(false)) {
            final FHologram hologram2 = new FHologram(key);
            HologramsManager.hologramHashMap.put(key, hologram2);
        }
    }
    
    public static void initHologram(final FHologram holo) {
        final String name = holo.getName();
        if (!HologramsManager.hologramHashMap.containsKey(name)) {
            return;
        }
        HologramsManager.hologramHashMap.get(name).deleteAll();
        HologramsManager.hologramHashMap.remove(name);
        final FHologram hologram = new FHologram(name);
        HologramsManager.hologramHashMap.put(name, hologram);
    }
    
    public static FHologram getHologram(final String s) {
        return HologramsManager.hologramHashMap.getOrDefault(s, null);
    }
    
    public static FHologram[] getHolograms() {
        return HologramsManager.hologramHashMap.values().toArray(new FHologram[0]);
    }
    
    static {
        HologramsManager.hologramHashMap = new HashMap<String, FHologram>();
    }
}
