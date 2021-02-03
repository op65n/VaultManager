package op65n.tech.vaultmanager.command.impl;

import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import me.mattstudios.mfgui.gui.guis.Gui;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.object.menu.VaultMenu;
import op65n.tech.vaultmanager.util.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

@Command("privatevault")
@Alias("pv")
public final class PrivateVaultCommand extends CommandBase {

    private final VaultManagerPlugin plugin;
    private final FileConfiguration configuration;

    public PrivateVaultCommand(final VaultManagerPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfig();
    }

    @Default
    public void onVaultCommand(final Player player, final Integer index) {
        final VaultMenu menu = new VaultMenu(plugin, player);

        Function.perform(new BukkitRunnable() {
            @Override
            public void run() {
                if (!Permissible.hasVaultAccess(player, index)) {
                    Message.send(
                            player,
                            configuration.getString("message.missing-vault-permission")
                    );
                    return;
                }

                final FileConfiguration configuration = File.getUserConfiguration(plugin, player);

                menu.assignVault(Serializable.fromBase64(configuration.getString(String.format("vaults.%s", index))));
                menu.constructMenu(index);
            }
        }).thenAccept((consumer) -> {
            final Gui vaultGui = menu.getVaultGui();

            if (vaultGui == null) {
                plugin.getLogger().log(Level.WARNING,
                        String.format("Failed to Construct & Open Private Vault num. %s for user %s", index, player.getName())
                );
                return;
            }

            vaultGui.open(player);
        });
    }

}
