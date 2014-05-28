package com.kronosad.projects.FreezeUnfreeze;

import com.kronosad.projects.FreezeUnfreeze.handler.PlayerListener;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FreezeUnfreeze extends JavaPlugin {
    protected static Logger logger = null;
    public static boolean debugMode = false;

    protected Server server;
    protected PluginManager pm;
    protected FreezeUnfreezeConfiguration config;
    protected File dataFolder;

    private Permission permission;

    public static String frozenMessage, invalidBlockMessage, invalidCommandMessage, invalidChatMessage;

    public static List<String> frozenPlayers = new ArrayList<String>();

    public void onEnable() {
        logger = this.getLogger();
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        this.dataFolder = this.getDataFolder();

        this.dataFolder.mkdirs();

        Configuration configuration = this.getConfig();
        configuration.options().copyDefaults(true);
        debugMode = configuration.getBoolean("debug");

        frozenMessage = configuration.getString("frozenMessage");
        invalidBlockMessage = configuration.getString("invalidBlockMessage");
        invalidCommandMessage = configuration.getString("invalidCommandMessage");
        invalidChatMessage = configuration.getString("invalidChatMessage");

        this.config = new FreezeUnfreezeConfiguration(configuration);

        frozenPlayers = config.getSerializedPlayers();

        this.saveConfig();

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        log("Version " + this.getDescription().getVersion() + " enabled");

        if (!setupPermissions()) {
            error("Was unable to setup Vault Permissions integration!");
        }
    }

    // Snippet from the Vault plugin page.
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public void onDisable() {
        log("Version " + this.getDescription().getVersion() + " disabled");
        config.setConfig(getConfig());
        config.serializePlayers(frozenPlayers);
        saveConfig();
    }

    public static void log(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void error(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    public static void error(String msg, Throwable t) {
        logger.log(Level.SEVERE, msg, t);
    }

    public static void debug(String msg) {
        if (debugMode) {
            log("[Debug] " + msg);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("suspend")) {
            if (sender instanceof Player) {
                if (!permission.has(sender, "player.suspend")) {
                    sender.sendMessage(ChatColor.DARK_RED + "Invalid permissions!");
                    debug(((Player) sender).getDisplayName() + " tried to use the SUSPEND command, but didn't pass permissions validation!");
                    return false;
                }
            }

            if (args.length <= 1) {
                freezePlayer(args[0]);
                sender.sendMessage(getFrozenMessage(args[0]));
                return true;
            } else {
                sender.sendMessage("[FreezeUnfreeze] Missing Player Name!");
                return false;

            }
        } else if (label.equalsIgnoreCase("unsuspend")) {
            if (sender instanceof Player) {
                if (!permission.has(sender, "player.unsuspend")) {
                    sender.sendMessage(ChatColor.DARK_RED + "Invalid permissions!");
                    debug(((Player) sender).getDisplayName() + " tried to use the UNSUSPEND command, but didn't pass permissions validation!");
                    return false;
                }
            }

            if (args.length <= 1) {
                unfreezePlayer(args[0]);
                sender.sendMessage(getUnFrozenMessage(args[0]));
                return true;
            } else {
                sender.sendMessage("[FreezeUnfreeze] Missing Player Name!");
                return false;
            }
        }
        return false;
    }

    public static void freezePlayer(String playerName) {
        frozenPlayers.add(playerName);
    }

    public static void unfreezePlayer(String playerName) {
        frozenPlayers.remove(playerName);
    }

    public static String getFrozenMessage(String player) {
        String msg = ChatColor.AQUA + "Froze Player: " + ChatColor.GOLD + player;
        return "[FreezeUnfreeze] " + msg;
    }

    public static String getUnFrozenMessage(String player) {
        String msg = ChatColor.AQUA + "Unfroze Player: " + ChatColor.GOLD + player;
        return "[FreezeUnfreeze] " + msg;
    }
}
