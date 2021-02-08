package op65n.tech.vaultmanager.command.registerable;

import me.mattstudios.mf.base.CommandManager;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.command.impl.admin.InspectPrivateVaultCommand;
import op65n.tech.vaultmanager.command.impl.user.PrivateVaultCommand;
import op65n.tech.vaultmanager.command.impl.user.PrivateVaultNameCommand;
import org.jetbrains.annotations.NotNull;

public final class CommandRegisterable {

    public void register(@NotNull final VaultManagerPlugin plugin) {
        final CommandManager manager = new CommandManager(plugin);

        manager.register(
                new InspectPrivateVaultCommand(plugin),

                new PrivateVaultCommand(plugin),
                new PrivateVaultNameCommand(plugin)
        );
    }

}
