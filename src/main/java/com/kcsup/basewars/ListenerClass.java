package com.kcsup.basewars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ListenerClass implements Listener {

    public Main main;

    public ListenerClass(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(!player.isSneaking() && e.getClickedBlock().getType() == Material.BEACON) {
                e.setCancelled(true);
                player.openWorkbench(player.getLocation(), true);
            } else if(player.isSneaking() && e.getClickedBlock().getType() == Material.BEACON &&
                player.getItemInHand().getAmount() == 0){
                e.setCancelled(true);
                player.openWorkbench(player.getLocation(), true);
            } else if(player.isSneaking() && e.getClickedBlock().getType() == Material.BEACON &&
                    !player.getItemInHand().getType().isBlock()) {
                e.setCancelled(true);
                player.openWorkbench(player.getLocation(), true);
            }

            if(!player.isSneaking() && e.getClickedBlock().getType() == Material.WORKBENCH) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "No.");
            } else if(player.isSneaking() && e.getClickedBlock().getType() == Material.WORKBENCH &&
                    player.getItemInHand().getAmount() == 0){
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "No.");
            } else if(player.isSneaking() && e.getClickedBlock().getType() == Material.WORKBENCH &&
                    !player.getItemInHand().getType().isBlock()) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "No.");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Location blockPlacedLocation = e.getBlockPlaced().getLocation();
        if (e.getBlockPlaced().getType() == Material.BEACON) {
            Location locationAddTwo = e.getBlockPlaced().getLocation().add(0, 2, 0);
            player.setBedSpawnLocation(locationAddTwo, true);
            main.beaconLocation.put(blockPlacedLocation, player);
        }

        Location placedMinusOne = e.getBlockPlaced().getLocation().subtract(0, 1, 0);
        Location placedMinusTwo = e.getBlockPlaced().getLocation().subtract(0, 2, 0);
        if(e.getBlockPlaced().getType() != Material.BEACON) {
            if (main.beaconLocation.containsKey(placedMinusOne) ||
                    main.beaconLocation.containsKey(placedMinusTwo)) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot place anything 1 or 2 blocks above a beacon!");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player playerBreaking = e.getPlayer();
        Location blockBrokenLocation = e.getBlock().getLocation();
        if(e.getBlock().getType() == Material.BEACON) {
            if (main.beaconLocation.containsKey(blockBrokenLocation)) {
                Player brokenBeaconOwner = main.beaconLocation.get(blockBrokenLocation);
                if(brokenBeaconOwner == playerBreaking) {
                    e.setCancelled(true);
                    brokenBeaconOwner.sendMessage(ChatColor.RED + "You cannot break your own beacon!");
                } else {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.sendMessage(ChatColor.RED + brokenBeaconOwner.getName() + "'s Beacon was Destroyed by " +
                                playerBreaking.getName() + "!");
                    }
                    main.beaconLocation.remove(blockBrokenLocation);
                }
            }
        }
    }

}
