package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.*;
import org.bukkit.command.*;
import me.TheJokerDev.futureholograms.utils.*;
import me.TheJokerDev.futureholograms.holo.*;
import java.util.function.*;
import java.util.stream.*;
import org.bukkit.util.*;
import java.util.*;

public class RemoveCmd implements SubCommand {
    @Override
    public boolean onCommand(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            final String var1 = args[0];
            final FHologram var2 = HologramsManager.getHologram(var1);
            if (var2 == null) {
                Utils.sendMessage(sender, "messages.commands.remove.notExist");
                return true;
            }
            Utils.getFile("holograms.yml").set(var1, null);
            var2.deleteAll();
            HologramsManager.hologramHashMap.remove(var1);
            Utils.sendMessage(sender, "messages.commands.remove.success");
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
        return Utils.getConfig().getString("messages.commands.remove.help").replace("%alias%", "fholo");
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
