package op65n.tech.vaultmanager;

import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import me.mattstudios.msg.base.MessageOptions;
import me.mattstudios.msg.base.internal.Format;
import op65n.tech.vaultmanager.command.registerable.CommandRegisterable;
import op65n.tech.vaultmanager.command.registerable.completion.CompletionCache;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.data.registerable.DataRegisterable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.op65n.gazelle.Gazelle;

import java.util.concurrent.CompletableFuture;

public final class VaultManagerPlugin extends JavaPlugin {

    private final ActionHandler actionHandler = new ActionHandler(this);
    private final DataRegisterable dataRegisterable = new DataRegisterable();
    private final CommandRegisterable commandRegisterable = new CommandRegisterable();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.dataRegisterable.register(this);
        this.commandRegisterable.register(this);

        actionHandler.createBukkitMessage(MessageOptions.builder().removeFormat(Format.ITALIC).build());
        actionHandler.loadDefaults(false);
    }

    @Override
    public void onDisable() {
        CompletableFuture.supplyAsync(() -> {
            Gazelle.stop();
            return null;
        }).join();
        reloadConfig();
    }

    @NotNull
    public DataProvider getDataProvider() {
        return this.dataRegisterable.getDataProvider();
    }

    @NotNull
    public CompletionCache getCompletionCache() {
        return this.commandRegisterable.getCompletionCache();
    }

    @NotNull
    public ActionHandler getActionHandler() {
        return this.actionHandler;
    }
}
