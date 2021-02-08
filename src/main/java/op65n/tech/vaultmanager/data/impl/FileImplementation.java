package op65n.tech.vaultmanager.data.impl;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.object.impl.VaultSnapshotImplementation;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.util.Serializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * vault:
 * position:
 * contents: <serialized string>
 * name: <custom display name>
 */
public final class FileImplementation implements DataProvider {

    private final VaultManagerPlugin plugin;

    public FileImplementation(final VaultManagerPlugin plugin) {
        this.plugin = plugin;
    }

    @Nullable
    @Override
    public VaultSnapshot getVaultSnapshot(@NotNull final UUID identifier, final int position) {
        final FileConfiguration configuration = getUserConfiguration(identifier);
        final ConfigurationSection vaultSection = configuration.getConfigurationSection(String.format("vault.%s", position));

        if (vaultSection == null)
            return null;

        return new VaultSnapshotImplementation(
                identifier, position,
                vaultSection.getString("name"),
                Serializable.deserialize(vaultSection.getString("contents"))
        );
    }

    @Override
    public void setVaultDisplayName(@NotNull final UUID identifier, final int position, @NotNull final String name) {
        final File file = new File(plugin.getDataFolder(), String.format("storage/vault-%s.yml", identifier.toString()));
        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection vaultSection = configuration.getConfigurationSection(String.format("vault.%s", position));

        if (vaultSection == null)
            vaultSection = configuration.createSection(String.format("vault.%s", position));

        vaultSection.set("name", name);

        try {
            configuration.save(file);
        } catch (final IOException ignored) {
        }
    }

    @Override
    public void setVaultContents(@NotNull final UUID identifier, final int position, @NotNull final Map<Integer, ItemStack> contents) {
        final File file = new File(plugin.getDataFolder(), String.format("storage/vault-%s.yml", identifier.toString()));
        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection vaultSection = configuration.getConfigurationSection(String.format("vault.%s", position));

        if (vaultSection == null)
            vaultSection = configuration.createSection(String.format("vault.%s", position));

        vaultSection.set("contents", Serializable.serialize(contents));

        try {
            configuration.save(file);
        } catch (final IOException ignored) {
        }
    }

    @Override
    public Integer getVaultPositionByName(@NotNull final UUID identifier, @NotNull final String name) {
        final FileConfiguration configuration = getUserConfiguration(identifier);
        final ConfigurationSection vaultsSection = configuration.getConfigurationSection("vault");
        Integer position = null;

        if (vaultsSection == null)
            return position;

        for (final String key : vaultsSection.getKeys(false)) {
            final ConfigurationSection vaultSection = vaultsSection.getConfigurationSection(key);

            if (vaultSection == null)
                continue;

            final String customName = vaultSection.getString("name");
            if (customName == null)
                continue;

            if (customName.equalsIgnoreCase(name)) {
                position = Integer.parseInt(key);
                break;
            }
        }

        return position;
    }

    /**
     * Returns the linked file configuration to the specified user
     *
     * @param identifier Desired user's identifier
     * @return {@link FileConfiguration} of the user's file
     */
    private FileConfiguration getUserConfiguration(final UUID identifier) {
        final File file = new File(plugin.getDataFolder(), String.format("storage/vault-%s.yml", identifier.toString()));

        return YamlConfiguration.loadConfiguration(file);
    }

}
