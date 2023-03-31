package cz.jeme.programu.hibernator;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

    private final FileConfiguration yaml = new YamlConfiguration();
    private final File configFile;

    private boolean pluginEnabled;

    private long sleep;

    private boolean unloadChunks;

    private int leaveDelay;

    private int startDelay;

    private boolean logHibernation;

    private boolean logChunks;

    private boolean logSchedule;

    public Config(File configFile) {
        this.configFile = configFile;
        reload();
    }

    private static final Map<String, String> CONFIG_MAP = Map.of("ENABLED", "enabled", "TPS", "hibernation-tps",
            "UNLOAD_CHUNKS", "unload-chunks", "LEAVE_DELAY", "player-leave-delay", "START_DELAY", "server-start-delay",
            "LOG_HIBERNATE", "log-hibernation", "LOG_CHUNKS", "log-chunk-unload", "LOG_SCHEDULE", "log-schedule");

    public void setPluginEnabled(boolean pluginEnabled) {
        this.pluginEnabled = pluginEnabled;
        save();
    }

    public void setSleep(long sleep) {
        this.sleep = sleep;
        save();
    }

    public boolean isPluginEnabled() {
        return pluginEnabled;
    }

    public long getSleep() {
        return sleep;
    }

    public void reload() {
        try {
            yaml.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            Hibernator.serverLog(Level.SEVERE, "Unable to load config!");
            e.printStackTrace();
            return;
        }

        pluginEnabled = yaml.getBoolean(CONFIG_MAP.get("ENABLED"));
        double hibernateTPS = yaml.getDouble(CONFIG_MAP.get("TPS"));
        if (hibernateTPS < 0.5) {
            Hibernator.serverLog(Level.WARNING,
                    "It is not recommended to set TPS under 0.5 because the server might crash!");
        }
        if (hibernateTPS > 20) {
            throw new IllegalArgumentException("TPS cannot be higher than 20!");
        }
        sleep = (long) (1000 / hibernateTPS);
        unloadChunks = yaml.getBoolean(CONFIG_MAP.get("UNLOAD_CHUNKS"));
        leaveDelay = yaml.getInt(CONFIG_MAP.get("LEAVE_DELAY"));
        logHibernation = yaml.getBoolean(CONFIG_MAP.get("LOG_HIBERNATE"));
        logChunks = yaml.getBoolean(CONFIG_MAP.get("LOG_CHUNKS"));
        startDelay = yaml.getInt(CONFIG_MAP.get("START_DELAY"));
        logSchedule = yaml.getBoolean(CONFIG_MAP.get("LOG_SCHEDULE"));
    }

    public void save() {
        yaml.set(CONFIG_MAP.get("ENABLED"), pluginEnabled);
        yaml.set(CONFIG_MAP.get("TPS"), 1000F / sleep);
        yaml.set(CONFIG_MAP.get("UNLOAD_CHUNKS"), unloadChunks);
        yaml.set(CONFIG_MAP.get("LEAVE_DELAY"), leaveDelay);
        yaml.set(CONFIG_MAP.get("LOG_HIBERNATE"), logHibernation);
        yaml.set(CONFIG_MAP.get("LOG_CHUNKS"), logChunks);
        yaml.set(CONFIG_MAP.get("START_DELAY"), startDelay);
        yaml.set(CONFIG_MAP.get("LOG_SCHEDULE"), logSchedule);

        try {
            yaml.save(configFile);
        } catch (IOException e) {
            Hibernator.serverLog(Level.SEVERE, "Unable to save config!");
            e.printStackTrace();
        }
    }

    public int getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
        save();
    }

    public boolean doUnloadChunks() {
        return unloadChunks;
    }

    public void setUnloadChunks(boolean unloadChunks) {
        this.unloadChunks = unloadChunks;
        save();
    }

    public int getLeaveDelay() {
        return leaveDelay;
    }

    public void setLeaveDelay(int leaveDelay) {
        this.leaveDelay = leaveDelay;
        save();
    }

    public boolean doLogHibernation() {
        return logHibernation;
    }

    public void setLogHibernation(boolean logHibernation) {
        this.logHibernation = logHibernation;
        save();
    }

    public boolean doLogChunks() {
        return logChunks;
    }

    public boolean doLogSchedule() {
        return logSchedule;
    }

    public void setLogSchedule(boolean logSchedule) {
        this.logSchedule = logSchedule;
        save();
    }

    public void setLogChunks(boolean logChunks) {
        this.logChunks = logChunks;
        save();
    }
}
