package me.TheJokerDev.futureholograms;

import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.*;
import me.TheJokerDev.futureholograms.commands.*;
import org.bukkit.event.*;
import me.TheJokerDev.futureholograms.listeners.*;
import org.bukkit.*;
import me.TheJokerDev.futureholograms.utils.*;
import org.bukkit.command.*;
import me.TheJokerDev.other.*;
import org.bukkit.plugin.*;
import java.util.*;
import me.TheJokerDev.futureholograms.holo.*;

public final class Main extends JavaPlugin {
    private static Main plugin;
    private static boolean papiLoaded;
    private long ms;
    private static boolean hasUpdate;
    private String newVersion;

    public Main() {
        this.newVersion = "";
    }

    public void onEnable() {
        this.saveDefaultConfig();
        Main.plugin = this;
        this.ms = System.currentTimeMillis();
        new BukkitRunnable() {
            public void run() {
                Main.log(0, "{prefix}&7Loading plugin...");
                final PluginManager pm = Main.this.getServer().getPluginManager();
                Main.log(0, "{prefix}&7Checking dependencies...");
                if (!pm.isPluginEnabled("PlaceholderAPI")) {
                    Main.log(1, "&cYou need to install PlaceholderAPI to work.");
                    Main.papiLoaded = false;
                    pm.disablePlugin((Plugin)Main.plugin);
                    return;
                }
                if (!pm.isPluginEnabled("HolographicDisplays")) {
                    Main.log(1, "&cYou need to force install HolographicDisplays to work.");
                    pm.disablePlugin((Plugin)Main.plugin);
                    return;
                }
                Main.log(0, "{prefix}&aDependencies checked and hooked!");
                Main.log(0, "{prefix}&7Loading commands...");
                Main.this.getCommand("futureholograms").setExecutor((CommandExecutor)new FHologramsCmd());
                Main.this.getCommand("futureholograms").setTabCompleter((TabCompleter)new FHologramsCmd());
                Main.log(0, "{prefix}&aCommands loaded!");
                Main.log(0, "{prefix}&7Loading listeners...");
                Main.listeners((Listener)new LoginListeners(), (Listener)new WorldListeners());
                Main.log(0, "{prefix}&aListeners loaded sucessfully!");
                final long ms = System.currentTimeMillis() - Main.plugin.ms;
                Utils.sendMessage((CommandSender)Bukkit.getConsoleSender(), "{prefix}&aPlugin fully loaded and started!", "&b&l=========================================", "&fThanks to use my plugin. Plugin loaded in", "&f" + ms / 1000L + " seconds.", "", "&a    Made with love, by TheJokerDev &c<3", "&b&l=========================================");

                // The update check code has been removed as per your request.

                Main.log(0, "{prefix}&7Loading holograms...");
                HologramsManager.initHolograms();
                Main.log(0, "{prefix}&aHolograms loaded!");
            }
        }.runTask((Plugin)this);
    }

    public String getNewVersion() {
        return this.newVersion;
    }

    public static void listeners(final Listener... list) {
        Arrays.stream(list).forEach(l -> Bukkit.getPluginManager().registerEvents(l, (Plugin)getPlugin()));
    }

    public static boolean hasUpdate() {
        return Main.hasUpdate;
    }

    public static void log(final int mode, final String msg) {
        if (mode == 0) {
            Utils.sendMessage((CommandSender)Bukkit.getConsoleSender(), msg);
        }
        else if (mode == 1) {
            Utils.sendMessage((CommandSender)Bukkit.getConsoleSender(), "&c&lError: &7" + msg);
        }
        else if (mode == 2 && getPlugin().getConfig().getBoolean("debug")) {
            Utils.sendMessage((CommandSender)Bukkit.getConsoleSender(), "&e&lDebug: &7" + msg);
        }
    }

    public static boolean isPapiLoaded() {
        return Main.papiLoaded;
    }

    public static Main getPlugin() {
        return Main.plugin;
    }

    public static String getPrefix() {
        return Utils.getConfig().getString("prefix");
    }

    public void onDisable() {
        log(0, "{prefix}&7Disabling holograms...");
        for (final FHologram holo : HologramsManager.getHolograms()) {
            holo.deleteAll();
            HologramsManager.hologramHashMap.remove(holo.getName());
        }
        log(0, "{prefix}&cHolograms disabled!");
        log(0, "{prefix}&cDisabling plugin...");
    }

    static {
        Main.papiLoaded = true;
    }
}
