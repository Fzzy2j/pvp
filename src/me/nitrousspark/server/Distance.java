package me.nitrousspark.server;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Distance {

    public static boolean isPlayerInRegion(World world, Location loc1, Location loc2, Player player) {

        int i = (int) loc1.getX();
        int j = (int) loc1.getY();
        int k = (int) loc1.getZ();

        int i1 = (int) loc2.getX();
        int j1 = (int) loc2.getY();
        int k1 = (int) loc2.getZ();

        Location loc3 = player.getLocation();

        int i2 = (int) loc3.getX();
        int j2 = (int) loc3.getY();
        int k2 = (int) loc3.getZ();

        return (i <= i1 && i >= i2 && j <= j1 && j >= j2 && k <= k1 && k >= k2);

    }

    public static boolean isPlayerInRegion(World world, int i, int j, int k, int i1, int j1, int k1, Player player) {

        Location loc3 = player.getLocation();

        int i2 = (int) loc3.getX();
        int j2 = (int) loc3.getY();
        int k2 = (int) loc3.getZ();

        return (i <= i1 && i >= i2 && j <= j1 && j >= j2 && k <= k1 && k >= k2);

    }

    public static ArrayList<Player> playersDistance(Location loc, double distance) {

        ArrayList<Player> players = new ArrayList<Player>();

        double i1 = loc.getX() + distance;
        double j1 = loc.getY() + distance;
        double k1 = loc.getZ() + distance;

        double i2 = loc.getX() - distance;
        double j2 = loc.getY() - distance;
        double k2 = loc.getZ() - distance;

        double i3 = 0;
        double j3 = 0;
        double k3 = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (player.getWorld().equals(loc.getWorld())) {

                i3 = player.getLocation().getX();
                j3 = player.getLocation().getY();
                k3 = player.getLocation().getZ();

                if (i3 <= i1 && i3 >= i2 && j3 <= j1 && j3 >= j2 && k3 <= k1 && k3 >= k2) {

                    players.add(player);

                }

            }

        }

        return players;

    }

}
