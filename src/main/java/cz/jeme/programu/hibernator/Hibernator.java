package cz.jeme.programu.hibernator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class Hibernator extends JavaPlugin {

    private HibernationManager hibernationManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Config config = new Config(new File(getDataFolder(), "config.yml"));

        hibernationManager = new HibernationManager(config);
        EventListener eventListener = new EventListener(hibernationManager, config);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(eventListener, this);

        new HibernatorCommand(config, hibernationManager);
    }

    @Override
    public void onDisable() {
        getConfig();
        hibernationManager.disableHibernation();
    }

    public static void reload(Config config, HibernationManager hibernationManager) {
        config.reload();
        hibernationManager.reload();
    }

    public static void serverLog(Level lvl, String msg) {
        assert msg != null : "Message is null!";
        Bukkit.getServer().getLogger().log(lvl, Messages.strip(Messages.PREFIX) + msg);
    }
}
