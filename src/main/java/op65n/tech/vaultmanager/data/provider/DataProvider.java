package op65n.tech.vaultmanager.data.provider;

import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface DataProvider {

    @NotNull
    VaultSnapshot getVaultSnapshot(@NotNull final UUID identifier, final int position);

    Integer getVaultPositionByName(@NotNull final UUID identifier, @NotNull final String name);

    void setVaultDisplayName(@NotNull final UUID identifier, final int position, @NotNull final String name);

    void setVaultContents(@NotNull final UUID identifier, final int position, @NotNull final Map<Integer, ItemStack> contents);

}
