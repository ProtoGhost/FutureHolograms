package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import me.TheJokerDev.futureholograms.utils.*;
import me.TheJokerDev.futureholograms.holo.*;
import java.util.function.*;
import java.util.stream.*;
import org.bukkit.util.*;
import java.util.*;

public class SetExactlyLocationCmd implements SubCommand {
    @Override
    public boolean onCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            Utils.sendMessage(sender, "messages.onlyPlayers");
            return true;
        }
        final Player p = (Player)sender;
        if (args.length == 1) {
            final String var1 = args[0];
            final FHologram var2 = HologramsManager.getHologram(var1);
            if (var2 == null) {
                Utils.sendMessage(sender, "messages.commands.setexactlylocation.notExist");
                return true;
            }
            var2.setExactlyLocation(p.getLocation());
            var2.deleteAll();
            var2.reload();
            Utils.sendMessage(sender, "messages.commands.setexactlylocation.success");
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
            final List<String> list2 = new ArrayList<>();
            StringUtil.copyPartialMatches(var1, list1, list2);
            Collections.shuffle(list2);
            return list2;
        }
        return null;
    }

    @Override
    public String help() {
        return Utils.getConfig().getString("messages.commands.setexactlylocation.help").replace("%alias%", "fholo");
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean console() {
        return false;
    }
}
