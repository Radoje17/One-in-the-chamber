package me.radoje17.oneinthechamber;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SpawnManager {

    private File f;
    private YamlConfiguration data;

    public SpawnManager() {
        refreshData();
    }

    public void refreshData() {
        this.f = new File(Bukkit.getWorldContainer() + "/plugins/OneInTheChamber/spawnpoints.yml");
        this.data = YamlConfiguration.loadConfiguration(f);
    }

    private void saveData() {
        try {
            data.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getSpawnpointAmount() {
        return data.getStringList("spawnpoints").size();
    }

    public ArrayList<String> getSpawnpoints() {
        return (ArrayList<String>) data.getStringList("spawnpoints");
    }

    public void addSpawn(Location l) {
        ArrayList<String> spawnpoints = getSpawnpoints();
        spawnpoints.add(l.getX() + ";" + l.getY() + ";" + l.getZ() + ";" + l.getYaw() + ";" + l.getPitch() + ";" + l.getWorld().getName());
        data.set("spawnpoints", spawnpoints);
        saveData();
    }

    public void removeSpawn(int index) {
        ArrayList<String> spawnpoints = getSpawnpoints();
        spawnpoints.remove(index);
        data.set("spawnpoints", spawnpoints);
        saveData();
    }

    public Location getRandomSpawn() {
        String[] s = getSpawnpoints().get(new Random().nextInt(getSpawnpointAmount())).split(";");

        double x = Double.parseDouble(s[0]);
        double y = Double.parseDouble(s[1]);
        double z = Double.parseDouble(s[2]);
        float yaw = Float.parseFloat(s[3]);
        float pitch = Float.parseFloat(s[4]);
        World w = Bukkit.getWorld(s[5]);

        return new Location(w, x, y, z, yaw, pitch);
    }

    public Location getSpawn(int index) {
        if (index > getSpawnpointAmount()) {
            return getRandomSpawn();
        }
        String[] s = getSpawnpoints().get(index).split(";");

        double x = Double.parseDouble(s[0]);
        double y = Double.parseDouble(s[1]);
        double z = Double.parseDouble(s[2]);
        float yaw = Float.parseFloat(s[3]);
        float pitch = Float.parseFloat(s[4]);
        World w = Bukkit.getWorld(s[5]);

        return new Location(w, x, y, z, yaw, pitch);
    }

    public void setLobby(Location l) {
        data.set("lobby", l);
        saveData();
    }

    public Location getLobby() {
        return (data.get("lobby") != null ? (Location) data.get("lobby") : Bukkit.getWorlds().get(0).getSpawnLocation());
    }

}
