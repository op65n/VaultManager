package op65n.tech.vaultmanager.object;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.object.impl.PrivateVault;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerVault {

    private final VaultManagerPlugin plugin = JavaPlugin.getPlugin(VaultManagerPlugin.class);
    private final UUID identifier;

    public PlayerVault(final Player player) {
        this.identifier = player.getUniqueId();
    }

    /**
     * Returns the Unique ID of the vault owner
     * {@link Player#getUniqueId()}
     *
     * @return {@link UUID} of the vault owner
     */
    public UUID getIdentifier() {
        return this.identifier;
    }

    /**
     * Retrieves the specified {@link PrivateVault} in the given position
     * or generates a new one if none match.
     *
     * @param position of the desired vault
     * @return {@link PrivateVault} or generates a new one if none match
     */
    public PrivateVault getPrivateVault(final int position) {
        return null; // serialize from file
    }



}
