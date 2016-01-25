package me.nitrousspark.server;

import java.io.File;
import java.util.List;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ClassHandler {

	private Player player = null;

	public ClassHandler(Player player) {

		this.player = player;

	}

	public Yaml getSYaml(Player player) {

		return new Yaml(Core.getPath() + "data" + File.separator + "players"
				+ File.separator + player.getName() + ".yml");

	}

	public void handleMap() {
		Location loc1 = new Location(player.getWorld(), 5.5, 70, 68.5, 90, 0);
		Location loc2 = new Location(player.getWorld(), 42.5, 74, -297.5, 90,
				0);
		Location loc3 = new Location(player.getWorld(), -243.5, 63, 44.5, 90, 0);
		Yaml config = Core.config();
		if (config.getString("map").equals("map1")) {
			player.teleport(loc1);
		}
		if (config.getString("map").equals("map2")) {
			player.teleport(loc2);
		}
		if (config.getString("map").equals("map3")) {
			player.teleport(loc3);
		}
	}

	public void handleMapChange() {
		Yaml config = Core.config();
		if (config.getString("map").equals("map3")) {
			config.set("map", "map1");
		}
		else if (config.getString("map").equals("map2")) {
			config.set("map", "map3");
		}
		else if (config.getString("map").equals("map1")) {
			config.set("map", "map2");
		}
		config.save();
	}
	
	public void grenade(Location l, double radius, int damage)
    {
        l.getWorld().createExplosion(l, 0.0f, false);
        List<Entity> entities = l.getWorld().getEntities();
        for(Entity e : entities)
        {
            if(!(e instanceof LivingEntity))
                    continue;
            Location el = e.getLocation();
            double distance = el.distance(l);
            if(distance <= radius)
            {
                double h = ((LivingEntity)e).getHealth() - damage;
                ((LivingEntity) e).setHealth(h);
            }
 
        }
    }

}
