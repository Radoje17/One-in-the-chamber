package me.radoje17.oneinthechamber;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class OneInTheChamber extends JavaPlugin {

    private UUIDManager uuidManager;
    private SpawnManager spawnManager;
    private GameManager gameManager;
    private Stats stats;
    private SignListener signListener;


    private Commands commands;

    @Override
    public void onEnable() {
        this.uuidManager = new UUIDManager();
        this.spawnManager = new SpawnManager();
        this.gameManager = new GameManager(this);
        this.stats = new Stats();
        this.signListener = new SignListener(this);

        signListener.updateSigns();

        this.commands = new Commands(this);


        Bukkit.getPluginManager().registerEvents(uuidManager, this);
        Bukkit.getPluginManager().registerEvents(gameManager, this);
        Bukkit.getPluginManager().registerEvents(signListener, this);

        getCommand("OneInTheChamber").setExecutor(commands);
    }

    @Override
    public void onDisable() {
        gameManager.quitALL();
    }

    public UUIDManager getUuidManager() {
        return uuidManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Stats getStats() {
        return stats;
    }

    public SignListener getSignListener() {
        return signListener;
    }
}
