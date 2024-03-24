package cz.jeme.programu.hibernator;

import org.bukkit.Bukkit;

public final class Schedule {
    private final long delay;
    private final long scheduledTime;
    private final long startTime;

    public Schedule(long delay) {
        this.delay = delay;
        Bukkit.getScheduler().runTaskLaterAsynchronously(
                Hibernator.getPlugin(),
                () -> Bukkit.getScheduler().runTask(Hibernator.getPlugin(), HibernationManager.INSTANCE::enableHibernation),
                delay
        );
        scheduledTime = System.currentTimeMillis();
        startTime = scheduledTime + delay * 50;
    }

    public long getDelay() {
        return delay;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getRemainingTime() {
        return startTime - System.currentTimeMillis();
    }
}
