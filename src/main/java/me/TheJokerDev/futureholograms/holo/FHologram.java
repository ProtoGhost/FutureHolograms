package me.TheJokerDev.futureholograms.holo;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.TheJokerDev.futureholograms.utils.*;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import org.bukkit.*;
import org.bukkit.scheduler.*;
import me.TheJokerDev.futureholograms.*;
import java.io.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import me.clip.placeholderapi.*;

import me.TheJokerDev.other.*;
import org.bukkit.entity.*;
import java.util.function.*;
import java.util.*;

public class FHologram
{
    private final String name;
    private String defaultHologram;
    private BukkitTask updateTask;
    private boolean refresh;
    private int refreshRate;
    private Location location;
    public HashMap<Player, Hologram> holograms;
    public List<String> cooldown;

    public void setLocation(final Location location) {
        this.location = location;
        Utils.getFile("holograms.yml").set(this.getName() + ".location", LocationUtil.getString(location, true));
        Utils.getFile("holograms.yml").save();
    }

    public void setExactlyLocation(final Location location) {
        this.location = location;
        Utils.getFile("holograms.yml").set(this.getName() + ".location", LocationUtil.getString(location, false));
        Utils.getFile("holograms.yml").save();
    }

    public FHologram(final String name) {
        this.refresh = false;
        this.refreshRate = 20;
        this.location = null;
        this.cooldown = new ArrayList<String>();
        this.name = name;
        this.holograms = new HashMap<Player, Hologram>();
        this.defaultHologram = this.getSection().getString("default");
        if (this.getSection().get("location") != null) {
            this.location = LocationUtil.getLocation(this.getSection().getString("location"));
            if (this.getSection().get("refresh") != null && this.getSection().getBoolean("refresh")) {
                this.refresh = true;
            }
            if (this.getSection().get("refreshRate") != null && Utils.isNumeric(this.getSection().getString("refreshRate"))) {
                this.refreshRate = this.getSection().getInt("refreshRate");
            }
            for (final Player p : Bukkit.getOnlinePlayers()) {
                this.spawn(p);
            }
            if (this.refresh) {
                this.updateTask = new BukkitRunnable() {
                    public void run() {
                        if (!new File(Main.getPlugin().getDataFolder(), "holograms.yml").exists()) {
                            this.cancel();
                            HologramsManager.hologramHashMap.remove(name);
                            FHologram.this.deleteAll();
                            return;
                        }
                        FHologram.this.onRefresh();
                    }
                }.runTaskTimerAsynchronously((Plugin)Main.getPlugin(), 0L, (long)this.refreshRate);
            }
            return;
        }
        Utils.sendMessage((CommandSender)Bukkit.getConsoleSender(), "{prefix}&cThe hologram " + name + " doesn't have any location to spawn!");
    }

    public void reload() {
        this.deleteAll();
        this.holograms.keySet().forEach(l -> this.deleteHologram(l));
        this.holograms = new HashMap<Player, Hologram>();
        this.defaultHologram = this.getSection().getString("default");
        if (this.getSection().get("location") != null) {
            this.location = LocationUtil.getLocation(this.getSection().getString("location"));
        }
        else {
            Utils.sendMessage((CommandSender)Bukkit.getConsoleSender(), "{prefix}&cThe hologram " + this.name + " doesn't have any location to spawn!");
        }
        if (this.getSection().get("refresh") != null && this.getSection().getBoolean("refresh")) {
            this.refresh = true;
        }
        if (this.getSection().get("refreshRate") != null && Utils.isNumeric(this.getSection().getString("refreshRate"))) {
            this.refreshRate = this.getSection().getInt("refreshRate");
        }
        for (final Player p : Bukkit.getOnlinePlayers()) {
            this.spawn(p);
        }
        if (this.refresh) {
            this.updateTask = new BukkitRunnable() {
                public void run() {
                    if (!new File(Main.getPlugin().getDataFolder(), "holograms.yml").exists()) {
                        this.cancel();
                        HologramsManager.hologramHashMap.remove(FHologram.this.name);
                        FHologram.this.deleteAll();
                        return;
                    }
                    FHologram.this.onRefresh();
                }
            }.runTaskTimerAsynchronously((Plugin)Main.getPlugin(), 0L, (long)this.refreshRate);
        }
    }

    public ConfigurationSection getSection() {
        return Utils.getFile("holograms.yml").getSection(this.name);
    }

    public Hologram getHologram(final Player p) {
        return this.holograms.get(p);
    }

    public void showTo(final Player p, final Player t) {
        new BukkitRunnable() {
            public void run() {
                FHologram.this.holograms.get(p).getVisibilityManager().showTo(t);
            }
        }.runTask((Plugin)Main.getPlugin());
    }

    public boolean hasCooldown() {
        return this.getSection().get("cooldown") != null;
    }

    public int getCooldown() {
        return this.getSection().getInt("cooldown");
    }

    public void hideTo(final Player p, final Player t) {
        new BukkitRunnable() {
            public void run() {
                FHologram.this.holograms.get(p).getVisibilityManager().hideTo(t);
            }
        }.runTask((Plugin)Main.getPlugin());
    }

    public void deleteHologram(final Player p) {
        if (this.holograms.get(p).size() > 0) {
            this.holograms.get(p).clearLines();
        }
        this.holograms.get(p).delete();
    }

    public Location getLocation() {
        return this.location;
    }

    public String getFormattedLocation() {
        return (this.location != null) ? ("X:" + this.location.getBlockX() + "; Y:" + this.location.getBlockY() + "; Z:" + this.location.getBlockZ()) : Utils.getConfig().getString("messages.commands.list.noLocation");
    }

    public String getSelection(final Player p) {
        return Utils.getFile("data.yml").getString(this.getName() + "." + p.getName(), this.defaultHologram);
    }

    public void loadPlayer(final Player p) {
        Utils.getFile("data.yml").add(this.getName() + "." + p.getName(), this.defaultHologram);
    }

    public String getName() {
        return this.name;
    }

    public void spawn(final Player p) {
        if (this.location == null) {
            return;
        }
        if (this.location.getWorld() == null) {
            Main.log(1, "&cCan't spawn a hologram in a null world location!");
            return;
        }
        final Hologram holo = HologramsAPI.createHologram((Plugin)Main.getPlugin(), this.location);
        for (final String s : this.getLines(p)) {
            holo.appendTextLine(Utils.ct(PlaceholderAPI.setPlaceholders(p, s)));
        }
        holo.setAllowPlaceholders(true);
        final VisibilityManager var7 = holo.getVisibilityManager();
        var7.setVisibleByDefault(false);
        var7.showTo(p);
        this.updateTouchLine(holo, p);
        this.holograms.put(p, holo);
    }

    public void onRefresh() {
        if (this.holograms.isEmpty()) {
            return;
        }
        for (final Player p : this.getPlayers()) {
            this.refreshPlayer(p);
        }
    }

    List<Player> getPlayers() {
        final List<Player> p = new ArrayList<Player>();
        for (final Player p2 : this.holograms.keySet()) {
            if (p2 != null) {
                p.add(p2);
            }
            else {
                this.holograms.remove(null);
            }
        }
        return p;
    }

    public void updateTask() {
        this.updateTask.cancel();
        this.updateTask = new BukkitRunnable() {
            public void run() {
                if (!new File(Main.getPlugin().getDataFolder(), "holograms.yml").exists()) {
                    this.cancel();
                    HologramsManager.hologramHashMap.remove(FHologram.this.name);
                    FHologram.this.deleteAll();
                    return;
                }
                FHologram.this.onRefresh();
            }
        }.runTaskTimerAsynchronously((Plugin)Main.getPlugin(), 0L, (long)this.refreshRate);
    }

    public void refreshPlayer(final Player p) {
        final Hologram holo = this.holograms.get(p);
        if (p == null || !p.isOnline()) {
            holo.delete();
            this.holograms.remove(p);
            return;
        }
        if (holo == null) {
            this.holograms.remove(p);
            this.updateTask();
        }
        if (holo.isDeleted()) {
            this.holograms.remove(p);
            this.updateTask();
        }
        final List<String> var4 = this.getLines(p);
        if (holo.size() > var4.size()) {
            for (int i = holo.size() - var4.size(), i2 = 0; i2 < i; ++i2) {
                holo.removeLine(holo.size() - 1 - 1);
            }
        }
        if (var4.size() < holo.size()) {
            for (int i = holo.size() - var4.size(), i2 = 0; i2 < i; ++i2) {
                holo.removeLine(holo.size() - 1 - 1);
            }
        }
        else {
            for (int i = 0; i < var4.size(); ++i) {
                if (i >= holo.size()) {
                    if (holo.isDeleted()) {
                        this.updateTask();
                        break;
                    }
                    holo.insertTextLine(i, Utils.ct(PlaceholderAPI.setPlaceholders(p, (String)var4.get(i))));
                }
                else {
                    final TextLine textLine = (TextLine)holo.getLine(i);
                    textLine.setText(Utils.ct(PlaceholderAPI.setPlaceholders(p, (String)var4.get(i))));
                }
            }
        }
    }

    private void updateTouchLine(final Hologram holo, final Player p) {
        for (int i = 0; i < holo.size(); ++i) {
            final TouchableLine line = (TouchableLine)holo.getLine(i);
            line.setTouchHandler((TouchHandler)null);
        }
        if (this.getTouchLine(p, holo, this.getLines(p)) == 999) {
            for (int i = 0; i < holo.size(); ++i) {
                final TouchableLine lastLine = (TouchableLine)holo.getLine(i);
                final TouchHandler touchHandler = player -> this.onClick(p);
                lastLine.setTouchHandler(touchHandler);
            }
        }
        else {
            final TouchableLine lastLine2 = (TouchableLine)holo.getLine(this.getTouchLine(p, holo, this.getLines(p)));
            final TouchHandler touchHandler2 = player -> this.onClick(p);
            lastLine2.setTouchHandler(touchHandler2);
        }
    }

    public void onClick(final Player p) {
        if (this.hasCooldown()) {
            if (this.cooldown.contains(p.getName())) {
                return;
            }
            this.cooldown.add(p.getName());
            new BukkitRunnable() {
                public void run() {
                    FHologram.this.cooldown.remove(p.getName());
                }
            }.runTaskLaterAsynchronously((Plugin)Main.getPlugin(), (long)this.getCooldown());
        }
        this.executeActions(p);
        this.updateTouchLine(this.holograms.get(p), p);
        if (p.isSneaking() && this.hasBack(p)) {
            this.setBack(p);
        }
        else {
            this.setNext(p);
        }
        this.refreshPlayer(p);
    }

    private boolean hasBack(final Player p) {
        return this.getSection().get(this.getSelection(p) + ".back") != null;
    }

    private boolean hasTouchLine(final Player p) {
        return this.getSection().get(this.getSelection(p) + ".touchLine") != null;
    }

    private int getTouchLine(final Player p, final Hologram holo, final List<String> lines) {
        final int lastLine = holo.size() - 1;
        if (!this.hasTouchLine(p)) {
            return lastLine;
        }
        final String var1 = this.getSection().getString(this.getSelection(p) + ".touchLine");
        final boolean isInt = Utils.isNumeric(var1);
        if (isInt) {
            final int i = Integer.parseInt(var1) - 1;
            if (i > lines.size() || i < 0) {
                return lastLine;
            }
            return i;
        }
        else {
            final String lowerCase = var1.toLowerCase();
            switch (lowerCase) {
                case "top": {
                    return 0;
                }
                case "middle": {
                    if (lines.size() == 0) {
                        return 0;
                    }
                    return lines.size() / 2;
                }
                case "bottom": {
                    return lastLine;
                }
                case "all": {
                    return 999;
                }
                default: {
                    return lastLine;
                }
            }
        }
    }

    public void executeActions(final Player p) {
        for (String s : this.getActions(p)) {
            final boolean isCommand = s.contains("[command]") || s.contains("[console]");
            final boolean isSound = s.startsWith("[sound]");
            final boolean isMessage = s.startsWith("[message]");
            if (isCommand) {
                final boolean isConsole = s.contains("[console]");
                s = s.replace("[command]", "").replace("[console]", "");
                s = PlaceholderAPI.setPlaceholders(p, s);
                if (isConsole) {
                    Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), s);
                }
                else {
                    p.chat("/" + s);
                }
            }
            else if (isMessage) {
                s = s.replace("[message]", "");
                s = PlaceholderAPI.setPlaceholders(p, s);
                Utils.sendMessage((CommandSender)p, s);
            }
            else {
                if (!isSound) {
                    continue;
                }
                s = s.replace("[sound]", "");
                float volume = 1.0f;
                float pitch = 1.0f;
                XSound sound;
                if (s.contains(",")) {
                    final String[] var1 = s.split(",");
                    sound = XSound.valueOf(var1[0]);
                    volume = Float.parseFloat(var1[1]);
                    pitch = Float.parseFloat(var1[2]);
                }
                else {
                    sound = XSound.valueOf(s.toUpperCase());
                }
                sound.play((Entity)p, volume, pitch);
            }
        }
    }

    public void deleteAll() {
        if (this.holograms.isEmpty()) {
            return;
        }
        this.getPlayers().forEach(this::deleteHologram);
        if (this.updateTask != null) {
            this.updateTask.cancel();
        }
        this.holograms.clear();
    }

    public List<String> getLines(final Player p) {
        final String selection = this.getSelection(p);
        final List<String> list = new ArrayList<String>();
        if (this.getSection().get(selection + ".lines") != null) {
            list.addAll(this.getSection().getStringList(selection + ".lines"));
        }
        return list;
    }

    public List<String> getActions(final Player p) {
        final String selection = this.getSelection(p);
        final List<String> list = new ArrayList<String>();
        if (this.getSection().get(selection + ".actions") != null) {
            list.addAll(this.getSection().getStringList(selection + ".actions"));
        }
        return list;
    }

    public void setNext(final Player p) {
        Utils.getFile("data.yml").set(this.getName() + "." + p.getName(), this.getNext(p));
    }

    public String getNext(final Player p) {
        return this.getSection().getString(this.getSelection(p) + ".next", this.defaultHologram);
    }

    public void setBack(final Player p) {
        Utils.getFile("data.yml").set(this.getName() + "." + p.getName(), this.getBack(p));
    }

    public String getBack(final Player p) {
        return this.getSection().getString(this.getSelection(p) + ".back", this.defaultHologram);
    }
}
