package op65n.tech.vaultmanager.util;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.logging.Level;

public final class File {

    /**
     * Returns a {@link FileConfiguration} for the given user
     *
     * @param player desired user
     * @return {@link FileConfiguration} for the given user
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static FileConfiguration getUserConfiguration(final VaultManagerPlugin plugin, final Player player) {
        final java.io.File file = new java.io.File(plugin.getDataFolder(), String.format("storage/vault-%s.yml", player.getUniqueId().toString()));

        try {
            if (!file.exists())
                file.createNewFile();
        } catch (final IOException exception) {
            plugin.getLogger().log(Level.WARNING,
                    String.format("Failed to generate a new Vault file for user %s!", player.getName())
            );
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves the given serialized string to the user's vault file
     *
     * @param player desired user
     * @param index of the updated {@link op65n.tech.vaultmanager.object.impl.PrivateVault}
     * @param base64 {@link String} of the serialized contents
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void setUserConfiguration(final VaultManagerPlugin plugin, final Player player, final int index, final String base64) {
        final java.io.File file = new java.io.File(plugin.getDataFolder(), String.format("storage/vault-%s.yml", player.getUniqueId().toString()));

        try {
            if (!file.exists())
                file.createNewFile();
        } catch (final IOException exception) {
            plugin.getLogger().log(Level.WARNING,
                    String.format("Failed to generate a new Vault file for user %s!", player.getName())
            );
        }

        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.set(String.format("vaults.%s", index), base64);

        try {
            configuration.save(file);
        } catch (final IOException exception) {
            plugin.getLogger().log(Level.WARNING,
                    String.format("Failed to save Vault file for user %s!", player.getName())
            );
        }
    }

}
