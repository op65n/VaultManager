package op65n.tech.vaultmanager.data.registerable;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.data.impl.DatabaseImplementation;
import op65n.tech.vaultmanager.data.impl.FileImplementation;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ConstantConditions")
public final class DataRegisterable {

    private DataProvider provider;

    public void register(@NotNull final VaultManagerPlugin plugin) {
        final String dataProvider = plugin.getConfig().getString("settings.data-provider", "FILE");

        switch (dataProvider.toUpperCase()) {
            case "FILE" -> this.provider = new FileImplementation(plugin);
            case "DATABASE" -> this.provider = new DatabaseImplementation(plugin);
            default -> throw new RuntimeException("Failed to recognize DataProvider type! Available: FILE, DATABASE");
        }
    }

    @NotNull
    public DataProvider getDataProvider() {
        return this.provider;
    }

}
