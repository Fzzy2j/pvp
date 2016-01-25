package com.Killstreaks.plugin;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Utils {
	
	public static void trashBlock(final Block block, Plugin plugin, int seconds){
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			
			@Override
			public void run(){
				
				block.breakNaturally();
				
			}
			
		}, seconds * 20L);
		
	}
	
	public static void dropItem(ItemStack item, Location loc){
		
		loc.getWorld().dropItemNaturally(loc, item);
		
	}
	
}
