package cz.jeme.programu.hibernator;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

public final class Hibernator extends JavaPlugin {
    public static @NotNull FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reload();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(EventListener.INSTANCE, this);

        new HibernatorCommand(this); // Register /hibernator command
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            HibernationManager.INSTANCE.scheduleHibernation(
                    Hibernator.config.getLong("server-start-delay") * 20L
            );
        }
    }

    public void reload() {
        reloadConfig();
        config = getConfig();
    }

    @Override
    public void onDisable() {
        HibernationManager.INSTANCE.disableHibernation();
    }

    public static void serverLog(@NotNull Level level, @NotNull String message) {
        Bukkit.getLogger().log(level, Message.strip(Message.PREFIX) + message);
    }

    public static void serverLog(@NotNull Level level, @NotNull String message, @NotNull Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        String stackTraceStr = stringWriter.toString();
        serverLog(level, message + "\n" + stackTraceStr);
    }

    public static void serverLog(@NotNull String message, @NotNull Exception exception) {
        serverLog(Level.SEVERE, message, exception);
    }

    public static @NotNull Hibernator getPlugin() {
        return getPlugin(Hibernator.class);
    }
}
