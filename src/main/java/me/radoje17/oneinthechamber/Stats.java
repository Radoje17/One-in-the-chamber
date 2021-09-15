package me.radoje17.oneinthechamber;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Stats {

    private File f;
    private FileConfiguration data;

    public Stats() {
        refreshData();
    }

    public void refreshData() {
        this.f = new File(Bukkit.getWorldContainer() + "/plugins/OneInTheChamber/stats.yml");
        this.data = YamlConfiguration.loadConfiguration(f);
    }

    private void saveData() {
        try {
            data.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStat(String uuid, String stat, double value) {
        data.set(uuid + "." + stat, value);
        saveData();
    }

    public void setStat(String uuid, String stat, String value) {
        data.set(uuid + "." + stat, value);
        saveData();
    }

    public void addStat(String uuid, String stat) {
        data.set(uuid + "." + stat, (getValueDouble(uuid, stat) + 1));
        saveData();
    }

    public void addStat(String uuid, String stat, int amount) {
        data.set(uuid + "." + stat, (getValueDouble(uuid, stat) + amount));
        saveData();
    }

    public String getValueString(String uuid, String stat) {
        return data.getString(uuid + "." + stat);
    }

    public Double getValueDouble(String uuid, String stat) {
        return data.getDouble(uuid + "." + stat);
    }
}
