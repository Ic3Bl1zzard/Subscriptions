package com.subscriptions.string;

import org.bukkit.ChatColor;

public class StringUtils {

    public static String format(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
