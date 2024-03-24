package cz.jeme.programu.hibernator;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public enum HibernationManager {
    INSTANCE;
    private @Nullable HibernationRunnable hibernation;
    private @Nullable Schedule schedule;

    public boolean enableHibernation() {
        schedule = null;
        if (isHibernating() || !Hibernator.config.getBoolean("enabled") || !Bukkit.getOnlinePlayers().isEmpty()) {
            return false;
        }
        double tps = Hibernator.config.getDouble("hibernation-tps");
        hibernation = new HibernationRunnable(1000D / tps);
        if (Hibernator.config.getBoolean("unload-chunks")) {
            hibernation.unloadChunks(Hibernator.config.getBoolean("log-chunk-unload"));
        }
        hibernation.runTaskTimer(Hibernator.getPlugin(), 0L, 1L);
        if (Hibernator.config.getBoolean("log-hibernation")) {
            Hibernator.serverLog(Level.INFO, "Entered hibernation (" + tps + " TPS)");
        }
        return true;
    }

    public void scheduleHibernation(long delay) {
        if (Hibernator.config.getBoolean("log-schedule") && Hibernator.config.getBoolean("enabled")) {
            Hibernator.serverLog(Level.INFO, "Scheduled hibernation in " + delay / 20D + " seconds (" + delay + " ticks)");
        }
        schedule = new Schedule(delay);
    }

    public boolean disableHibernation() {
        if (!isHibernating()) return false;
        hibernation.cancel();
        hibernation = null;
        if (Hibernator.config.getBoolean("log-hibernation")) {
            Hibernator.serverLog(Level.INFO, "Exited hibernation");
        }
        return true;
    }

    public void reload() {
        if (hibernation != null) {
            hibernation.setSleep(1000D / Hibernator.config.getDouble("hibernation-tps"));
        }
        if (isHibernating() && !Hibernator.config.getBoolean("enabled")) {
            disableHibernation();
        }
    }

    public @Nullable Schedule getSchedule() {
        return schedule;
    }

    public boolean isHibernating() {
        return hibernation != null;
    }

    public boolean isScheduled() {
        return schedule != null;
    }
}
