package cz.jeme.programu.hibernator;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public enum EventListener implements Listener {
    INSTANCE;

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerLogin(@NotNull PlayerLoginEvent event) {
        if (HibernationManager.INSTANCE.isHibernating()) {
            String message = Hibernator.config.getString("player-wake-message");
            if (message == null) {
                HibernationManager.INSTANCE.disableHibernation();
                throw new NullPointerException("player-wake-message is null! Please set it in config!");
            }
            event.disallow(
                    PlayerLoginEvent.Result.KICK_OTHER,
                    Message.from(message.replace("\\n", "\n"))
                    // Yaml parses newline characters, this will replace \\n with \n
            );
        }
        HibernationManager.INSTANCE.disableHibernation();
        // schedule hibernation if the player decides not to reconnect
        HibernationManager.INSTANCE.scheduleHibernation(
                Hibernator.config.getLong("player-leave-delay") * 20L
        );
    }

    @EventHandler
    private void onPlayerLeave(@NotNull PlayerQuitEvent event) {
        // 1 inclusive here, because this event is called before the player actually leaves
        if (Bukkit.getOnlinePlayers().size() <= 1) {
            HibernationManager.INSTANCE.scheduleHibernation(
                    Hibernator.config.getLong("player-leave-delay") * 20L
            );
        }
    }
}
