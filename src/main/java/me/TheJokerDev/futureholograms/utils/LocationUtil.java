package me.TheJokerDev.futureholograms.utils;

import org.bukkit.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class LocationUtil {
    public static Location center(final Location var0) {
        return new Location(var0.getWorld(), getRelativeCoord(var0.getBlockX()), getRelativeCoord(var0.getBlockY()), getRelativeCoord(var0.getBlockZ()));
    }

    private static double getRelativeCoord(final int var0) {
        double var = var0;
        var = ((var < 0.0) ? (var + 0.5) : (var + 0.5));
        return var;
    }

    public static String getString(final Location var0, final boolean var1) {
        if (var0 == null) {
            System.out.println("Location null, can't be converted to string");
            return null;
        }
        return var1 ? (var0.getWorld().getName() + "," + center(var0).getX() + "," + var0.getY() + "," + center(var0).getZ() + "," + 0 + "," + var0.getYaw()) : (var0.getWorld().getName() + "," + var0.getX() + "," + var0.getY() + "," + var0.getZ() + "," + var0.getPitch() + "," + var0.getYaw());
    }

    public static Location getLocation(final String var0) {
        final String[] var = var0.split(",");
        Location var2 = null;
        if (var.length < 4) {
            System.out.println("Location can't be obtained from (world,x,y,z needed)'" + var0 + "'");
        } else if (var.length < 6) {
            var2 = new Location(Bukkit.getWorld(var[0]), Double.parseDouble(var[1]), Double.parseDouble(var[2]), Double.parseDouble(var[3]));
        } else {
            try {
                var2 = new Location(Bukkit.getWorld(var[0]), Double.parseDouble(var[1]), Double.parseDouble(var[2]), Double.parseDouble(var[3]), Float.parseFloat(var[5]), Float.parseFloat("0"));
            } catch (NullPointerException | NumberFormatException var3) {
                System.out.println("Location can't be obtained from '" + var0 + "'");
            }
        }
        return var2;
    }

    public static List<Location> getLocations(final List<String> var1) {
        return var1.stream().map(LocationUtil::getLocation).collect(Collectors.toList());
    }
}
