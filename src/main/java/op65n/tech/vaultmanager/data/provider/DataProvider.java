package op65n.tech.vaultmanager.data.provider;

import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface DataProvider {

    /**
     * Returns a {@link VaultSnapshot} from the saved data, or
     * creates a new snapshot with empty contents
     *
     * @param identifier of the requested player
     * @param position   of the requested vault
     * @return {@link VaultSnapshot} from existing data or a new snapshot
     */
    @NotNull
    VaultSnapshot getVaultSnapshot(@NotNull final UUID identifier, final int position);

    /**
     * Returns the position index of the matching vault, or null if none matches the given name
     *
     * @param identifier of the requested player
     * @param name       of the requested vault
     * @return {@link Integer} position of the corresponding vault, or null
     */
    @Nullable
    Integer getVaultPositionByName(@NotNull final UUID identifier, @NotNull final String name);

    /**
     * Set's the given vault's name to the given input
     *
     * @param identifier of the requested player
     * @param position   of the given vault
     * @param name       for the given vault
     */
    void setVaultDisplayName(@NotNull final UUID identifier, final int position, @NotNull final String name);

    /**
     * Set's the new contents for the given vault,
     * squashing previous contents (Serialized - Base64)
     *
     * @param identifier of the requested player
     * @param position   of the given vault
     * @param contents   of the vault
     */
    void setVaultContents(@NotNull final UUID identifier, final int position, @NotNull final Map<Integer, ItemStack> contents);

}
