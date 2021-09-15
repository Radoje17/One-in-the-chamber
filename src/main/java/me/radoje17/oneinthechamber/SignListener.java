package me.radoje17.oneinthechamber;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SignListener implements Listener {

    private File f;
    private FileConfiguration data;

    private OneInTheChamber plugin;

    public SignListener(OneInTheChamber plugin) {
        this.plugin = plugin;
        refreshData();
    }

    public void refreshData() {
        this.f = new File(Bukkit.getWorldContainer() + "/plugins/OneInTheChamber/signs.yml");
        this.data = YamlConfiguration.loadConfiguration(f);
    }

    public void updateSigns() {
        for (Location l : getLocations()) {
            Sign sign = (Sign) l.getBlock().getState();
            sign.setLine(0, ChatColor.DARK_GRAY + "[OITC]");
            int players = plugin.getGameManager().getPlayers().size();
            if (players == 1) {
                sign.setLine(1, "" + ChatColor.BOLD + 1 + ChatColor.RESET +" igrac čeka");
            } else if (players <= 3) {
                sign.setLine(1, "" + ChatColor.BOLD + players + ChatColor.RESET +" igraca igra");
            } else {
                sign.setLine(1, "" + ChatColor.BOLD + players + ChatColor.RESET +" igraca igraju");
            }
            sign.setLine(2, "");
            sign.setLine(3, "");
            sign.update();
        }
    }

    private void saveData() {
        try {
            data.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void place(SignChangeEvent e) {
        if (e.getLine(0).equals("[OITC]")) {
            addSign(e.getBlock().getLocation());
            updateSigns();
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if (e.getBlock() != null && e.getBlock().getType().toString().toLowerCase().contains("sign")) {
            removeSign(e.getBlock().getLocation());
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null && getLocations().contains(e.getClickedBlock().getLocation())) {
            e.getPlayer().chat("/oitc join");
        }
    }

    public ArrayList<String> getSigns() {
        return (data.getStringList("signs") != null) ? (ArrayList<String>) data.getStringList("signs") : new ArrayList<String> ();
    }

    public ArrayList<Location> getLocations() {
        ArrayList<Location> list = new ArrayList<Location>();

        for (String s : getSigns()) {
            String[] lista = s.split(";");
            list.add(new Location(Bukkit.getWorld(lista[0]), Double.parseDouble(lista[1]), Double.parseDouble(lista[2]), Double.parseDouble(lista[3])));
        }

        return list;
    }

    public void addSign(Location l) {
        ArrayList<String> list = getSigns();
        list.add(l.getWorld().getName() + ";" + l.getX() + ";" + l.getY() + ";" + l.getZ());
        data.set("signs", list);
        saveData();
    }

    public void removeSign(Location l) {
        ArrayList<String> list = getSigns();
        list.remove(l.getWorld().getName() + ";" + l.getX() + ";" + l.getY() + ";" + l.getZ());
        data.set("signs", list);
        saveData();
    }

}
