package op65n.tech.vaultmanager.command.impl;

import com.google.common.primitives.Ints;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.util.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@Command("setvault")
@SuppressWarnings("UnstableApiUsage")
public final class PrivateVaultNameCommand extends CommandBase {

    private final VaultManagerPlugin plugin;
    private final FileConfiguration configuration;

    public PrivateVaultNameCommand(final VaultManagerPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfig();
    }

    @Default
    @Permission("vaultmanager.vault.rename")
    public void onRenameCommand(final Player player, final String index, final String vaultName) {
        Task.async(() -> {
            final FileConfiguration configuration = File.getUserConfiguration(plugin, player);
            final int vaultPosition = Ints.tryParse(index) != null ? Integer.parseInt(index) : File.getVaultPositionByName(configuration, index);

            if (!Permissible.hasVaultAccess(player, vaultPosition)) {
                Message.send(
                        player,
                        this.configuration.getString("message.missing-vault-permission")
                );
                return;
            }

            final Nameable.Response validity = Nameable.checkValidity(vaultName);
            if (validity == Nameable.Response.LENGTH) {
                Message.send(
                        player,
                        validity.getReason()
                );
                return;
            }

            File.setPrivateVaultName(plugin, player, vaultPosition, vaultName);
            Message.send(
                    player,
                    this.configuration.getString("message.successfully-changed-vault-name")
            );
        });
    }

}
