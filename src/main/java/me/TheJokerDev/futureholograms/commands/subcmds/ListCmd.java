package me.TheJokerDev.futureholograms.commands.subcmds;

import me.TheJokerDev.futureholograms.commands.*;
import org.bukkit.command.*;
import me.TheJokerDev.futureholograms.utils.*;
import java.util.*;
import me.TheJokerDev.futureholograms.holo.*;

public class ListCmd implements SubCommand
{
    @Override
    public boolean onCommand(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            this.sendList(sender, "fholo");
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
        return Utils.getConfig().getString("messages.commands.list.help").replace("%alias%", "fholo");
    }
    
    @Override
    public String getPermission() {
        return "futureholograms.admin.list";
    }
    
    void sendList(final CommandSender cmd, final String label) {
        final List<String> help = Utils.getConfig().getStringList("messages.commands.list.format");
        final String format = Utils.getConfig().getString("messages.commands.list.hologramFormat");
        final List<String> var1 = new ArrayList<String>();
        for (final String s : help) {
            if (s.equalsIgnoreCase("{list}")) {
                for (final FHologram subcmd : HologramsManager.getHolograms()) {
                    var1.add(format.replaceAll("%name%", subcmd.getName()).replaceAll("%location%", subcmd.getFormattedLocation()));
                }
            }
            else {
                var1.add(s);
            }
        }
        Utils.sendMessage(cmd, var1);
    }
    
    @Override
    public boolean console() {
        return true;
    }
}
