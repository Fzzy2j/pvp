package com.Killstreaks.plugin;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.*;

public enum KitType {

	
	OFFENCE("OFFENCE"),
	DEFENCE("DEFENCE");
	
	private int i;
	
	KitType(String s){
		
		if(s.equalsIgnoreCase("DEFENCE")){
			
			this.i = 2;
			
		}
		
		if(s.equalsIgnoreCase("OFFENCE")){
			
			this.i = 2;
			
		}
		
	}
	
	public static KitType getKitType(String s){
		
		if(s.equalsIgnoreCase("DEFENCE")){
			
			return KitType.DEFENCE;
			
		}
		
		if(s.equalsIgnoreCase("OFFENCE")){
			
			return KitType.OFFENCE;
			
		}
		
		return KitType.DEFENCE;
		
	}
	
	public ArrayList<ItemStack> getKitItems(){
		
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		if(i == 1){
			
			items.add(new ItemStack(Material.BOW));
			
		}
		
		if(i == 2){
			
			items.add(new ItemStack(Material.ENDER_PEARL));
			
		}
		
		return items;
		
	}
	
}
