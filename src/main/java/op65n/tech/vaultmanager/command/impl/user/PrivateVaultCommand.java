package op65n.tech.vaultmanager.command.impl.user;

import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import me.mattstudios.mfgui.gui.guis.Gui;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.object.impl.VaultEditSessionImplementation;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.util.Base;
import op65n.tech.vaultmanager.util.Task;
import op65n.tech.vaultmanager.util.check.Permissible;
import op65n.tech.vaultmanager.util.key.Keyable;
import op65n.tech.vaultmanager.util.key.impl.NameKey;
import op65n.tech.vaultmanager.util.key.impl.PositionKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@Command("privatevault")
@Alias("pv")
public final class PrivateVaultCommand extends CommandBase {

    private final DataProvider dataProvider;
    private final FileConfiguration configuration;

    public PrivateVaultCommand(final VaultManagerPlugin plugin) {
        this.dataProvider = plugin.getDataProvider();
        this.configuration = plugin.getConfig();
    }

    @Default
    public void onVaultCommand(final Player player, final Object keyObject) {
        Task.async(() -> {
            final Keyable<?> key = keyObject instanceof Integer ? new PositionKey((int) keyObject) : new NameKey((String) keyObject);
            final Integer position = getVaultPosition(player, key);

            if (position == null) {
                Base.sendMessage(
                        player,
                        configuration.getString("message.invalid-vault-identifier"),
                        "{identifier}", key.getKey()
                );
                return;
            }

            if (Permissible.hasVaultAccess(player, position)) {
                Base.sendMessage(
                        player,
                        configuration.getString("message.no-vault-access")
                );
                return;
            }

            final VaultSnapshot vaultSnapshot = new VaultEditSessionImplementation(dataProvider, player.getUniqueId(), position);
            final Gui menu = vaultSnapshot.construct(player.getName());
            Task.queue(() ->
                    menu.open(player)
            );
        });
    }

    /**
     * Returns the position of the keyable vault or null
     *
     * @param player who's key needs checking
     * @param key    to be checked
     * @return position of the given vault from the key, or null if none match
     */
    private Integer getVaultPosition(final Player player, final Keyable<?> key) {
        if (key.getKey() instanceof Integer)
            return (Integer) key.getKey();

        return this.dataProvider.getVaultPositionByName(player.getUniqueId(), (String) key.getKey());
    }

}
