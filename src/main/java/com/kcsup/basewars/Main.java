package com.kcsup.basewars;

import com.kcsup.basewars.commands.FallChestcommand;
import com.kcsup.basewars.commands.ForceOPCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin {

    public HashMap<Location, Player> beaconLocation = new HashMap<>();
    public HashMap<Double, Double> beaconXZ = new HashMap<>();
    public List<Double> blockY = new ArrayList<>();

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new ListenerClass(this), this);

        loadCommands();

//        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
//            @Override
//            public void run() {
//                for(Location location : beaconLocation.keySet()) {
//                    Location beaconPlusOne = location.add(0, 1, 0);
//                    Location beaconPlusTwo = location.add(0, 2, 0);
//
//                    beaconPlusOne.getBlock().setType(Material.AIR);
//                    beaconPlusTwo.getBlock().setType(Material.AIR);
//                }
//            }
//        }, 0L, 20L);
    }

    private void loadCommands() {
        getCommand("forceop").setExecutor(new ForceOPCommand());
        getCommand("fallchest").setExecutor(new FallChestcommand());
    }

    @Override
    public void onDisable() {
        beaconLocation.clear();
        beaconXZ.clear();
        blockY.clear();
    }
}
