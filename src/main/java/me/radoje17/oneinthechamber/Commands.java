package me.radoje17.oneinthechamber;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class Commands implements CommandExecutor {

    private OneInTheChamber plugin;
    public Commands(OneInTheChamber plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GRAY + "One in the Chamber" + ChatColor.YELLOW + " by Radoje17");
            sender.sendMessage(ChatColor.GRAY + "Verzija " + ChatColor.YELLOW + Bukkit.getPluginManager().getPlugin("OneInTheChamber").getDescription().getVersion());
            if (plugin.getGameManager().inGame((Player) sender)) {
                sender.sendMessage("/" + label + " leave");
            } else {
                sender.sendMessage("  /" + label + " join");
            }
            if (sender.isOp()) {
                sender.sendMessage("  /" + label + " addSpawn");
                sender.sendMessage("  /" + label + " setLobby");
            }

            return false;
        }

        if (args[0].equalsIgnoreCase("join") && sender instanceof Player) {
            if (plugin.getGameManager().inGame((Player) sender)) {
                sender.sendMessage(ChatColor.GRAY + "Već ste u u igri.");
                return false;
            }

            plugin.getGameManager().addPlayer((Player) sender);
            sender.sendMessage(ChatColor.GRAY + "Ušli ste u igru, srećno!");
            return false;
        }

        if (args[0].equalsIgnoreCase("addSpawn") && sender.isOp() && sender instanceof Player) {
            plugin.getSpawnManager().addSpawn(((Player) sender).getLocation());
            sender.sendMessage(ChatColor.GRAY + "Spawnpoint na Vašoj lokaciji dodat.");
            return false;
        }

        if (args[0].equalsIgnoreCase("setLobby") && sender.isOp() && sender instanceof Player) {
            plugin.getSpawnManager().setLobby(((Player) sender).getLocation());
            sender.sendMessage(ChatColor.GRAY + "Lobby postavljen.");
            return false;
        }

        if (args[0].equalsIgnoreCase("leave") && sender instanceof Player) {
            if (!plugin.getGameManager().inGame((Player) sender)) {
                sender.sendMessage(ChatColor.GRAY + "Niste u igri.");
                return false;
            }
            plugin.getGameManager().removePlayer((Player) sender);
            sender.sendMessage(ChatColor.GRAY + "Izašali ste iz igre.");
            return false;
        }

        sender.sendMessage(ChatColor.RED + "Komanda ne postoji!");
        return false;
    }

}
