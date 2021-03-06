package op65n.tech.vaultmanager.command.impl.admin;

import dev.triumphteam.gui.guis.Gui;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.object.impl.VaultEditSessionImplementation;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.util.Task;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command("inspectvault")
@Alias("iv")
public final class InspectPrivateVaultCommand extends CommandBase {

    private final DataProvider dataProvider;

    public InspectPrivateVaultCommand(final VaultManagerPlugin plugin) {
        this.dataProvider = plugin.getDataProvider();
    }

    @Default
    @Permission("vaultmanager.command.admin.inspect")
    public void onInspectCommand(final Player player, final String target, final Integer position) {
        Task.async(() -> {
            boolean editable = player.hasPermission("vaultmanager.command.admin.edit");

            final OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(target);
            final UUID identifier = offlineTarget.getUniqueId();

            final VaultSnapshot vaultSnapshot = !editable
                    ? this.dataProvider.getVaultSnapshot(identifier, position)
                    : new VaultEditSessionImplementation(dataProvider, identifier, position, 6);
            final Gui snapshotMenu = vaultSnapshot.construct(target, editable);
            Task.queue(() ->
                    snapshotMenu.open(player)
            );
        });
    }

}
