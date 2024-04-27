package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import me.TheJokerDev.futureholograms.utils.*;
import me.TheJokerDev.futureholograms.holo.*;
import me.TheJokerDev.other.*;
import java.util.*;

public class CreateCmd implements SubCommand
{
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
            if (var2 != null) {
                Utils.sendMessage(sender, "messages.commands.create.alreadyExist");
            }
            final FileConfigurationUtil config = Utils.getFile("holograms.yml");
            config.add(var1 + ".location", LocationUtil.getString(p.getLocation(), true));
            config.add(var1 + ".default", "var1");
            config.add(var1 + ".var1.next", "var1");
            final List<String> lines = new ArrayList<String>();
            lines.add("&bChange this on holograms config.");
            config.add(var1 + ".var1.lines", lines);
            config.save();
            final FHologram holo = new FHologram(var1);
            HologramsManager.hologramHashMap.put(var1, holo);
            Utils.sendMessage(sender, "messages.commands.create.success");
        }
        else {
            Utils.sendMessage(sender, this.help());
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final String[] args) {
        return null;
    }
    
    @Override
    public String help() {
        return Utils.getConfig().getString("messages.commands.create.help").replace("%alias%", "fholo");
    }
    
    @Override
    public String getPermission() {
        return "futureholograms.admin.create";
    }
    
    @Override
    public boolean console() {
        return false;
    }
}
