package com.Killstreaks.plugin;

import java.io.File;

import me.nitrousspark.server.Core;
import me.nitrousspark.server.Yaml;

import org.bukkit.entity.Player;

public class OfflineUser {

	private String playerName = null;
	
	public OfflineUser(String playerName){
		
		this.playerName = playerName;
		
	}
	
	public Yaml getYaml(){
		
		return new Yaml(Core.getPath() + "data" + File.separator + "players" + File.separator + this.playerName + ".yml");
		
	}
	
	public int getKillstreak(){
		
		Yaml yaml = this.getYaml();
		
		yaml.add("killstreak", 0);
		
		yaml.reload();
		
		return yaml.getInteger("killstreak");
		
	}
	
	public int getMoneyFromKill(){
		
		Yaml config = Core.config();
		
		int i = config.getInteger("ksmodifier");
		int i1 = config.getInteger("paid");
		
		Yaml yaml = this.getYaml();
		
		yaml.add("paid", i1);
		
		yaml.add("ksmodifier", i);
		
		if(yaml.getInteger("paid") == 0){
			
			yaml.set("paid", i1);
			
		}
		
		if(yaml.getInteger("ksmodifier") == 0){
			
			yaml.set("ksmodifier", i);
			
		}
		
		yaml.reload();
		
		return (this.getBasePay() + (this.getKillstreak() * this.getModifier()));
		
	}
	
	public int getBasePay(){
		
		Yaml yaml = this.getYaml();
		
		return yaml.getInteger("paid");
		
	}
	
	public int getModifier(){
		
		Yaml yaml = this.getYaml();
		
		return yaml.getInteger("ksmodifier");
		
	}
	
	public void setTag(Player player){
		
		Yaml yaml = this.getYaml();
		
		yaml.set("tag", player.getName());
		
		yaml.save();
		
	}
	
	public String getTag(){
		
		Yaml yaml = this.getYaml();
		
		return yaml.getString("tag");
		
	}
	
	public void setKillstreak(int i){
		
		Yaml yaml = this.getYaml();
		
		yaml.set("killstreak", i);
		
		yaml.save();
		
	}
	
}
