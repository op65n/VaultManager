package op65n.tech.vaultmanager.util;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class Task {

    private static final VaultManagerPlugin PLUGIN = JavaPlugin.getPlugin(VaultManagerPlugin.class);
    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();

    /**
     * Executes the given {@link Runnable} asynchronously
     *
     * @param runnable Given code to be executed
     */
    public static void async(final Runnable runnable) {
        final int taskID = SCHEDULER.scheduleAsyncDelayedTask(PLUGIN, runnable);

        if (taskID == -1)
            throw new RuntimeException(
                    String.format("Failed to schedule Async task with the hash of '%s'!", runnable.hashCode())
            );
    }

    /**
     * Executes the given {@link Runnable} synchronously
     *
     * @param runnable Given code to be executed
     */
    public static void queue(final Runnable runnable) {
        final int taskID = SCHEDULER.scheduleSyncDelayedTask(PLUGIN, runnable);

        if (taskID == -1)
            throw new RuntimeException(
                    String.format("Failed to schedule a Sync task with the has of '%s'!", runnable.hashCode())
            );
    }


}
