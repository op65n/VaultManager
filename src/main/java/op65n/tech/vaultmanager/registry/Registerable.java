package op65n.tech.vaultmanager.registry;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import org.jetbrains.annotations.NotNull;

public interface Registerable {

    void register(@NotNull final VaultManagerPlugin plugin);

}
