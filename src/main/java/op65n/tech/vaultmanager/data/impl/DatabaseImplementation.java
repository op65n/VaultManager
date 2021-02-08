package op65n.tech.vaultmanager.data.impl;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.util.File;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public final class DatabaseImplementation implements DataProvider {

    private final VaultManagerPlugin plugin;

    public DatabaseImplementation(final VaultManagerPlugin plugin) {
        this.plugin = plugin;

        File.saveResources(
                "query.properties"
        );
    }

    @Nullable
    @Override
    public VaultSnapshot getVaultSnapshot(@NotNull final UUID identifier, final int position) {
        return null;
    }

    @Override
    public void setVaultDisplayName(@NotNull final UUID identifier, final int position, @NotNull final String name) {

    }

    @Override
    public Integer getVaultPositionByName(@NotNull final UUID identifier, @NotNull final String name) {
        return null;
    }

    @Override
    public void setVaultContents(@NotNull final UUID identifier, final int position, @NotNull final Map<Integer, ItemStack> contents) {

    }

}
