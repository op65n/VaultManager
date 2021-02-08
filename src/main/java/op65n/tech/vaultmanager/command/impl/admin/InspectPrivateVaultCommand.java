package op65n.tech.vaultmanager.command.impl.admin;

import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import me.mattstudios.mfgui.gui.guis.Gui;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.object.impl.VaultEditSessionImplementation;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.util.Base;
import op65n.tech.vaultmanager.util.Task;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command("inspectvault")
@Alias("iv")
public final class InspectPrivateVaultCommand extends CommandBase {

    private final DataProvider dataProvider;
    private final FileConfiguration configuration;

    public InspectPrivateVaultCommand(final VaultManagerPlugin plugin) {
        this.dataProvider = plugin.getDataProvider();
        this.configuration = plugin.getConfig();
    }

    @Default
    @Permission("vaultmanager.command.admin.inspect")
    public void onInspectCommand(final Player player, final String target, final Integer position, final Boolean edit) {
        Task.async(() -> {
            final OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(target);
            final UUID identifier = offlineTarget.getUniqueId();

            final VaultSnapshot vaultSnapshot = !edit ? this.dataProvider.getVaultSnapshot(identifier, position) : new VaultEditSessionImplementation(dataProvider, identifier, position);
            if (vaultSnapshot == null) {
                Base.sendMessage(
                        player,
                        configuration.getString("message.user-missing-access"),
                        "{target-name}", offlineTarget.getName()
                );
                return;
            }

            final Gui snapshotMenu = vaultSnapshot.construct(target);
            Task.queue(() ->
                    snapshotMenu.open(player)
            );
        });
    }

}
