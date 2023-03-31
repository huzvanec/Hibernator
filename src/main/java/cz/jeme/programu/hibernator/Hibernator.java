package cz.jeme.programu.hibernator;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

public class Hibernator extends JavaPlugin {
    private Config config;

    public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + ChatColor.BOLD + "Hibernator"
            + ChatColor.DARK_GRAY + "] ";

    EventListener eventListener;
    HibernationManager hibernationManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = new Config(new File(getDataFolder(), "config.yml"));

        hibernationManager = new HibernationManager(config);
        eventListener = new EventListener(hibernationManager, config);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(eventListener, this);

        PluginCommand command = getCommand("hibernator");
        if (command != null) {
            command.setTabCompleter(new CommandTabCompleter());
        }
    }

    @Override
    public void onDisable() {
        getConfig();
        hibernationManager.disableHibernation();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hibernator")) {
            if (args.length == 0) {
                config.setPluginEnabled(!config.isPluginEnabled());
                if (config.isPluginEnabled()) {
                    sender.sendMessage(PREFIX + ChatColor.GREEN + "Hibernator enabled");
                    hibernationManager.enableHibernation();
                } else {
                    hibernationManager.disableHibernation();
                    sender.sendMessage(PREFIX + ChatColor.GREEN + "Hibernator disabled");
                }
                return true;
            }
            if (args[0].equals("reload")) {
                reload();
                sender.sendMessage(PREFIX + ChatColor.GREEN + "Reloaded");
                return true;
            }
            if (args[0].equals("enable")) {
                config.setPluginEnabled(true);
                hibernationManager.enableHibernation();
                sender.sendMessage(PREFIX + ChatColor.GREEN + "Hibernator enabled");
                return true;
            }
            if (args[0].equals("disable")) {
                config.setPluginEnabled(false);
                hibernationManager.disableHibernation();
                sender.sendMessage(PREFIX + ChatColor.GREEN + "Hibernator disabled");
                return true;
            }
            if (args[0].equals("status")) {
                String pluginStatus = "disabled";
                if (config.isPluginEnabled()) {
                    pluginStatus = "enabled";
                }
                sender.sendMessage(PREFIX + ChatColor.GREEN + "Plugin is " + pluginStatus);
                String hibernationStatus = "off";
                if (hibernationManager.isHibernationEnabled()) {
                    hibernationStatus = "running";
                }
                sender.sendMessage(PREFIX + ChatColor.GREEN + "Hibernation is " + hibernationStatus);
                sender.sendMessage(PREFIX + ChatColor.GREEN + "Hibernation TPS is set to " + 1000F / config.getSleep());
                return true;
            }
            sender.sendMessage(PREFIX + ChatColor.RED + "Unknown command!");
            return true;
        }
        return false;
    }

    private void reload() {
        config.reload();
        hibernationManager.reload();
    }

    public static void serverLog(Level lvl, String msg) {
        if (msg == null) {
            throw new NullPointerException("Message is null!");
        }
        Bukkit.getServer().getLogger().log(lvl, ChatColor.stripColor(PREFIX) + msg);
    }
}
