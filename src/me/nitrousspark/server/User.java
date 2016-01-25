package me.nitrousspark.server;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class User {

    private Player player = null;

    public User(Player player) {

        this.player = player;

    }

    public Yaml getYaml() {

        return new Yaml(Core.getPath() + "data" + File.separator + "players" + File.separator + this.player.getName() + ".yml");

    }

    public Yaml getSYaml(Player player) {

        return new Yaml(Core.getPath() + "data" + File.separator + "players" + File.separator + player.getName() + ".yml");

    }

    public void kill(Player killed) {

        /*
         * Player player = this.player;
         * 
         * Yaml yaml1 = this.getSYaml(killed);
         */

        Yaml yaml = this.getSYaml(player);

        player.sendMessage(ChatColor.GREEN + "************* " + yaml.getInteger("killstreak") + " *************");

        /*
         * yaml.add("kills", 0);
         * 
         * yaml.increment("kills");
         * 
         * yaml.add("hKillstreak", 0);
         * 
         * if (yaml.getInteger("killstreak") > yaml.getInteger("hKillstreak")) { player.sendMessage(ChatColor.GREEN + "You Have Achieved Your Highest Killstreak!"); yaml.set("hKillstreak", yaml.getInteger("killstreak")); }
         * 
         * yaml.add("killed." + killed.getName(), 0);
         * 
         * yaml.increment("killed." + killed.getName());
         * 
         * yaml1.add("killedby." + this.player.getName(), 0);
         * 
         * yaml1.increment("killedby." + this.player.getName());
         * 
         * yaml.save();
         * 
         * yaml1.save();
         */

    }

    public void death() {

        Yaml yaml = this.getYaml();

        this.player.sendMessage(ChatColor.RED + "Your Kill Streak was ended.");

        yaml.add("deaths", 0);

        yaml.increment("deaths");

        yaml.set("killstreak", 0);

        yaml.save();

    }

    public int getKillstreak() {

        Yaml yaml = this.getYaml();

        yaml.add("killstreak", 0);

        yaml.reload();

        return yaml.getInteger("killstreak");

    }

    public void setTotalMoneyForLife(int i) {

        Yaml yaml = this.getYaml();

        yaml.add("total", 0);

        yaml.increment("total", i);

        yaml.save();

    }

    public void clearTotalMoneyForLife() {

        Yaml yaml = this.getYaml();

        yaml.add("total", 0);

        yaml.set("total", 0);

        yaml.save();

    }

    public int getTotalMoneyForLife() {

        Yaml yaml = this.getYaml();

        return yaml.getInteger("total");

    }

    public int getMoneyFromKill() {

        Yaml config = Core.config();

        int i = config.getInteger("ksmodifier");
        int i1 = config.getInteger("paid");

        Yaml yaml = this.getYaml();

        yaml.add("paid", i1);

        yaml.add("ksmodifier", i);

        if (yaml.getInteger("paid") == 0) {

            yaml.set("paid", i1);

        }

        if (yaml.getInteger("ksmodifier") == 0) {

            yaml.set("ksmodifier", i);

        }

        yaml.reload();

        return (this.getBasePay() + (this.getKillstreak() * this.getModifier()));

    }

    public int getBasePay() {

        Yaml yaml = this.getYaml();

        return yaml.getInteger("paid");

    }

    public int getModifier() {

        Yaml yaml = this.getYaml();

        return yaml.getInteger("ksmodifier");

    }

    public void setExp() {

        this.player.setLevel(this.getKillstreak());

    }

    public int getNukes() {

        Yaml yaml = this.getYaml();

        yaml.add("nukes", 0);

        return yaml.getInteger("nukes");

    }

    public void handlePoints() {

        Yaml yaml = this.getYaml();

        yaml.add("points", 0);

        yaml.increment("points", this.getKillstreak());

        yaml.save();

    }

    public void handleKillstreak() {

        Yaml yaml = this.getYaml();

        int i = yaml.getInteger("killstreak");

        if (i == 3) {

            ItemStack kill1 = new ItemStack(Material.GOLD_SWORD);

            this.player.getInventory().addItem(kill1);

            this.player.sendMessage(ChatColor.GREEN + "You have unlocked an Instant Kill (One time use) sword.");

            this.player.getWorld().playSound(this.player.getLocation(), Sound.LEVEL_UP, 1, 1);

        }

        if (i == 5) {

            ItemStack kill2 = new ItemStack(Material.IRON_AXE);

            this.player.getInventory().addItem(kill2);

            this.player.getInventory().addItem(kill2);

            this.player.sendMessage(ChatColor.GREEN + "You have unlocked a Tomahawk!");

            this.player.getWorld().playSound(this.player.getLocation(), Sound.LEVEL_UP, 1, 1);

        }

        if (i == 8) {

            ItemStack kill2 = new ItemStack(Material.DIAMOND_SWORD);

            this.player.getInventory().addItem(kill2);

            this.player.sendMessage(ChatColor.GREEN + "You Have Unlocked the Freeze Sword! (One Time use)");

            this.player.getWorld().playSound(this.player.getLocation(), Sound.LEVEL_UP, 1, 1);

        }

        if (i == 12) {

            this.player.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 1));

            this.player.sendMessage(ChatColor.GREEN + "You have unlocked one Crying Obsidian (One time use)!");

            Bukkit.broadcastMessage(ChatColor.GOLD + "[" + this.player.getName() + " Multikill!]");

            this.player.getWorld().playSound(this.player.getLocation(), Sound.LEVEL_UP, 1, 1);

        }

        if (i == 20) {

            Bukkit.broadcastMessage(ChatColor.RED + "[" + this.player.getName() + " OMG 20 Kills!]");

            this.player.getInventory().addItem(new ItemStack(Material.TNT, 1));

            this.player.sendMessage(ChatColor.GREEN + "You have earned one Napalm (One time use)!");

            this.player.getWorld().playSound(this.player.getLocation(), Sound.LEVEL_UP, 1, 1);

        }

        if (i == 30) {

            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Enemy Nuke Incoming! It's over...");

            new ClassHandler(this.player).handleMapChange();

            yaml.add("nukes", 0);

            yaml.increment("nukes");

            final Player nukingPlayer = this.player;

            for (Player player : Bukkit.getOnlinePlayers()) {
                final Yaml yaml1 = this.getSYaml(player);
                yaml1.set("map", Core.config().getString("map"));
                if (!(player == nukingPlayer)) {
                    player.sendMessage(ChatColor.RED + "*****BOOM*****");
                    player.sendMessage(ChatColor.RED + " ****BOOM****");
                    player.sendMessage(ChatColor.RED + "  ***BOOM***");
                    player.sendMessage(ChatColor.RED + "   **BOOM**");
                    player.sendMessage(ChatColor.RED + "    *BOOM*");
                    player.sendMessage(ChatColor.RED + "     BOOM");
                    player.setHealth(0);
                    new ClassHandler(player).handleMap();
                    new ClassHandler(nukingPlayer).handleMap();
                    player.sendMessage(ChatColor.RED + "*****BOOM*****");
                    player.sendMessage(ChatColor.RED + " ****BOOM****");
                    player.sendMessage(ChatColor.RED + "  ***BOOM***");
                    player.sendMessage(ChatColor.RED + "   **BOOM**");
                    player.sendMessage(ChatColor.RED + "    *BOOM*");
                    player.sendMessage(ChatColor.RED + "     BOOM");
                    player.setHealth(0);
                    new ClassHandler(player).handleMap();
                    new ClassHandler(nukingPlayer).handleMap();
                }
            }
        }

        yaml.save();

    }

}
