package cz.jeme.programu.hibernator;

import org.bukkit.scheduler.BukkitRunnable;

public class HibernationSchedule extends BukkitRunnable {

    HibernationManager hibernationManager;

    public HibernationSchedule(HibernationManager snoreHandler) {
        this.hibernationManager = snoreHandler;
    }

    @Override
    public void run() {
        hibernationManager.enableHibernation();
    }
}
