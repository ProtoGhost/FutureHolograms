package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import me.TheJokerDev.futureholograms.utils.*;
import me.TheJokerDev.futureholograms.holo.*;
import org.bukkit.*;
import java.util.stream.*;
import org.bukkit.util.*;
import java.util.*;

public class ActionCmd implements SubCommand {
    @Override
    public boolean onCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            Utils.sendMessage(sender, "messages.onlyConsole");
            return true;
        }
        if (args.length == 2) {
            final String var1 = args[0];
            final String var2 = args[1];
            final Player p = Bukkit.getPlayer(var1);
            if (p == null) {
                Utils.sendMessage(sender, "messages.commands.action.playerNotExist");
                return true;
            }
            final FHologram hologram = HologramsManager.getHologram(var2);
            if (hologram == null) {
                Utils.sendMessage(sender, "messages.commands.action.notExist");
                return true;
            }
            hologram.onClick(p);
            Utils.sendMessage(sender, "messages.commands.action.success");
        } else {
            Utils.sendMessage(sender, this.help());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final String[] args) {
        final List<String> list = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList()), list);
            Collections.shuffle(list);
        }
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], Arrays.stream(HologramsManager.getHolograms()).map(FHologram::getName).collect(Collectors.toList()), list);
            Collections.shuffle(list);
        }
        return list;
    }

    @Override
    public String help() {
        return Utils.getConfig().getString("messages.commands.action.help").replace("%alias%", "fholo");
    }

    @Override
    public String getPermission() {
        return "futureholograms.console";
    }

    @Override
    public boolean console() {
        return true;
    }
}
