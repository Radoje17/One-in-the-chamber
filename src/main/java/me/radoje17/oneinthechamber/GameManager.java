package me.radoje17.oneinthechamber;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GameManager implements Listener {

    private ArrayList<Player> players = new ArrayList<Player> ();

    private OneInTheChamber plugin;

    public GameManager(OneInTheChamber plugin) {
        this.plugin = plugin;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void quitALL() {
        Location l = plugin.getSpawnManager().getLobby();
        for (Player p : players) {
            p.getInventory().clear();
            p.teleport(plugin.getSpawnManager().getLobby());
            p.sendMessage(ChatColor.GRAY + "Zbog restartovanja servera ste izbačeni iz igre!");
        }
    }

    private void dajIteme(Player p) {
        p.getInventory().addItem(mac());
        p.getInventory().addItem(luk());
        p.getInventory().addItem(strela());
        p.getInventory().setItem(17, quit());
    }

    private ItemStack mac() {
        ItemStack mac = new ItemStack(Material.STONE_SWORD, 1);
        ItemMeta meta = mac.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Mač");
        mac.setItemMeta(meta);
        net.minecraft.server.v1_8_R3.ItemStack nsm = CraftItemStack.asNMSCopy(mac);
        NBTTagCompound compound = (nsm.hasTag()) ? nsm.getTag() : new NBTTagCompound();
        compound.setBoolean("Unbreakable", true);
        nsm.setTag(compound);
        return CraftItemStack.asBukkitCopy(nsm);
    }

    private ItemStack luk() {
        ItemStack luk = new ItemStack(Material.BOW, 1);
        ItemMeta meta = luk.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Luk");
        luk.setItemMeta(meta);
        net.minecraft.server.v1_8_R3.ItemStack nsm = CraftItemStack.asNMSCopy(luk);
        NBTTagCompound compound = (nsm.hasTag()) ? nsm.getTag() : new NBTTagCompound();
        compound.setBoolean("Unbreakable", true);
        nsm.setTag(compound);
        return CraftItemStack.asBukkitCopy(nsm);
    }

    private ItemStack strela() {
        ItemStack strela = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = strela.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Strela");
        strela.setItemMeta(meta);
        return strela;
    }

    private ItemStack quit() {
        ItemStack quit = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta meta = quit.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Napusti igru");
        quit.setItemMeta(meta);
        return quit;
    }

    public void addPlayer(Player p) {
        players.add(p);
        p.setGameMode(GameMode.ADVENTURE);
        Location spawnpoint = plugin.getSpawnManager().getRandomSpawn();
        p.getInventory().clear();
        p.teleport(spawnpoint);
        dajIteme(p);
        p.setHealth(20);
        p.setFoodLevel(20);
        plugin.getSignListener().updateSigns();
    }

    public void removePlayer(Player p) {
        players.remove(p);
        p.getInventory().clear();
        p.teleport(plugin.getSpawnManager().getLobby());
        plugin.getSignListener().updateSigns();

    }

    public boolean inGame(Player p) {
        return players.contains(p);
    }

    @EventHandler
    public void falldamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player && e.getDamager()instanceof Player && !(inGame((Player) e.getEntity()) && inGame((Player) e.getDamager()))) {
            e.setCancelled(true);
            return;
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE || e.getDamager() instanceof Projectile) {
            Projectile p = (Projectile) e.getDamager();
            if (p.getShooter() == e.getEntity()) {
                e.setCancelled(true);
                return;
            }
            e.setDamage(20);
            return;
        }
        /*if (e.getEntity() instanceof Player && e.getDamager() instanceof Player &&
                e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && players.contains(e.getDamager())) {

            ((Player) e.getEntity()).setHealth(0);

            plugin.getStats().addStat(plugin.getUuidManager().getUUID(e.getEntity().getName()), "deaths");
            plugin.getStats().addStat(plugin.getUuidManager().getUUID(e.getDamager().getName()), "kills");


            ((Player) e.getDamager()).getInventory().addItem(strela());
            for (Player p : players) {
                p.sendMessage(ChatColor.YELLOW + e.getDamager().getName() + ChatColor.GRAY + " je upucao " + ChatColor.YELLOW + e.getEntity().getName() + ChatColor.GRAY + "!");
            }
        }*/
    }

    @EventHandler
    public void kill(PlayerDeathEvent e) {
        e.getDrops().clear();
        e.setDeathMessage("");
        if (e.getEntity().getKiller() instanceof Player) {
            plugin.getStats().addStat(plugin.getUuidManager().getUUID(e.getEntity().getName()), "deaths");
            plugin.getStats().addStat(plugin.getUuidManager().getUUID(e.getEntity().getKiller().getName()), "kills");
            e.getEntity().getKiller().getInventory().addItem(strela());
            e.getEntity().getKiller().setHealth(20);
            for (Player p : players) {
                p.sendMessage(ChatColor.YELLOW + e.getEntity().getKiller().getName() + ChatColor.GRAY + " je ubio " + ChatColor.YELLOW + e.getEntity().getName() + ChatColor.GRAY + "!");
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                ((CraftPlayer) e.getEntity()).getHandle().getDataWatcher().watch(9, (byte) 0);
                e.getEntity().spigot().respawn();
            }
        }, 3L);
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        removePlayer(e.getPlayer());
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (inGame((Player) e.getWhoClicked())) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage(ChatColor.GRAY + "Izašali ste iz igre.");
                removePlayer((Player) e.getWhoClicked());
            }
        }
    }

    @EventHandler
    public void hit(ProjectileHitEvent e) {
        e.getEntity().remove();
    }

    @EventHandler
    public void foodChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        if (inGame(e.getPlayer())) {
            e.getPlayer().getInventory().clear();
            e.setRespawnLocation(plugin.getSpawnManager().getRandomSpawn());
            dajIteme(e.getPlayer());
        }
    }

    @EventHandler
    public void drop(PlayerDropItemEvent e) {
        if (inGame(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void drop(PlayerPickupItemEvent e) {
        if (inGame(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void breakEvent(BlockBreakEvent e) {
        if (inGame(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().toLowerCase().startsWith("/play")) {
            e.setCancelled(true);
            e.getPlayer().chat("/oitc join");
            return;
        }

        if (inGame(e.getPlayer())) {
            if (!e.getMessage().toLowerCase().startsWith("/oitc") &&
                    !e.getMessage().toLowerCase().startsWith("/oneinthechamber")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Ne možete da koristite komande tokom igre.");
            }
        }
    }

    @EventHandler
    public void weatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        e.setFormat(ChatColor.WHITE + e.getPlayer().getName() + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + e.getMessage().replace('&', '§'));
    }
}