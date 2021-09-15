package me.radoje17.oneinthechamber;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;

public class UUIDManager implements Listener {

    private File f;
    private FileConfiguration data;

    public UUIDManager() {
        refreshData();
    }

    private void refreshData() {
        this.f = new File(Bukkit.getWorldContainer() + "/uuids.yml");
        data = YamlConfiguration.loadConfiguration(f);
    }

    private void saveData() {
        try {
            data.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUUID(String player) {
        return data.getString("uuids." + Bukkit.getOfflinePlayer(player).getName());
    }

    public String getUUID(Player player) {
        return data.getString("uuids." + player.getName());
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        data.set("uuids." + e.getPlayer().getName(), e.getPlayer().getUniqueId().toString());
        saveData();
        e.setJoinMessage(ChatColor.YELLOW + e.getPlayer().getName() + ChatColor.GRAY + " nam se pridružio.");
        e.getPlayer().teleport(((OneInTheChamber)Bukkit.getPluginManager().getPlugin("OneInTheChamber")).getSpawnManager().getLobby());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.YELLOW + e.getPlayer().getName() + ChatColor.GRAY + " nas je napustio.");
    }
}
