package op65n.tech.vaultmanager.command.impl.user;

import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.util.Base;
import op65n.tech.vaultmanager.util.Task;
import op65n.tech.vaultmanager.util.check.Nameable;
import op65n.tech.vaultmanager.util.check.Permissible;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command("setvault")
public final class PrivateVaultNameCommand extends CommandBase {

    private final DataProvider dataProvider;
    private final FileConfiguration configuration;
    private final ActionHandler actionHandler;

    public PrivateVaultNameCommand(final VaultManagerPlugin plugin) {
        this.dataProvider = plugin.getDataProvider();
        this.configuration = plugin.getConfig();
        this.actionHandler = plugin.getActionHandler();
    }

    @Default
    @Permission("vaultmanager.vault.rename")
    public void onRenameCommand(final Player player, final Integer position, final String name) {
        Task.async(() -> {
            final UUID identifier = player.getUniqueId();
            if (!Permissible.hasVaultAccess(player, position)) {
                actionHandler.execute(
                        player,
                        configuration.getStringList("message.no-vault-access")
                );
                return;
            }

            final Nameable.Response response = Nameable.checkValidity(name);
            if (response == Nameable.Response.VALID) {
                actionHandler.execute(
                        player,
                        Base.replaceList(
                                configuration.getStringList("message.successfully-changed-name"),
                                "{vault-name}", name,
                                "{vault-position}", position
                        )
                );
                this.dataProvider.setVaultDisplayName(identifier, position, name);
                return;
            }

            actionHandler.execute(
                    player,
                    configuration.getStringList(String.format("message.%s", response.getReasonPath()))
            );
        });
    }

}
