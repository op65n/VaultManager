package op65n.tech.vaultmanager.command.registerable;

import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.CompletionHandler;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.command.impl.PrivateVaultCommand;
import op65n.tech.vaultmanager.command.impl.PrivateVaultNameCommand;
import op65n.tech.vaultmanager.registry.Registerable;

public final class CommandRegisterable implements Registerable {

    /**
     * Registers our {@link me.mattstudios.mf.base.CommandBase}'s
     * and handles specific tab completions
     *
     * @param plugin {@link VaultManagerPlugin} instance
     */
    @Override
    public void register(final VaultManagerPlugin plugin) {
        final CommandManager manager = new CommandManager(plugin);

        manager.register(
                new PrivateVaultCommand(plugin),
                new PrivateVaultNameCommand(plugin)
        );

        final CompletionHandler completionHandler = manager.getCompletionHandler();
    }

}
