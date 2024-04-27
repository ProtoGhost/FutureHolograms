package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.*;
import org.bukkit.command.*;
import me.TheJokerDev.futureholograms.utils.*;
import me.TheJokerDev.futureholograms.*;
import me.TheJokerDev.futureholograms.holo.*;
import java.util.function.*;
import java.util.stream.*;
import org.bukkit.util.*;
import java.util.*;

public class ReloadCmd implements SubCommand {
    @Override
    public boolean onCommand(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            if (HologramsManager.getHolograms().length == 0) {
                Utils.sendMessage(sender, "messages.commands.reload.noHolograms");
                return true;
            }
            HologramsManager.initHolograms();
            Utils.sendMessage(sender, "messages.commands.reload.success");
        } else if (args.length == 1) {
            final String var1 = args[0];
            if (var1.equalsIgnoreCase("config")) {
                Main.getPlugin().saveDefaultConfig();
                Main.getPlugin().saveConfig();
                Main.getPlugin().reloadConfig();
                Utils.sendMessage(sender, "messages.commands.reload.config");
                return true;
            }
            final FHologram holo = HologramsManager.getHologram(var1);
            if (holo == null) {
                Utils.sendMessage(sender, "messages.commands.reload.notExist");
                return true;
            }
            holo.reload();
            Utils.sendMessage(sender, "messages.commands.reload.success2");
        } else {
            Utils.sendMessage(sender, this.help());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            final String var1 = args[0];
            final List<String> list1 = Arrays.stream(HologramsManager.getHolograms())
                    .map(FHologram::getName)
                    .collect(Collectors.toList());
            list1.add("config");
            final List<String> list2 = new ArrayList<String>();
            StringUtil.copyPartialMatches(var1, list1, list2);
            Collections.shuffle(list2);
            return list2;
        }
        return null;
    }

    @Override
    public String help() {
        return Utils.getConfig().getString("messages.commands.reload.help").replace("%alias%", "fholo");
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean console() {
        return true;
    }
}
