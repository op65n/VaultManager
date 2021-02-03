package op65n.tech.vaultmanager;

import op65n.tech.vaultmanager.command.registerable.CommandRegisterable;
import op65n.tech.vaultmanager.registry.Registerable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class VaultManagerPlugin extends JavaPlugin {

    private final Set<Registerable> components = new HashSet<>(Arrays.asList(
            new CommandRegisterable()
    ));

    @Override
    public void onEnable() {
        saveDefaultConfig();

        components.forEach(it -> it.register(this));
    }

    @Override
    public void onDisable() {
        reloadConfig();
    }

}
