package op65n.tech.vaultmanager.command.impl;

import com.google.common.primitives.Ints;
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

import java.util.logging.Level;

@Command("privatevault")
@Alias("pv")
@SuppressWarnings("UnstableApiUsage")
public final class PrivateVaultCommand extends CommandBase {

    private final VaultManagerPlugin plugin;
    private final FileConfiguration configuration;

    public PrivateVaultCommand(final VaultManagerPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfig();
    }

    @Default
    public void onVaultCommand(final Player player, final String index) {
        Task.async(() -> {
            final VaultMenu menu = new VaultMenu(plugin, player);
            final FileConfiguration configuration = File.getUserConfiguration(plugin, player);

            configuration.getKeys(true).forEach(System.out::println);
            final int vaultPosition = Ints.tryParse(index) != null ? Integer.parseInt(index) : File.getVaultPositionByName(configuration, index);
            if (!Permissible.hasVaultAccess(player, vaultPosition)) {
                Message.send(
                        player,
                        this.configuration.getString("message.missing-vault-permission")
                );
                return;
            }

            menu.assignContents(Serializable.decodeBase64ToInventory(configuration.getString(String.format("vaults.%s.contents", vaultPosition))));
            menu.assignVaultName(configuration.getString(String.format("vaults.%s.displayName", vaultPosition)));

            menu.constructMenu(vaultPosition);

            Task.queue(() -> {
                final Gui vaultGui = menu.getVaultGui();
                if (vaultGui == null) {
                    plugin.getLogger().log(Level.WARNING,
                            String.format("Failed to Construct & Open Private Vault num. %s for user %s", vaultPosition, player.getName())
                    );
                    return;
                }

                vaultGui.open(player);
            });
        });
    }


}
