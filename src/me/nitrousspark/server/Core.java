
package me.nitrousspark.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Core extends JavaPlugin implements Listener {

    private static String path = "";

    private static Server server = null;

    public static Core instance = null;

    public static Plugin plugin = null;

    ArrayList<Player> doubl = new ArrayList<Player>();

    ArrayList<Player> triple = new ArrayList<Player>();

    ArrayList<Player> triple1 = new ArrayList<Player>();

    ArrayList<Player> bowspam = new ArrayList<Player>();

    ArrayList<Player> swordspam = new ArrayList<Player>();

    ArrayList<Player> moderate = new ArrayList<Player>();

    ArrayList<Player> revenge = new ArrayList<Player>();

    ArrayList<Player> freeze = new ArrayList<Player>();

    ArrayList<Player> boom = new ArrayList<Player>();

    ArrayList<Player> sponge = new ArrayList<Player>();

    ArrayList<Player> invisible = new ArrayList<Player>();

    ArrayList<Player> damage = new ArrayList<Player>();

    ArrayList<Arrow> trickshot = new ArrayList<Arrow>();

    ArrayList<Player> kill = new ArrayList<Player>();

    ArrayList<Player> water = new ArrayList<Player>();

    HashMap<String, Integer> kills = new HashMap<String, Integer>();

    public ArrayList<Player> playersInLineOfSight(Player looker, ArrayList<Player> group) {
        ArrayList<Player> visible = new ArrayList<Player>();
        for (int i = group.size(); i > 0; i -= 1) {
            if (looker.hasLineOfSight(group.get(i))) {
                visible.add(group.get(i));
            }
        }
        return visible;
    }

    @Override
    public void onEnable() {

        Core.instance = this;

        Core.plugin = this;

        Core.server = this.getServer();

        (new File(this.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "players")).mkdirs();

        (new File(this.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "playerData")).mkdirs();

        Core.path = this.getDataFolder().getAbsolutePath() + File.separator;

        this.getServer().getPluginManager().registerEvents(this, this);

        Yaml config = Core.config();

        config.add("ksmodifier", 25);

        config.add("paid", 50);

        config.add("map", "map1");

        config.save();

    }

    public static String getPath() {

        return path;

    }

    public static Yaml config() {

        return new Yaml(Core.plugin.getDataFolder().getAbsolutePath() + File.separator + "config.yml");

    }

    public static Yaml playerData() {

        return new Yaml(Core.plugin.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "playerData" + File.separator + "playerData.yml");

    }

    @Override
    public void onDisable() {

        this.history().save();

    }

    public Yaml getPlayerYaml(Player player) {

        return new Yaml(this.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "players" + File.separator + player.getName() + ".yml");

    }

    public Yaml getPlayerYaml(String s) {

        return new Yaml(this.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "players" + File.separator + s + ".yml");

    }

    public Yaml history() {

        return new Yaml(this.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "history.yml");

    }

    @EventHandler
    public void projectileHit(ProjectileHitEvent event) {

        if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player) {

            if (event.getEntity().getLastDamageCause() instanceof Player) {
                event.getEntity().remove();
            }

            Player player = (Player) event.getEntity().getShooter();

            Yaml yaml = this.getPlayerYaml(player);

            yaml.add("arrows", 0);

            yaml.increment("arrows");

            yaml.save();

            Yaml history = this.history();

            history.add("arrows", 0);

            history.increment("arrows");

            history.save();

            event.getEntity().remove();

        }

    }

    public static Server server() {

        return server;

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        User user = new User(event.getPlayer());

        if (event.getMessage().replaceAll("/", "").equalsIgnoreCase("spawn")) {

            if (user.getKillstreak() > 10) {

                event.getPlayer().sendMessage(ChatColor.RED + "You can't use '/spawn' when your killstreak is greater than ten!");

                event.setCancelled(true);
            }

            if (!event.isCancelled()) {

                final Player player = event.getPlayer();

                if (player.isOp()) {
                    new ClassHandler(player).handleMap();
                }

                if (!player.isOp()) {

                    this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                        public void run() {
                            new ClassHandler(player).handleMap();
                        }
                    }, 65L);
                }
            }
        }

    }

    @EventHandler
    public void foodChange(FoodLevelChangeEvent event) {

        event.setCancelled(true);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        int y = (int) player.getLocation().getY();
        final Yaml yaml = this.getPlayerYaml(player);

        yaml.add("nodus", "true");
        yaml.set("nodus", "true");

        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

            public void run() {
                yaml.set("nodus", "false");
                yaml.save();
            }
        }, 10L);

        if (yaml.get("logged").equals(1)) {
            player.setHealth(0);
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " PvP Logged!");
            yaml.set("logged", 0);
        }

        yaml.save();

        if (y < 50) {

            player.setHealth(0);

        }

    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Yaml global = this.history();
        if (event.getEntity().getKiller() == null) {
            Yaml yaml = this.getPlayerYaml(event.getEntity());
            yaml.set("killstreak", 0);
            global.add("deaths", 0);
            global.increment("deaths");
            global.save();
            yaml.save();
        }
        if (event.getEntity() instanceof Player) {
            if (event.getEntity().getKiller() instanceof Player) {
                Yaml data = Core.playerData();
                if (!kills.containsKey(event.getEntity().getKiller().getName())) {
                    kills.put(event.getEntity().getKiller().getName(), 1);
                }
                if (kills.containsKey(event.getEntity().getKiller().getName())) {
                    kills.put(event.getEntity().getKiller().getName(), kills.get(event.getEntity().getKiller().getName()) + 1);
                }
                data.add("PlayerKills", kills);
                data.set("PlayerKills", kills);
                data.save();
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (sender.isOp()) {
                if (label.equalsIgnoreCase("donor")) {
                    Player target = sender.getServer().getPlayer(args[0]);
                    Yaml yaml = this.getPlayerYaml(target);
                    yaml.add("rank", "donor");
                    yaml.set("rank", "donor");
                    yaml.save();
                }
                if (label.equalsIgnoreCase("donor+")) {
                    Player target = sender.getServer().getPlayer(args[0]);
                    Yaml yaml = this.getPlayerYaml(target);
                    yaml.add("rank", "donor+");
                    yaml.set("rank", "donor+");
                    yaml.save();
                }
                if (label.equalsIgnoreCase("donor++")) {
                    Player target = sender.getServer().getPlayer(args[0]);
                    Yaml yaml = this.getPlayerYaml(target);
                    yaml.add("rank", "donor++");
                    yaml.set("rank", "donor++");
                    yaml.save();
                }
                if (label.equalsIgnoreCase("resetrank")) {
                    Player target = sender.getServer().getPlayer(args[0]);
                    Yaml yaml = this.getPlayerYaml(target);
                    yaml.add("rank", "default");
                    yaml.set("rank", "default");
                    yaml.save();
                }
                if (label.equalsIgnoreCase("spawnskel")) {
                    sender.getServer().getWorld("world").spawnEntity(new Location(sender.getServer().getWorld("world"), 52.5, 62, -296.5), EntityType.SKELETON);
                    sender.getServer().getWorld("world").spawnEntity(new Location(sender.getServer().getWorld("world"), 52.5, 62, -290.5), EntityType.SKELETON);
                }
            }
        }
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (label.equalsIgnoreCase("report")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Usage: /report [player]");
                }
                if (args.length == 1) {
                    Player targetPlayer = sender.getServer().getPlayer(args[0]);
                    Yaml yaml1 = this.getPlayerYaml(targetPlayer);
                    Yaml yaml = this.getPlayerYaml(player);
                    yaml.add("reportedPlayers", "");
                    if (!yaml.getString("reportedPlayers").contains(targetPlayer.getName())) {
                        yaml1.add("reports", 0);
                        yaml1.set("reports", yaml1.getInteger("reports") + 1);
                        player.sendMessage(ChatColor.GREEN + "Player Reported.");
                        yaml.add("reportedPlayers", targetPlayer.getName());
                        yaml.set("reportedPlayers", yaml.get("reportedPlayers") + ", " + targetPlayer.getName());
                        yaml.save();
                        this.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                                "mail send nitrousspark " + targetPlayer.getName() + " was reported by " + player.getName() + ", Total reports on " + targetPlayer.getName() + ": " + yaml1.getInteger("reports"));
                    } else {
                        player.sendMessage(ChatColor.RED + "Player Allready Reported!");
                    }
                }
            }
            if (player.isOp()) {
                if (label.equalsIgnoreCase("map")) {
                    new ClassHandler(player).handleMapChange();
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        new ClassHandler(players).handleMap();
                    }
                }
            }
            if (label.equalsIgnoreCase("killed")) {

                if (args.length == 0) {

                    sender.sendMessage(ChatColor.RED + "Too few arguments.");

                }

                if (args.length > 1) {

                    sender.sendMessage(ChatColor.RED + "Too many arguments.");

                }

                if (args.length == 1) {

                    String s = args[0];
                    // String s1 = s;

                    Player player1 = Bukkit.getPlayerExact(s);

                    if (player1 == null) {

                        for (Player players : Bukkit.getOnlinePlayers()) {

                            if (players.getName().toLowerCase().contains(s.toLowerCase())) {

                                player1 = players;

                            }

                        }

                    }

                    if (player1 == null) {

                        sender.sendMessage(ChatColor.RED + "This command only works with online players.");

                    } else {

                        Yaml yaml = new Yaml(this.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "players" + File.separator + sender.getName() + ".yml");

                        int i = yaml.getInteger("killed." + player1.getName());

                        if (i == 0) {

                            sender.sendMessage(ChatColor.RED + "You have never killed " + player1.getName() + "!");

                        } else {

                            sender.sendMessage(ChatColor.GREEN + "You have killed " + player1.getName() + " " + i + " times!");

                        }

                    }

                }

            }

            if (label.equalsIgnoreCase("killedby")) {

                if (args.length == 0) {

                    sender.sendMessage(ChatColor.RED + "Too few arguments.");

                }

                if (args.length > 1) {

                    sender.sendMessage(ChatColor.RED + "Too many arguments.");

                }

                if (args.length == 1) {

                    String s = args[0];
                    // String s1 = s;

                    Player player1 = Bukkit.getPlayerExact(s);

                    if (player1 == null) {

                        for (Player players : Bukkit.getOnlinePlayers()) {

                            if (players.getName().toLowerCase().contains(s.toLowerCase())) {

                                player1 = players;

                            }

                        }

                    }

                    if (player1 == null) {

                        sender.sendMessage(ChatColor.RED + "This command only works with online players.");

                    } else {

                        Yaml yaml = new Yaml(this.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "players" + File.separator + sender.getName() + ".yml");

                        int i = yaml.getInteger("killedby." + player1.getName());

                        if (i == 0) {

                            sender.sendMessage(ChatColor.RED + "You have never been killed by " + player1.getName() + "!");

                        } else {

                            sender.sendMessage(ChatColor.GREEN + "You have been killed by " + player1.getName() + " " + i + " times!");

                        }

                    }

                }

            }

            if (label.equalsIgnoreCase("stats")) {

                if (sender instanceof ConsoleCommandSender) {

                    sender.sendMessage(ChatColor.RED + "This command can only be ran by players!");

                    return true;

                }

                if (args.length == 0) {

                    Yaml yaml = this.getPlayerYaml(player);

                    double kd = yaml.getDouble("kd");
                    int kills = yaml.getInteger("kills");
                    int deaths = yaml.getInteger("deaths");
                    int curKillstreak = yaml.getInteger("killstreak");
                    int hKillstreak = yaml.getInteger("hkillstreak");
                    int swings = yaml.getInteger("Swings");
                    int sonicbooms = yaml.getInteger("sonicbooms");

                    sender.sendMessage(ChatColor.YELLOW + "* * Stats * *");

                    sender.sendMessage(ChatColor.GREEN + "Their Kill/Death Ratio is " + String.format("%.2f", kd) + "!");

                    sender.sendMessage(ChatColor.GREEN + "Your base pay per kill is $" + new User(player).getBasePay() + "!");

                    sender.sendMessage(ChatColor.GREEN + "Your killstreak modifier is $" + new User(player).getModifier() + "!");

                    sender.sendMessage(ChatColor.GREEN + "Your next kill is worth $" + (new User(player).getMoneyFromKill()) + "!");

                    sender.sendMessage(ChatColor.GREEN + "You have earned $" + new User(player).getTotalMoneyForLife() + " this life!");

                    sender.sendMessage(ChatColor.GREEN + "You Have Swung You'r Sword " + swings + " Times!");

                    sender.sendMessage(ChatColor.GREEN + "You Have Created " + sonicbooms + " Sonic Booms!");

                    sender.sendMessage(ChatColor.GREEN + "You have called in " + new User(player).getNukes() + " nukes!");

                    sender.sendMessage(ChatColor.GREEN + "You have " + kills + " kills!");
                    sender.sendMessage(ChatColor.GREEN + "You have " + deaths + " deaths!");

                    sender.sendMessage(ChatColor.GREEN + "Your current killstreak is " + curKillstreak + "!");
                    sender.sendMessage(ChatColor.GREEN + "Your record killstreak is " + hKillstreak + "!");

                    sender.sendMessage(ChatColor.GREEN + "You have shot " + yaml.getInteger("arrows") + " arrows!");

                    sender.sendMessage(ChatColor.GREEN + "More information!");

                    sender.sendMessage(ChatColor.GREEN + "Type '/global' to view global stats!");

                }
                if (args.length == 1) {

                    Player targetPlayer = sender.getServer().getPlayer(args[0]);

                    Yaml yaml = this.getPlayerYaml(targetPlayer);

                    double kd = yaml.getDouble("kd");
                    int kills = yaml.getInteger("kills");
                    int deaths = yaml.getInteger("deaths");
                    int curKillstreak = yaml.getInteger("killstreak");
                    int hKillstreak = yaml.getInteger("hkillstreak");
                    int swings = yaml.getInteger("Swings");
                    int sonicbooms = yaml.getInteger("sonicbooms");

                    sender.sendMessage(ChatColor.YELLOW + targetPlayer.getName() + " Stats.");

                    sender.sendMessage(ChatColor.GREEN + "Their Kill/Death Ratio is " + String.format("%.2f", kd) + "!");

                    sender.sendMessage(ChatColor.GREEN + "They're base pay per kill is $" + new User(targetPlayer).getBasePay() + "!");

                    sender.sendMessage(ChatColor.GREEN + "They're killstreak modifier is $" + new User(targetPlayer).getModifier() + "!");

                    sender.sendMessage(ChatColor.GREEN + "They're next kill is worth $" + (new User(targetPlayer).getMoneyFromKill()) + "!");

                    sender.sendMessage(ChatColor.GREEN + "They have earned $" + new User(targetPlayer).getTotalMoneyForLife() + " this life!");

                    sender.sendMessage(ChatColor.GREEN + "They Have Swung Their Sword " + swings + " Times!");

                    sender.sendMessage(ChatColor.GREEN + "They Have Created " + sonicbooms + " Sonic Booms!");

                    sender.sendMessage(ChatColor.GREEN + "They have called in " + new User(targetPlayer).getNukes() + " nukes!");

                    sender.sendMessage(ChatColor.GREEN + "They have " + kills + " kills!");
                    sender.sendMessage(ChatColor.GREEN + "They have " + deaths + " deaths!");

                    sender.sendMessage(ChatColor.GREEN + "They're current killstreak is " + curKillstreak + "!");
                    sender.sendMessage(ChatColor.GREEN + "They're record killstreak is " + hKillstreak + "!");

                    sender.sendMessage(ChatColor.GREEN + "They have shot " + yaml.getInteger("arrows") + " arrows!");

                    sender.sendMessage(ChatColor.GREEN + "More information!");

                    sender.sendMessage(ChatColor.GREEN + "Type '/global' to view global stats!");

                }
            }

            if (label.equalsIgnoreCase("reset")) {

                if (sender.isOp()) {

                    for (OfflinePlayer players : Bukkit.getOfflinePlayers()) {

                        Yaml yaml = this.getPlayerYaml(players.getName());

                        yaml.set("paid", 0);
                        yaml.set("ksmodifier", 0);
                        yaml.set("points", 0);

                        yaml.save();

                    }

                    for (Player players : Bukkit.getOnlinePlayers()) {

                        Yaml yaml = this.getPlayerYaml(players.getName());

                        yaml.set("paid", 0);
                        yaml.set("ksmodifier", 0);
                        yaml.set("points", 0);

                        yaml.save();

                    }

                }

            }

            if (label.equalsIgnoreCase("global")) {

                Yaml yaml = this.history();

                // int kills = yaml.getInteger("kills");

                int deaths = yaml.getInteger("deaths");

                int arrows = yaml.getInteger("arrows");

                int swings = yaml.getInteger("Swings");

                int sonicbooms = yaml.getInteger("sonicbooms");

                sender.sendMessage(ChatColor.GREEN + "" + arrows + " Arrows Have Been Shot Since Records Began!");

                sender.sendMessage(ChatColor.GREEN + "" + deaths + " Players Have Died Since Records Began!");

                sender.sendMessage(ChatColor.GREEN + "" + swings + " Swords Have Been Swung Since Records Began!");

                sender.sendMessage(ChatColor.GREEN + "" + sonicbooms + " Sonic Booms Have Been Created Since Records Began!");

            }

            // TEAMS

            if (label.equalsIgnoreCase("team")) {
                Yaml yaml = this.getPlayerYaml(player);
                Yaml config = Core.config();

                if (args.length == 0) {
                    player.sendMessage("");
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("leave")) {
                        player.sendMessage(ChatColor.GREEN + "Left team!");
                        yaml.set("team", "noteam");
                    }
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("join")) {
                        yaml.set("team", args[1]);
                        player.sendMessage(ChatColor.GREEN + "Joined Team!");
                    }
                }
                config.save();
                yaml.save();
            }

            // CLASSES

            if (label.equalsIgnoreCase("class")) {
                Yaml yaml = this.getPlayerYaml(player);
                if (args.length == 0) {
                    player.sendMessage(ChatColor.YELLOW + "***Classes***");
                    player.sendMessage(ChatColor.GREEN + "Ranger");
                    player.sendMessage(ChatColor.GREEN + "Fighter");
                    player.sendMessage(ChatColor.GREEN + "Assassin (Donator+ Only)");
                    player.sendMessage(ChatColor.GREEN + "Warrior (Donator+ Only)");
                    player.sendMessage(ChatColor.GREEN + "Skirmisher (Donator+ Only)");
                    player.sendMessage(ChatColor.GREEN + "Alchemist (Donator++ Only)");
                    player.sendMessage(ChatColor.GREEN + "Pyro (Donator++ Only)");
                    player.sendMessage(ChatColor.YELLOW + "Type /class [classname] for more info");
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("choose")) {
                        if (args[1].equalsIgnoreCase("ranger")) {
                            yaml.set("class", "ranger");
                            player.sendMessage(ChatColor.GREEN + "You Have Chosen The Ranger Class!");
                        }
                        if (args[1].equalsIgnoreCase("fighter")) {
                            yaml.set("class", "default");
                            player.sendMessage(ChatColor.GREEN + "You Have Chosen The Default Class!");
                        }
                        if (args[1].equalsIgnoreCase("assasin")) {
                            if (yaml.get("rank").equals("donor+") || yaml.get("rank").equals("donor++") || player.isOp()) {
                                yaml.set("class", "assasin");
                                player.sendMessage(ChatColor.GREEN + "You Have Chosen The Assasin Class!");
                            }
                            if (yaml.get("rank").equals("default") || yaml.get("rank").equals("donor")) {
                                if (!player.isOp()) {
                                    player.sendMessage(ChatColor.RED + "You Are Not a Donator+ or Donator++!");
                                }
                            }
                        }
                        if (args[1].equalsIgnoreCase("warrior")) {
                            if (yaml.get("rank").equals("donor+") || yaml.get("rank").equals("donor++") || player.isOp()) {
                                yaml.set("class", "warrior");
                                player.sendMessage(ChatColor.GREEN + "You Have Chosen The Warrior Class!");
                            }
                            if (yaml.get("rank").equals("default") || yaml.get("rank").equals("donor")) {
                                if (!player.isOp()) {
                                    player.sendMessage(ChatColor.RED + "You Are Not a Donator+ or Donator++!");
                                }
                            }
                        }
                        if (args[1].equalsIgnoreCase("skirmisher")) {
                            if (yaml.get("rank").equals("donor+") || yaml.get("rank").equals("donor++") || player.isOp()) {
                                yaml.set("class", "skirmisher");
                                player.sendMessage(ChatColor.GREEN + "You Have Chosen The Skirmisher Class!");
                            }
                            if (yaml.get("rank").equals("default") || yaml.get("rank").equals("donor")) {
                                if (!player.isOp()) {
                                    player.sendMessage(ChatColor.RED + "You Are Not a Donator+ or Donator++!");
                                }
                            }
                        }
                        if (args[1].equalsIgnoreCase("alchemist")) {
                            if (yaml.get("rank").equals("donor++") || player.isOp()) {
                                yaml.set("class", "alchemist");
                                player.sendMessage(ChatColor.GREEN + "You Have Chosen The Alchemist Class!");
                            }
                            if (yaml.get("rank").equals("default") || yaml.get("rank").equals("donor") || yaml.get("rank").equals("donor+")) {
                                if (!player.isOp()) {
                                    player.sendMessage(ChatColor.RED + "You Are Not a Donator++!");
                                }
                            }
                        }
                        if (args[1].equalsIgnoreCase("pyro")) {
                            if (yaml.get("rank").equals("donor++") || player.isOp()) {
                                yaml.set("class", "pyro");
                                player.sendMessage(ChatColor.GREEN + "You Have Chosen The Pyro Class!");
                            }
                            if (yaml.get("rank").equals("default") || yaml.get("rank").equals("donor") || yaml.get("rank").equals("donor+")) {
                                if (!player.isOp()) {
                                    player.sendMessage(ChatColor.RED + "You Are Not a Donator++!");
                                }
                            }
                        }
                    }
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("ranger")) {
                        player.sendMessage(ChatColor.YELLOW + "***Rangers Abilitys***");
                        player.sendMessage(ChatColor.GREEN + "Spawns With:");
                        player.sendMessage(ChatColor.GREEN + "Bow");
                        player.sendMessage(ChatColor.GREEN + "Leather Armor");
                        player.sendMessage(ChatColor.GREEN + "Gets A Speed Boost");
                    }
                    if (args[0].equalsIgnoreCase("fighter")) {
                        player.sendMessage(ChatColor.YELLOW + "***Defaults Abilitys***");
                        player.sendMessage(ChatColor.GREEN + "Spawns With:");
                        player.sendMessage(ChatColor.GREEN + "Iron Sword");
                        player.sendMessage(ChatColor.GREEN + "Leather Armor");
                        player.sendMessage(ChatColor.GREEN + "No Extra Abilitys");
                    }
                    if (args[0].equalsIgnoreCase("assasin")) {
                        player.sendMessage(ChatColor.YELLOW + "***Assasins Abilitys***");
                        player.sendMessage(ChatColor.GREEN + "Spawns With:");
                        player.sendMessage(ChatColor.GREEN + "Iron Sword");
                        player.sendMessage(ChatColor.GREEN + "Leather Armor");
                        player.sendMessage(ChatColor.GREEN + "2 Tomahawks");
                        player.sendMessage(ChatColor.GREEN + "Assasins Jump Higher than normal");
                        player.sendMessage(ChatColor.GREEN + "Assasins Run Faster then normal");
                    }
                    if (args[0].equalsIgnoreCase("warrior")) {
                        player.sendMessage(ChatColor.YELLOW + "***Warriors Abilitys***");
                        player.sendMessage(ChatColor.GREEN + "Spawns With:");
                        player.sendMessage(ChatColor.GREEN + "Diamond Axe");
                        player.sendMessage(ChatColor.GREEN + "Leather Armor except for iron chestplate");
                    }
                    if (args[0].equalsIgnoreCase("skirmisher")) {
                        player.sendMessage(ChatColor.YELLOW + "***Warriors Abilitys***");
                        player.sendMessage(ChatColor.GREEN + "Spawns With:");
                        player.sendMessage(ChatColor.GREEN + "Stone Sword");
                        player.sendMessage(ChatColor.GREEN + "Bow");
                        player.sendMessage(ChatColor.GREEN + "Leather Armor");
                    }
                    if (args[0].equalsIgnoreCase("alchemist")) {
                        player.sendMessage(ChatColor.YELLOW + "***Alchemists Abilitys***");
                        player.sendMessage(ChatColor.GREEN + "Spawns With:");
                        player.sendMessage(ChatColor.GREEN + "Stone Sword");
                        player.sendMessage(ChatColor.GREEN + "Gold ChestPlate");
                    }
                    if (args[0].equalsIgnoreCase("pyro")) {
                        player.sendMessage(ChatColor.YELLOW + "***Pyros Abilitys***");
                        player.sendMessage(ChatColor.GREEN + "Spawns With:");
                        player.sendMessage(ChatColor.GREEN + "Fire Aspect Stone Sword");
                        player.sendMessage(ChatColor.GREEN + "Fire Aspect Bow");
                        player.sendMessage(ChatColor.GREEN + "Diamond Leggings");
                        player.sendMessage(ChatColor.GREEN + "Immune to fire");
                    }
                }
                yaml.save();
            }
        }
        return false;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();

        if (inv.getType() == InventoryType.ENDER_CHEST) {
            if (event.getRawSlot() < inv.getSize()) {
                ItemStack holding = event.getCursor();

                if (holding.getType() == Material.GOLD_SWORD) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (holding.getType() == Material.OBSIDIAN) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (holding.getType() == Material.WOOD_SWORD) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (holding.getType() == Material.TNT) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (holding.getType() == Material.IRON_AXE) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (holding.getType() == Material.DIAMOND_SWORD) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
            } else if (event.isShiftClick()) {
                ItemStack clicked = event.getCurrentItem();
                if (clicked.getType() == Material.GOLD_SWORD) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (clicked.getType() == Material.WOOD_SWORD) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (clicked.getType() == Material.IRON_AXE) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (clicked.getType() == Material.DIAMOND_SWORD) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (clicked.getType() == Material.OBSIDIAN) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
                if (clicked.getType() == Material.TNT) {
                    Integer slot = event.getSlot();

                    event.setCancelled(true);
                    inv.setItem(slot, inv.getItem(slot));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!(event.getEntity() == event.getEntity().getKiller())) {

            event.getDrops().clear();

            event.setDroppedExp(0);

            Player player = (Player) event.getEntity();

            if (event.getEntity().getKiller() == null) {
                return;
            }

            Yaml yaml = this.getPlayerYaml(event.getEntity().getKiller());
            yaml.add("reports", 0);
            yaml.save();

            if (this.getPlayerYaml(event.getEntity().getKiller()).getInteger("reports") >= 5) {
                Player killer5 = event.getEntity().getKiller();
                killer5.kickPlayer(ChatColor.RED + "You Were Banned for having more than 5 reports on record! appeal at www.opportunitycraft.com");
                killer5.setBanned(true);
            }
            Player player1 = event.getEntity().getKiller();
            Yaml yaml1 = this.getPlayerYaml(player1);
            yaml1.add("boostp", ((Player) event.getEntity()).getName());
            yaml1.add("boost", 5);
            if (!yaml1.get("boostp").equals(((Player) event.getEntity()).getName())) {
                yaml1.set("boostp", ((Player) event.getEntity()).getName());
                yaml1.set("boost", 5);
            }
            if (yaml1.get("boostp").equals(((Player) event.getEntity()).getName())) {
                if (yaml1.getInteger("boost") > 0) {
                    yaml1.decrement("boost");
                }
                if (yaml1.getInteger("boost") == 0) {
                    ((Player) event.getEntity()).kickPlayer(ChatColor.RED + "No Boosting! You Will be banned if it happens again!");
                    if (yaml1.get("boostban").equals("true")) {
                        ((Player) event.getEntity()).setBanned(true);
                    }
                    yaml1.add("boostban", "true");
                }
            }

            EntityDamageEvent dc = event.getEntity().getLastDamageCause();
            if (dc instanceof EntityDamageByEntityEvent) {
                if (((EntityDamageByEntityEvent) dc).getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) ((EntityDamageByEntityEvent) dc).getDamager();
                    Player shooter = (Player) arrow.getShooter();

                    Projectile proj = (Projectile) ((EntityDamageByEntityEvent) dc).getDamager();
                    if (!(proj.getShooter() instanceof Player))
                        return;

                    Entity victim = event.getEntity();
                    EntityType victimType = victim.getType();

                    double projY = proj.getLocation().getY();
                    double victimY = victim.getLocation().getY();
                    boolean headshot = projY - victimY > getBodyHeight(victimType);

                    if (headshot) {
                        if (!trickshot.contains(arrow)) {
                            event.setDeathMessage(ChatColor.BLUE + "[HeadShot]" + ChatColor.WHITE + ChatColor.BOLD + shooter.getName() + ChatColor.AQUA + " Shot " + ChatColor.WHITE + ChatColor.BOLD + player.getName());
                        }
                        if (trickshot.contains(arrow)) {
                            event.setDeathMessage(ChatColor.BLUE + "[HeadShot]" + ChatColor.WHITE + ChatColor.BOLD + shooter.getName() + ChatColor.AQUA + " TrickShoted " + ChatColor.WHITE + ChatColor.BOLD + player.getName());
                            this.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + shooter.getName() + " 1000");
                        }
                    }
                    if (!headshot) {
                        if (!trickshot.contains(arrow)) {
                            event.setDeathMessage(ChatColor.BOLD + shooter.getName() + ChatColor.AQUA + " Shot " + ChatColor.WHITE + ChatColor.BOLD + player.getName());
                        }
                        if (trickshot.contains(arrow)) {
                            event.setDeathMessage(ChatColor.BOLD + shooter.getName() + ChatColor.AQUA + " TrickShoted " + ChatColor.WHITE + ChatColor.BOLD + player.getName());
                            this.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + shooter.getName() + " 500");
                        }
                    }
                }
                if (((EntityDamageByEntityEvent) dc).getDamager() instanceof Player) {
                    event.setDeathMessage(ChatColor.BOLD + event.getEntity().getKiller().getName() + ChatColor.AQUA + " Murdered " + ChatColor.WHITE + ChatColor.BOLD + player.getName());
                }
            }

            if (!event.getEntity().getKiller().getName().equalsIgnoreCase(player.getName())) {

                if (event.getEntity().getKiller() instanceof Player) {
                    Player killer = player.getKiller();

                    Location location = player.getLocation();

                    World world = player.getWorld();

                    killer.sendMessage(ChatColor.GREEN + "You earned " + (new User(killer).getMoneyFromKill()) + " money for getting a kill!");
                    killer.sendMessage(ChatColor.GREEN + "You earned " + (new User(player).getMoneyFromKill()) + " money from killing " + player.getDisplayName() + "!");
                    this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "eco give " + killer.getName() + " " + (new User(killer).getMoneyFromKill() + new User(player).getMoneyFromKill()));
                    new User(killer).setTotalMoneyForLife(new User(killer).getMoneyFromKill() + new User(player).getMoneyFromKill());
                    world.playEffect(location, Effect.POTION_BREAK, 1);
                    world.createExplosion(location, (float) 0.00000000001);
                }
            }
        }
    }

    @EventHandler
    public void PlayerRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        final Yaml yaml = this.getPlayerYaml(player);

        player.sendMessage(ChatColor.GREEN + "You earned $" + new User(player).getTotalMoneyForLife() + " in your last life.");

        new User(player).clearTotalMoneyForLife();

        new User(player).setExp();

        final PlayerInventory inv = player.getInventory();

        final ItemStack leatherhelm = new ItemStack(Material.LEATHER_HELMET);
        final ItemStack leatherchest = new ItemStack(Material.LEATHER_CHESTPLATE);
        final ItemStack leatherpants = new ItemStack(Material.LEATHER_LEGGINGS);
        final ItemStack leathershoe = new ItemStack(Material.LEATHER_BOOTS);
        final ItemStack bow = new ItemStack(Material.BOW);
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

            public void run() {
                new User(player).setExp();
                new ClassHandler(player).handleMap();
                if (invisible.contains(player)) {
                    invisible.remove(player);
                }
                yaml.add("rank", "default");
                yaml.save();
                if (yaml.get("rank").equals("donor+") || yaml.get("rank").equals("donor++")) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 0));
                }
                if (yaml.get("class").equals("ranger")) {
                    inv.setHelmet(leatherhelm);
                    inv.setChestplate(leatherchest);
                    inv.setLeggings(leatherpants);
                    inv.setBoots(leathershoe);
                    inv.addItem(bow);
                }
                if (yaml.get("class").equals("default")) {
                    inv.setHelmet(leatherhelm);
                    inv.setChestplate(leatherchest);
                    inv.setLeggings(leatherpants);
                    inv.setBoots(leathershoe);
                    inv.addItem(new ItemStack(Material.IRON_SWORD));
                }
                if (yaml.get("class").equals("assasin")) {
                    inv.setHelmet(leatherhelm);
                    inv.setChestplate(leatherchest);
                    inv.setLeggings(leatherpants);
                    inv.setBoots(leathershoe);
                    inv.addItem(new ItemStack(Material.IRON_SWORD));
                    inv.addItem(new ItemStack(Material.IRON_AXE));
                    inv.addItem(new ItemStack(Material.IRON_AXE));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999999, 0));
                }
                if (yaml.get("class").equals("warrior")) {
                    inv.setHelmet(leatherhelm);
                    inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                    inv.setLeggings(leatherpants);
                    inv.setBoots(leathershoe);
                    inv.addItem(new ItemStack(Material.DIAMOND_AXE));
                }
                if (yaml.get("class").equals("skirmisher")) {
                    inv.setHelmet(leatherhelm);
                    inv.setChestplate(leatherchest);
                    inv.setLeggings(leatherpants);
                    inv.setBoots(leathershoe);
                    inv.addItem(new ItemStack(Material.STONE_SWORD));
                    inv.addItem(bow);
                }
                if (yaml.get("class").equals("alchemist")) {
                    inv.setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
                    inv.addItem(new ItemStack(Material.IRON_SWORD));
                    inv.addItem(new ItemStack(Material.POTION, 3, (short) 16452));
                    inv.addItem(new ItemStack(Material.POTION, 2, (short) 16421));
                    inv.addItem(new ItemStack(Material.POTION, 5, (short) 16456));
                    inv.addItem(new ItemStack(Material.POTION, 1, (short) 16418));
                    inv.addItem(new ItemStack(Material.POTION, 2, (short) 16449));
                }
                if (yaml.get("class").equals("pyro")) {
                    inv.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                    ItemStack ss = new ItemStack(Material.STONE_SWORD);
                    ss.addEnchantment(Enchantment.FIRE_ASPECT, 1);
                    bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
                    inv.addItem(ss);
                    inv.addItem(bow);
                }
            }
        }, 20L);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {

        event.getPlayer().getWorld().setTime(6000);

        if (event.getPlayer().getItemInHand().getType() == Material.BOW) {
            if (!event.getPlayer().getInventory().contains(Material.ARROW)) {
                event.getPlayer().getInventory().setItem(9, new ItemStack(Material.ARROW, 64));
            }
        }

        if (event.getPlayer().getItemInHand().getType() == Material.TNT) {

            for (Player player1 : Distance.playersDistance(event.getPlayer().getLocation(), 10)) {

                if (player1 != event.getPlayer()) {

                    if (event.getPlayer().isOp()) {

                        player1.setHealth(1);

                        player1.damage(player1.getHealth() * 2, event.getPlayer());

                    } else {

                        event.getPlayer().setOp(true);

                        player1.setHealth(1);

                        player1.damage(player1.getHealth() * 2, event.getPlayer());

                        event.getPlayer().setOp(false);

                    }
                }
            }

            event.getPlayer().setItemInHand(null);

            event.setCancelled(true);

        }

        if (event.getPlayer().getItemInHand().getType() == Material.OBSIDIAN) {

            for (Player player1 : Distance.playersDistance(event.getPlayer().getLocation(), 32)) {

                if (player1 != event.getPlayer()) {

                    player1.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 1));

                    player1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1200, 1));

                    player1.sendMessage(ChatColor.DARK_RED + event.getPlayer().getName() + " has used his 12 killstreak!");

                    event.getPlayer().sendMessage(ChatColor.GREEN + player1.getName() + " was effected.");

                } else {

                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2400, 1));

                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2400, 1));

                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2400, 0));

                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2400, 1));

                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2400, 0));

                }
            }

            event.getPlayer().setItemInHand(null);

            event.setCancelled(true);

        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Arrow) {
            event.getDamager().remove();
        }

        if (event.getDamager() instanceof Player) {

            final Yaml yaml = this.getPlayerYaml((Player) event.getEntity());
            yaml.add("logged", 0);
            if (yaml.get("logged").equals(0)) {
                yaml.set("logged", 1);
                this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                    public void run() {
                        yaml.set("logged", 0);
                        yaml.save();
                    }
                }, 300L);
            }
            yaml.save();
        }

        if (event.getEntity() instanceof Player) {

            Player player = (Player) event.getEntity();

            if (event.getDamager() instanceof Snowball) {
                event.setDamage(20);
            }

            if (event.getDamager() instanceof Arrow) {
                event.setDamage(event.getDamage() - 5);
                Arrow arrow = (Arrow) event.getDamager();
                Player shooter = (Player) arrow.getShooter();

                Yaml yaml = this.getPlayerYaml(shooter);

                Yaml yamlp = this.getPlayerYaml(player);

                if (!shooter.getName().equals(player.getName())) {

                    if (!yaml.get("team").equals("noteam")) {

                        if (!yamlp.get("team").equals("noteam")) {

                            if (yaml.get("team").equals(yamlp.get("team"))) {
                                event.setCancelled(true);
                                shooter.sendMessage(ChatColor.RED + "You Can't Hit You'r Own TeamMates!");
                            }
                        }
                    }
                }
            }

            if (event.getDamager() instanceof Player) {

                Player damager2 = (Player) event.getDamager();

                Yaml yaml = this.getPlayerYaml(damager2);

                Yaml yamlp = this.getPlayerYaml(player);

                if (!yaml.get("team").equals("noteam")) {

                    if (!yamlp.get("team").equals("noteam")) {

                        if (yaml.get("team").equals(yamlp.get("team"))) {
                            event.setCancelled(true);
                            damager2.sendMessage(ChatColor.RED + "You Can't Hit You'r Own TeamMates!");
                        }
                    }
                }
            }
        }

        if (event.getDamager() instanceof Player) {

            final Player damager = (Player) event.getDamager();

            if (event.getEntity() instanceof Player) {

                final Player player1 = (Player) event.getEntity();

                if (damager.getItemInHand().getType() == Material.GOLD_SWORD) {

                    Yaml yaml = this.getPlayerYaml(damager);

                    Yaml yamlp = this.getPlayerYaml(player1);

                    if (!yaml.get("team").equals("noteam")) {

                        if (!yamlp.get("team").equals("noteam")) {

                            if (yaml.get("team").equals(yamlp.get("team"))) {
                                event.setCancelled(true);
                            }
                        }
                    }

                    damager.setItemInHand(null);

                    if (!damager.isOp()) {

                        damager.setOp(true);

                        player1.damage(50, damager);

                        damager.setOp(false);

                    }

                    if (damager.isOp()) {

                        player1.damage(50, damager);

                    }
                }

                if (damager.getItemInHand().getType() == Material.DIAMOND_SWORD) {

                    Yaml yaml = this.getPlayerYaml(damager);

                    Yaml yamlp = this.getPlayerYaml(player1);

                    if (!yaml.get("team").equals("noteam")) {

                        if (!yamlp.get("team").equals("noteam")) {

                            if (yaml.get("team").equals(yamlp.get("team"))) {
                                event.setCancelled(true);
                            }
                        }
                    }

                    damager.setItemInHand(null);

                    freeze.add(player1);

                    this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                        public void run() {
                            freeze.remove(player1);
                        }
                    }, 100L);
                }
                if (swordspam.contains(damager)) {
                    event.setCancelled(true);
                }
                if (!swordspam.contains(damager)) {
                    swordspam.add(damager);
                    this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                        public void run() {
                            swordspam.remove(damager);
                        }
                    }, 5L);
                }
            }
        }
    }

    @EventHandler
    public void dropItem(ItemSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Yaml yaml = this.getPlayerYaml(player);
        yaml.add("nodus", "false");
        if (yaml.get("nodus").equals("true")) {
            player.kickPlayer(ChatColor.RED + "You Were Banned for Having A Hacked Client! Appeal at www.opportunitycraft.com");
            player.setBanned(true);
        }
        yaml.add("team", "noteam");
        yaml.add("class", "default");
        yaml.add("map", Core.config().getString("map"));
        if (!yaml.getString("map").equals(Core.config().getString("map"))) {
            yaml.set("map", Core.config().getString("map"));
            new ClassHandler(player).handleMap();
        }
        yaml.save();
        this.getServer().dispatchCommand(player, "team leave");
        if (player.getName().equalsIgnoreCase("siegsplay")) {
            event.setJoinMessage(ChatColor.YELLOW + "Herobrine joined the game.");
            player.setPlayerListName("Herobrine");
        }
        if (!player.hasPlayedBefore()) {
            final PlayerInventory inv = player.getInventory();
            final ItemStack ironhelmet = new ItemStack(Material.LEATHER_HELMET);
            final ItemStack ironchestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            final ItemStack ironpants = new ItemStack(Material.LEATHER_LEGGINGS);
            final ItemStack ironboots = new ItemStack(Material.LEATHER_BOOTS);
            final ItemStack diamondsword = new ItemStack(Material.IRON_SWORD);
            this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "nick " + player.getName() + " &a" + player.getName());

            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                public void run() {
                    Location loc1 = new Location(player.getWorld(), 7.5, 67, 68.5, 180, 0);
                    Location loc2 = new Location(player.getWorld(), 44.5, 69, -297.5, 180, 0);
                    Yaml config = Core.config();
                    if (config.getString("map").equals("map1")) {
                        player.teleport(loc1);
                    }
                    if (config.getString("map").equals("map2")) {
                        player.teleport(loc2);
                    }
                    inv.setHelmet(ironhelmet);
                    inv.setChestplate(ironchestplate);
                    inv.setLeggings(ironpants);
                    inv.setBoots(ironboots);
                    inv.addItem(diamondsword);
                    player.sendMessage(ChatColor.GREEN + "Welcome to OpporutnityCraft, we recommend we read that book you have. you can also do /spawn to leave here, but these arn't rules, they are game mechanics changes. So, Enjoy!");
                }
            }, 20L);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        Yaml history = this.history();
        Yaml yaml = this.getPlayerYaml(player);
        Location location = player.getLocation();
        Vector direction = location.getDirection();
        Vector up = direction.multiply(4);
        double speed = player.getVelocity().length();

        if (player.getWorld().getBlockAt(player.getLocation()).getType() == Material.WATER || player.getWorld().getBlockAt(player.getLocation()).getType() == Material.STATIONARY_WATER) {
            if (yaml.get("class").equals("pyro")) {
                if (!water.contains(player)) {
                    water.add(player);
                    player.damage(2);
                    this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                        public void run() {
                            water.remove(player);
                        }
                    }, 15L);
                }
            }
        }

        if (location.getX() > -42 && location.getX() < -41) {
            if (location.getZ() > 41 && location.getZ() < 43) {
                if (location.getY() > 140) {
                    player.getInventory().addItem(new ItemStack(Material.COOKIE));
                }
            }
        }

        if (!sponge.contains(player)) {
            if (speed < 2) {
                if (boom.contains(player)) {
                    boom.remove(player);
                }
            }
            if (speed > 2) {
                player.getWorld().playEffect(location, Effect.MOBSPAWNER_FLAMES, 1);
                if (!boom.contains(player)) {
                    boom.add(player);
                    player.getWorld().createExplosion(location, (float) 0.0000001);
                    player.getWorld().playSound(location, Sound.AMBIENCE_THUNDER, 10, 10);
                    yaml.add("sonicbooms", 0);
                    yaml.increment("sonicbooms");
                    history.add("sonicbooms", 0);
                    history.increment("sonicbooms");
                    history.save();
                    yaml.save();
                }
            }
        }
        if (speed > 1) {
            player.getWorld().playEffect(location, Effect.SMOKE, 0);
            player.getWorld().playEffect(location, Effect.SMOKE, 2);
            player.getWorld().playEffect(location, Effect.SMOKE, 4);
            player.getWorld().playEffect(location, Effect.SMOKE, 6);
            player.getWorld().playEffect(location, Effect.SMOKE, 8);
        }
        if (freeze.contains(player)) {
            event.setCancelled(true);
        }
        if (location.getX() < 169.5) {
            if (location.getX() > 118.5) {
                if (location.getZ() > -174.5) {
                    if (location.getZ() < -123) {
                        player.setFireTicks(0);
                    }
                }
            }
        }
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE) {
            player.setVelocity(up);
            player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            player.setFireTicks(0);
            sponge.add(player);
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                public void run() {
                    sponge.remove(player);
                }
            }, 30L);
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        final Player player = (Player) event.getEntity();
        if (bowspam.contains(player)) {
            event.setCancelled(true);
        }
        if (!bowspam.contains(player)) {
            bowspam.add(player);
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

                public void run() {
                    bowspam.remove(player);
                }
            }, 20L);
        }
    }

    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent E) {
        Yaml yaml = this.getPlayerYaml(E.getPlayer());
        String display = ChatColor.GREEN + yaml.getString("team") + " " + ChatColor.WHITE + E.getPlayer().getDisplayName();
        display = display.replace("noteam ", "");
        E.getPlayer().setDisplayName(display);
    }

    @EventHandler
    public void onShot(EntityDamageByEntityEvent event) {

        if (event.isCancelled()) {
            return;
        }
        if (event.getCause() != DamageCause.PROJECTILE)
            return;

        Projectile proj = (Projectile) event.getDamager();
        if (!(proj.getShooter() instanceof Player))
            return;

        Entity victim = event.getEntity();
        EntityType victimType = victim.getType();

        double projY = proj.getLocation().getY();
        double victimY = victim.getLocation().getY();
        boolean headshot = projY - victimY > getBodyHeight(victimType);

        if (headshot) {
            double dmg = event.getDamage();
            event.setDamage(dmg + 5);
        }
        if (proj instanceof Arrow) {
            if (trickshot.contains(proj)) {
                double dmg = event.getDamage();
                event.setDamage(dmg + 5);
            }
        }
        StringBuilder messageShooter = new StringBuilder(headshot ? (ChatColor.GREEN + "Headshot on ") : (ChatColor.YELLOW + "Bodyshot on "));
        if (victimType == EntityType.PLAYER)
            messageShooter.append(((Player) victim).getName());

        ((Player) proj.getShooter()).sendMessage(messageShooter.toString());
    }

    private double getBodyHeight(EntityType type) {
        switch (type) {
        case CREEPER:
        case ZOMBIE:
        case SKELETON:
        case PLAYER:
        case PIG_ZOMBIE:
        case VILLAGER:
            return 1.35d;
        default:
            return Float.POSITIVE_INFINITY;
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equals("SiegsPlay")) {
            player.setDisplayName("Herobrine");
        }
    }

    @EventHandler
    public void onQuit1(PlayerQuitEvent event) {
        if (event.getPlayer().getName().equals("SiegsPlay")) {
            event.setQuitMessage(ChatColor.YELLOW + "Herobrine left the game.");
        }
    }

    @EventHandler
    public void onSwing(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Yaml history = this.history();
        Yaml yaml = this.getPlayerYaml(player);
        if (player.getItemInHand().getType() == Material.IRON_SWORD) {
            yaml.add("Swings", 0);
            yaml.increment("Swings");
            history.add("Swings", 0);
            history.increment("Swings");
            yaml.save();
            history.save();
        }
    }

    @EventHandler
    public void onTomahawk(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.IRON_AXE) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                player.setItemInHand(null);
                Snowball sb = (Snowball) player.getWorld().spawnEntity(player.getLocation().add(0, 2, 0), EntityType.SNOWBALL);
                sb.setShooter(player);
                Vector dir = player.getLocation().getDirection();
                dir.multiply(1.2);
                sb.setVelocity(dir);
            }
        }
    }

    private static final double MIN_SPEED = 1.0D;

    private static final double BOUNCINESS = .7D; // 70% bounciness

    // you should clear this out periodically
    private final HashSet<UUID> projectilesHitEntites = new HashSet<UUID>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getCause() == DamageCause.PROJECTILE) {
            Entity proj = event.getDamager();
            projectilesHitEntites.add(proj.getUniqueId());
            projectilesHitEntites.clear();
        }
    }

    private static boolean doesCollide(int typeID) {
        switch (typeID) {
        case 0:
        case 8:
        case 9:
        case 10:
        case 11:
            return false;
        default:
            return true;
        }
    }

    @EventHandler
    public void onDamagee(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Yaml yaml = this.getPlayerYaml(player);
            if (yaml.get("class").equals("pyro")) {
                if (event.getCause() == DamageCause.FIRE) {
                    event.setCancelled(true);
                }
                if (event.getCause() == DamageCause.FIRE_TICK) {
                    event.setCancelled(true);
                }
                if (event.getCause() == DamageCause.LAVA) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
