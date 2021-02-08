package op65n.tech.vaultmanager.util;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class File {

    private static final VaultManagerPlugin PLUGIN = JavaPlugin.getPlugin(VaultManagerPlugin.class);

    /**
     * Saves the given resource path's to the plugins directory,
     * if a file with that path does not already exist
     *
     * @param path A set of file path's to be saved
     */
    public static void saveResources(@NotNull final String... path) {
        Arrays.stream(path).forEach(it -> {
            if (!new java.io.File(PLUGIN.getDataFolder(), it).exists())
                PLUGIN.saveResource(it, false);
        });
    }

}
