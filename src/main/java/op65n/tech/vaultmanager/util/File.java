package op65n.tech.vaultmanager.util;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.logging.Level;

@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
public final class File {

    /**
     * Returns a {@link FileConfiguration} for the given user
     *
     * @param player desired user
     * @return {@link FileConfiguration} for the given user
     */
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
     * @param index  of the vault
     * @param base64 {@link String} of the serialized contents
     */
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
        configuration.set(String.format("vaults.%s.contents", index), base64);

        try {
            configuration.save(file);
        } catch (final IOException exception) {
            plugin.getLogger().log(Level.WARNING,
                    String.format("Failed to save Vault file for user %s!", player.getName())
            );
        }
    }

    public static void setPrivateVaultName(final VaultManagerPlugin plugin, final Player player, final int index, final String name) {
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
        configuration.set(String.format("vaults.%s.displayName", index), name);

        try {
            configuration.save(file);
        } catch (final IOException exception) {
            plugin.getLogger().log(Level.WARNING,
                    String.format("Failed to save Vault file for user %s!", player.getName())
            );
        }
    }

    public static int getVaultPositionByName(final FileConfiguration configuration, final String name) {
        int sectionIndex = 0;

        final ConfigurationSection vaults = configuration.getConfigurationSection("vaults");
        for (final String key : vaults.getKeys(true)) {
            final ConfigurationSection vault = vaults.getConfigurationSection(key);

            final String displayName = vault.get("displayName") == null ? "none" : vault.getString("displayName");
            if (displayName.equals(name)) {
                sectionIndex = Integer.parseInt(key);
                break;
            }
        }

        return sectionIndex;
    }

}
