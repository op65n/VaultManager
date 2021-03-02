package op65n.tech.vaultmanager.command.impl.user;

import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import com.google.common.primitives.Ints;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.CompleteFor;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import me.mattstudios.mfgui.gui.guis.Gui;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.command.registerable.completion.CompletionCache;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.object.impl.VaultEditSessionImplementation;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.util.Base;
import op65n.tech.vaultmanager.util.Task;
import op65n.tech.vaultmanager.util.check.Permissible;
import op65n.tech.vaultmanager.util.key.Keyable;
import op65n.tech.vaultmanager.util.key.impl.NameKey;
import op65n.tech.vaultmanager.util.key.impl.PositionKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@Command("privatevault")
@Alias("pv")
public final class PrivateVaultCommand extends CommandBase {

    private final DataProvider dataProvider;
    private final FileConfiguration configuration;
    private final CompletionCache completionCache;
    private final ActionHandler actionHandler;

    public PrivateVaultCommand(final VaultManagerPlugin plugin) {
        this.dataProvider = plugin.getDataProvider();
        this.configuration = plugin.getConfig();
        this.completionCache = plugin.getCompletionCache();
        this.actionHandler = plugin.getActionHandler();
    }

    @Default
    public void onVaultCommand(final Player player, final String keyObject) {
        Task.async(() -> {
            final Keyable<?> key = Ints.tryParse(keyObject) != null ? new PositionKey(Integer.parseInt(keyObject)) : new NameKey(keyObject);
            final Integer position = getVaultPosition(player, key);
            if (position == null) {
                actionHandler.execute(
                        player,
                        Base.replaceList(
                                configuration.getStringList("message.invalid-vault-identifier"),
                                "{identifier}", key.getKey()
                        )
                );
                return;
            }

            if (position <= 0) {
                actionHandler.execute(
                        player,
                        configuration.getStringList("message.invalid-position-number")
                );
                return;
            }

            if (!Permissible.hasVaultAccess(player, position)) {
                actionHandler.execute(
                        player,
                        configuration.getStringList("message.no-vault-access")
                );
                return;
            }

            final int size = Permissible.getAllowedVaultSize(player);
            final VaultSnapshot vaultSnapshot = new VaultEditSessionImplementation(dataProvider, player.getUniqueId(), position, size);
            final Gui menu = vaultSnapshot.construct(player.getName());
            Task.queue(() ->
                    menu.open(player)
            );
        });
    }

    @CompleteFor("pv")
    private List<String> completion(final List<String> arguments, final CommandSender sender) {
        if (!(sender instanceof Player)) return Collections.emptyList();

        return this.completionCache.getCompletionsFor(((Player) sender).getUniqueId());
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
