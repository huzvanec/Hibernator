package cz.jeme.programu.hibernator;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class HibernationManager {
    private HibernationRunnable hibernation;
    private final Config config;

    private boolean hibernationEnabled = false;

    public boolean isHibernationEnabled() {
        return hibernationEnabled;
    }

    public HibernationManager(Config config) {
        this.config = config;

        if (Bukkit.getOnlinePlayers().size() == 0) {
            scheduleEnableHibernation(config.getStartDelay() * 20L);
        }
    }

    public boolean enableHibernation() {
        if (hibernationEnabled || !config.isPluginEnabled() || Bukkit.getOnlinePlayers().size() > 0) {
            return false;
        }
        hibernation = new HibernationRunnable(config.getSleep());
        if (config.doUnloadChunks()) {
            hibernation.unloadChunks(config.doLogChunks());
        }
        hibernation.runTaskTimer(Hibernator.getPlugin(Hibernator.class), 0L, 1L);
        hibernationEnabled = true;
        if (config.doLogHibernation()) {
            Hibernator.serverLog(Level.INFO, "Entered hibernation (" + 1000F / config.getSleep() + " TPS)");
        }
        return true;
    }

    public void scheduleEnableHibernation(long delay) {
        if (config.doLogSchedule()) {
            Hibernator.serverLog(Level.INFO, "Scheduled hibernation in " + delay / 20 + " seconds (" + delay + " ticks)");
        }
        Hibernator plugin = Hibernator.getPlugin(Hibernator.class);
        new HibernationSchedule(this).runTaskLater(plugin, delay);
    }

    public boolean disableHibernation() {
        if (!hibernationEnabled) {
            return false;
        }
        hibernation.cancel();
        hibernation = null;
        hibernationEnabled = false;
        if (config.doLogHibernation()) {
            Hibernator.serverLog(Level.INFO, "Exited hibernation");
        }
        return true;
    }

    public void reload() {
        if (hibernation != null) {
            hibernation.setSleep(config.getSleep());
        }
        if (hibernationEnabled && !config.isPluginEnabled()) {
            disableHibernation();
        }
    }
}
