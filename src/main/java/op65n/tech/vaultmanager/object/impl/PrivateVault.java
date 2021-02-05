package op65n.tech.vaultmanager.object.impl;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class PrivateVault {

    private String vaultName;
    private final Map<Integer, ItemStack> contents = new HashMap<>();

    /**
     * Adds a {@link ItemStack} to the specified slot in the desired vault
     *
     * @param slot for the {@link ItemStack} to be assigned to
     * @param item added to the vault
     */
    public void setContent(final int slot, final ItemStack item) {
        this.contents.put(slot, item);
    }

    /**
     * Set's the custom name for this vault
     *
     * @param name of the vault
     */
    public void setVaultName(final String name) {
        this.vaultName = name;
    }

    /**
     * Returns the name of this vault
     *
     * @param index {@link PrivateVault}'s index
     * @return The custom name of the vault, or a default
     */
    public String getVaultName(final int index) {
        if (vaultName == null || vaultName.isBlank() || vaultName.isEmpty())
            return String.format("Vault %s", index);

        return this.vaultName;
    }

    /**
     * Returns the contents of the vault as a {@link Map}
     *
     * @return Contents of the vault
     */
    public Map<Integer, ItemStack> getContents() {
        return this.contents;
    }

}
