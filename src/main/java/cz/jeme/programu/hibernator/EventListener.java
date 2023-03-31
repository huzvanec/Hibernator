package cz.jeme.programu.hibernator;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    private final HibernationManager hibernationManager;
    private final Config config;

    public EventListener(HibernationManager hibernationManager, Config config) {
        this.hibernationManager = hibernationManager;
        this.config = config;
    }

    @EventHandler
    private void onPlayerLogin(PlayerLoginEvent event) {
        hibernationManager.disableHibernation();
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            return;
        }
        hibernationManager.scheduleEnableHibernation(config.getLeaveDelay() * 20L);
    }
}
