package me.TheJokerDev.futureholograms.utils;

import java.util.function.*;
import java.util.stream.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import me.clip.placeholderapi.*;
import org.bukkit.*;
import java.util.*;
import me.TheJokerDev.futureholograms.*;
import java.io.*;
import me.TheJokerDev.other.*;

public class Utils {
    public static String ct(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> ct(final List<String> list) {
        return list.stream()
                .map(Utils::ct)
                .collect(Collectors.toList());
    }

    public static String[] ct(final String... list) {
        return Arrays.stream(list)
                .map(Utils::ct)
                .toArray(String[]::new);
    }

    public static void sendMessage(final CommandSender sender, final List<String> list) {
        list.forEach(s -> sendMessage(sender, s));
    }

    public static void sendMessage(final CommandSender sender, final String... list) {
        Arrays.stream(list).forEach(s -> sendMessage(sender, s));
    }

    public static void sendMessage(final CommandSender sender, final String path) {
        String msg = null;
        try {
            if (getConfig().get(path) == null) {
                msg = path;
            }
            if (msg == null) {
                if (isStringList(getConfig(), path)) {
                    for (final String s : getConfig().getStringList(path)) {
                        sendMessage(sender, s);
                    }
                    return;
                }
                if (!path.equals("")) {
                    msg = getConfig().getString(path);
                } else {
                    msg = "";
                }
            }
        } catch (Exception e) {
            msg = path;
        }
        msg = ct(msg);
        msg = msg.replace("\\n", "\n");
        if (msg.contains("\n")) {
            final String[] msg2 = msg.split("\n");
            sendMessage(sender, msg2);
            return;
        }
        final boolean hasPrefix = msg.contains("{prefix}");
        final boolean isCentered = msg.contains("{center}");
        final boolean isBroadCast = msg.contains("{broadcast}");
        if (hasPrefix) {
            msg = msg.replace("{prefix}", "");
            msg = getPrefix() + msg;
        }
        if (sender instanceof Player) {
            msg = PlaceholderAPI.setPlaceholders((Player)sender, msg);
        } else {
            msg = PlaceholderAPI.setPlaceholders((Player)null, msg);
        }
        if (isCentered) {
            msg = msg.replace("{center}", "");
            msg = getCenteredMSG(msg);
        }
        if (isBroadCast) {
            final String finalMsg = msg.replace("{broadcast}", "");
            Bukkit.getOnlinePlayers().forEach(p -> sendMessage(p, finalMsg));
            sendMessage((CommandSender)Bukkit.getConsoleSender(), msg);
            return;
        }
        if (sender instanceof Player) {
            sender.sendMessage(msg);
        } else {
            Bukkit.getConsoleSender().sendMessage(msg);
        }
    }

    public static boolean isNumeric(final String var0) {
        try {
            Integer.parseInt(var0);
            return true;
        } catch (NumberFormatException var) {
            return false;
        }
    }

    public static String getPrefix() {
        return Main.getPrefix();
    }

    public static FileConfigurationUtil getFile(final String fileName) {
        final File file = new File(Main.getPlugin().getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                Main.getPlugin().saveResource(fileName, false);
            } catch (Exception e) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                }
            }
        }
        return new FileConfigurationUtil(file);
    }

    public static boolean isStringList(final FileConfigurationUtil config, final String path) {
        boolean b = false;
        final Object var1 = config.get(path);
        if (var1 instanceof List) {
            b = true;
        }
        return b;
    }

    public static FileConfigurationUtil getConfig() {
        return getFile("config.yml");
    }

    public static String getCenteredMSG(String message) {
        if (message == null || message.equals("")) {
            return null;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        for (final char c : message.toCharArray()) {
            if (c == '�') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = (c == 'l' || c == 'L');
            } else {
                final DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += (isBold ? dFI.getBoldLength() : dFI.getLength());
                ++messagePxSize;
            }
        }
        final int halvedMessageSize = messagePxSize / 2;
        final int toCompensate = 154 - halvedMessageSize;
        final int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        final StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb + message;
    }
}
