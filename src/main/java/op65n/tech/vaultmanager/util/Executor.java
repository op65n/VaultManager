package op65n.tech.vaultmanager.util;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Executor {

    private static final VaultManagerPlugin PLUGIN = JavaPlugin.getPlugin(VaultManagerPlugin.class);
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);

    public static void async(final Runnable async) {
        EXECUTOR.submit(async);
    }

    public static void queue(final Runnable sync) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(PLUGIN, sync);
    }

}
