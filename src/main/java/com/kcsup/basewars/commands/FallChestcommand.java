package com.kcsup.basewars.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FallChestcommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Location playerLoc1 = player.getLocation().add(0, 10, 0);
//            Location playerLoc2 = player.getLocation().add(0,9,0);
            player.getWorld().spawnEntity(playerLoc1, EntityType.MINECART_CHEST);
            player.sendMessage(ChatColor.GREEN + "LootDrop spawned!");
            for(Entity entity : player.getNearbyEntities(12, 12, 12)) {
                if(entity instanceof StorageMinecart) {
                    StorageMinecart cart = (StorageMinecart) entity;
                    Inventory cartInv = cart.getInventory();
                    Random r = new Random();
                    for(int i=0; i<5; i++) {
                        int low = 0;
                        int high = 27;
                        int result = r.nextInt(high-low) + low;

                        cartInv.setItem(result, new ItemStack(Material.DIAMOND_SWORD));
                    }

                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }
        return false;
    }
}
