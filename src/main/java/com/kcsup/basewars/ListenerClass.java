package com.kcsup.basewars;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ListenerClass implements Listener {

    public Main main;

    public ListenerClass(Main main) {
        this.main = main;
    }

    private boolean isInBeaconXZ(double X, double Z) {
        return main.beaconXZ.containsKey(X) &&
                main.beaconXZ.containsValue(Z);
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
            main.beaconXZ.put(blockPlacedLocation.getX(), blockPlacedLocation.getZ());
            main.blockY.add(blockPlacedLocation.getY());
        }

        Location placedMinusOne = e.getBlockPlaced().getLocation().subtract(0, 1, 0);
        Location placedMinusTwo = e.getBlockPlaced().getLocation().subtract(0, 2, 0);

        if(e.getBlockPlaced().getType() != Material.BEACON) {
            if (main.beaconLocation.containsKey(placedMinusOne) ||
                    main.beaconLocation.containsKey(placedMinusTwo)) {
                if(!e.getBlockPlaced().getType().hasGravity()) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot place anything 1 or 2 blocks above a beacon!");
                }
            }

            if(main.beaconXZ.containsKey(blockPlacedLocation.getX()) &&
            main.beaconXZ.containsValue(blockPlacedLocation.getZ())) {
                World world = blockPlacedLocation.getWorld();
                if(e.getBlockPlaced().getType().hasGravity()) {
                    for(int i = 1; i < world.getMaxHeight(); i++) {
                        Material check = blockPlacedLocation.getBlock().getRelative(BlockFace.DOWN,i).getType();
                        if(check == Material.BEACON) {
                            e.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You cannot place anything with gravity above a beacon!");
                            break;
                        }

                        if(check != Material.AIR) {
                            break;
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player playerBreaking = e.getPlayer();
        Location blockBrokenLocation = e.getBlock().getLocation();
        World world = blockBrokenLocation.getWorld();
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
                    brokenBeaconOwner.setBedSpawnLocation(world.getSpawnLocation());
                }
            }
        }

    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        if(e.getInventory().getResult().getType() == Material.BED) {
            e.getInventory().setResult(null);
        } else if(e.getInventory().getResult().getType() == Material.BEACON) {
            e.getInventory().setResult(null);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        Entity entity = e.getEntity();
        Block block = e.getBlock();
        Location loc = block.getLocation();
        World world = loc.getWorld();
        if(e.getEntity() instanceof FallingBlock) {
            if(main.beaconXZ.containsKey(loc.getX()) &&
            main.beaconXZ.containsValue(loc.getZ())) {
                for(int i = 1; i < world.getMaxHeight(); i++) {
                    Material check = loc.getBlock().getRelative(BlockFace.DOWN,i).getType();
                    if(check == Material.BEACON) {
                        world.dropItemNaturally(loc, new ItemStack(block.getType()));
                        entity.remove();
                        break;
                    }

                    if(check != Material.AIR) {
                        break;
                    }

                }
            }
        }
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            if(e.getEntity() instanceof Player) {
                Player victim = (Player) e.getEntity();
                if(!main.pvpEnabled) {
                    damager.sendMessage(ChatColor.RED + "PvP is currently disabled!");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPiston(BlockPistonExtendEvent e) {
        Block piston = e.getBlock();
        int beingPushed = e.getBlocks().size();
        Location blockLocation = piston.getLocation();
        List<Material> pushedMaterials = new ArrayList<>();

        for(Player online : Bukkit.getOnlinePlayers()) {
            online.sendMessage(ChatColor.GREEN + "Working");
        }
        for(Block block : e.getBlocks()){
            pushedMaterials.add(block.getType());
        }
        if(pushedMaterials.contains(Material.SLIME_BLOCK)) {
            e.setCancelled(true);
        } else {
            if (e.getDirection() == BlockFace.NORTH) {
                if (main.blockY.contains(blockLocation.getY() - 1) ||
                        main.blockY.contains(blockLocation.getY() - 2)) {
                    if (main.beaconXZ.containsKey(blockLocation.getX()) &&
                            main.beaconXZ.containsValue(blockLocation.getZ() - beingPushed - 1)) {
                        e.setCancelled(true);
                    }
                }
            } else if (e.getDirection() == BlockFace.EAST) {
                if (main.blockY.contains(blockLocation.getY() - 1) ||
                        main.blockY.contains(blockLocation.getY() - 2)) {
                    if (main.beaconXZ.containsKey(blockLocation.getX() + beingPushed + 1) &&
                            main.beaconXZ.containsValue(blockLocation.getZ())) {
                        e.setCancelled(true);
                    }
                }
            } else if (e.getDirection() == BlockFace.SOUTH) {
                if (main.blockY.contains(blockLocation.getY() - 1) ||
                        main.blockY.contains(blockLocation.getY() - 2)) {
                    if (main.beaconXZ.containsKey(blockLocation.getX()) &&
                            main.beaconXZ.containsValue(blockLocation.getZ() + beingPushed + 1)) {
                        e.setCancelled(true);
                    }
                }
            } else if (e.getDirection() == BlockFace.WEST) {
                if (main.blockY.contains(blockLocation.getY() - 1) ||
                        main.blockY.contains(blockLocation.getY() - 2)) {
                    if (main.beaconXZ.containsKey(blockLocation.getX() - beingPushed - 1) &&
                            main.beaconXZ.containsValue(blockLocation.getZ())) {
                        e.setCancelled(true);
                    }
                }
            } else if(e.getDirection() == BlockFace.DOWN) {
                if(main.beaconXZ.containsKey(blockLocation.getX()) &&
                main.beaconXZ.containsValue(blockLocation.getZ())) {
                    if(main.blockY.contains(blockLocation.getY() - beingPushed - 3) ||
                    main.blockY.contains(blockLocation.getY() - beingPushed - 4)) {
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

// Made by Me...
/* -------------------------------------------------------------------------------- */
    @EventHandler
    public void onPlaceFluid(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Material type;
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.WATER_BUCKET) {
            type = Material.WATER;
        } else if (event.getItem().getType() == Material.LAVA_BUCKET) {
            type = Material.LAVA;
        } else return;
        Block block = event.getClickedBlock().getRelative(event.getBlockFace());
        if (generates(type, block)) {
            event.setCancelled(true);
            return;
        } else {
            BlockFace[] nesw = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            for (BlockFace face : nesw) {
                if (generates(type, block.getRelative(face))) {
                    System.out.println(face);
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler()
    public void disableLavaWaterBlockCreation(BlockFromToEvent event) {
        if (event.isCancelled()) return;
        if (!event.getBlock().isLiquid()) return;
        Material type = event.getBlock().getType();
        Block to = event.getToBlock();
        if (generates(event.getBlock(), to)) {
            event.setCancelled(true);
            return;
        } else {
            BlockFace[] nesw = {BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            for (BlockFace face : nesw) {
                if (generates(event.getBlock(), to.getRelative(face))) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }

    private boolean generates(Block from, Block to) {
        if (!to.isLiquid()) return false;
        return generates(from.getType(), to.getType());
    }

    private boolean generates(Material from, Block to) {
        if (!to.isLiquid()) return false;
        return generates(from, to.getType());
    }

    private boolean generates(Material from, Material to) {
        if (from == Material.STATIONARY_WATER) from = Material.WATER;
        else if (from == Material.STATIONARY_LAVA) from = Material.LAVA;

        if (to == Material.STATIONARY_WATER) to = Material.WATER;
        else if (to == Material.STATIONARY_LAVA) to = Material.LAVA;

        return from != to;
    }
/* -------------------------------------------------------------------------------- */

//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent e) {
//        if(e.getWhoClicked() instanceof Player) {
//            for(Player player : Bukkit.getOnlinePlayers()) {
//                player = (Player) e.getWhoClicked();
//                if (e.getClickedInventory().getType() == InventoryType.CHEST) {
//                    e.setCancelled(true);
//                    player.kickPlayer("LOL IMAGINE TRYING TO PLAY THE GAME");
//                    Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getDisplayName(),
//                    ChatColor.RED + "LOL IMAGINE TRYING TO TAKE THINGS FROM A CHEST",
//                    null,
//                    "KCsup [the dev of this plugin ;)]");
//                }
//            }
//        }
//    }

}
