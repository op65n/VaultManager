package op65n.tech.vaultmanager;

import op65n.tech.vaultmanager.command.registerable.CommandRegisterable;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.data.registerable.DataRegisterable;
import op65n.tech.vaultmanager.registry.Registerable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class VaultManagerPlugin extends JavaPlugin {

    private static final Set<Registerable> REGISTERABLES = new HashSet<>(Arrays.asList(
            new DataRegisterable(), new CommandRegisterable()
    ));

    @Override
    public void onEnable() {
        saveDefaultConfig();

        REGISTERABLES.forEach(it -> it.register(this));
    }

    @Override
    public void onDisable() {
        reloadConfig();
    }

    @NotNull
    public DataProvider getDataProvider() {
        final Optional<DataProvider> provider = REGISTERABLES.stream()
                .filter(it -> it instanceof DataRegisterable)
                .map(it -> (DataRegisterable) it)
                .map(DataRegisterable::getDataProvider)
                .findFirst();

        if (provider.isEmpty())
            throw new RuntimeException("Failed to retrieve registered DataProvider!");

        return provider.get();
    }

}
