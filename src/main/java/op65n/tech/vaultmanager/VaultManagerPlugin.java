package op65n.tech.vaultmanager;

import op65n.tech.vaultmanager.command.registerable.CommandRegisterable;
import op65n.tech.vaultmanager.command.registerable.completion.CompletionCache;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.data.registerable.DataRegisterable;
import op65n.tech.vaultmanager.database.Database;
import op65n.tech.vaultmanager.util.Task;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class VaultManagerPlugin extends JavaPlugin {

    private final DataRegisterable dataRegisterable = new DataRegisterable();
    private final CommandRegisterable commandRegisterable = new CommandRegisterable();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.dataRegisterable.register(this);
        this.commandRegisterable.register(this);
    }

    @Override
    public void onDisable() {
        CompletableFuture.supplyAsync(() -> {
            if (Database.INSTANCE == null) return null;
            Database.INSTANCE.terminateAdapter();
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

}
