package op65n.tech.vaultmanager.command.registerable;

import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.CompletionHandler;
import me.mattstudios.mf.base.MessageHandler;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.command.impl.admin.InspectPrivateVaultCommand;
import op65n.tech.vaultmanager.command.impl.user.PrivateVaultCommand;
import op65n.tech.vaultmanager.command.impl.user.PrivateVaultNameCommand;
import op65n.tech.vaultmanager.util.Base;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public final class CommandRegisterable {

    public void register(@NotNull final VaultManagerPlugin plugin) {
        final CommandManager manager = new CommandManager(plugin);

        manager.register(
                new InspectPrivateVaultCommand(plugin),

                new PrivateVaultCommand(plugin),
                new PrivateVaultNameCommand(plugin)
        );

        final CompletionHandler completionHandler = manager.getCompletionHandler();
        completionHandler.register("#vaults", resolver -> {

            return null;
        });

        final MessageHandler messageHandler = manager.getMessageHandler();
        final FileConfiguration configuration = plugin.getConfig();

        for (final MessageIdentifier message : MessageIdentifier.values()) {
            messageHandler.register(message.getIdentifier(), sender ->
                    Base.sendMessage(
                            sender,
                            configuration.getString(String.format("message.%s", message.getPath()))
                    )
            );
        }
    }

    private enum MessageIdentifier {
        PLAYER_ONLY("cmd.no.console", "player-only-command"),
        WRONG_USAGE("cmd.wrong.usage", "wrong-command-usage"),
        NO_PERMISSION("cmd.no.permission", "no-command-permission"),
        UNKNOWN_COMMAND("cmd.no.exists", "unknown-command");

        private final String identifier;
        private final String path;

        MessageIdentifier(@NotNull final String identifier, @NotNull final String path) {
            this.identifier = identifier;
            this.path = path;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public String getPath() {
            return this.path;
        }
    }

}
