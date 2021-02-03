package op65n.tech.vaultmanager;

import op65n.tech.vaultmanager.command.registerable.CommandRegisterable;
import op65n.tech.vaultmanager.registry.Registerable;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class VaultManagerPlugin extends JavaPlugin {

    private final Set<Registerable> components = new HashSet<>(Collections.singletonList(
            new CommandRegisterable()
    ));

    @Override
    public void onEnable() {
        saveDefaultConfig();

        saveResources(
                "storage/vault-uuid.yml"
        );

        components.forEach(it -> it.register(this));
    }

    @Override
    public void onDisable() {
        reloadConfig();
    }

    private void saveResources(final String... paths) {
        Arrays.stream(paths).forEach(path -> {
            if (!new File(getDataFolder(), path).exists())
                saveResource(path, false);
        });
    }

}
