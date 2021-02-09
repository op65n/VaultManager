package op65n.tech.vaultmanager;

import op65n.tech.vaultmanager.command.registerable.CommandRegisterable;
import op65n.tech.vaultmanager.command.registerable.completion.CompletionCache;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.data.registerable.DataRegisterable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
