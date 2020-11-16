package com.kcsup.basewars.commands;

import com.kcsup.basewars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpToggleCommand implements CommandExecutor {
    public Main main;

    public PvpToggleCommand(Main main) {
        this.main = main;

    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length < 1) {
                if(main.pvpEnabled) {
                    player.sendMessage(ChatColor.RED + "Currently, PvP is enabled. Using this command will toggle PvP off. To confirm this, do: /pvp-toggle confirm.");
                } else if(!main.pvpEnabled) {
                    player.sendMessage(ChatColor.RED + "Currently, PvP is disabled. Using this command will toggle PvP on. To confirm this, do: /pvp-toggle confirm.");
                }
            } else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("confirm")) {
                    if(main.pvpEnabled) {
                        player.sendMessage(ChatColor.RED + "Disabling PvP");
                        main.pvpEnabled = !main.pvpEnabled;
                    } else if(!main.pvpEnabled) {
                        player.sendMessage(ChatColor.RED + "Enabling PvP");
                        main.pvpEnabled = !main.pvpEnabled;
                    }
                }
            } else if(args.length > 1) {
                player.sendMessage(ChatColor.RED + "Too many arguments!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }
        return false;
    }
}
