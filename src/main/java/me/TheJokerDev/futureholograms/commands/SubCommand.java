package me.TheJokerDev.futureholograms.commands;

import org.bukkit.command.*;
import java.util.*;

public interface SubCommand
{
    boolean onCommand(final CommandSender p0, final String[] p1);
    
    List<String> onTabComplete(final CommandSender p0, final String[] p1);
    
    String help();
    
    String getPermission();
    
    boolean console();
}
