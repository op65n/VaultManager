package op65n.tech.vaultmanager.object.menu;

import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.object.impl.PrivateVault;
import op65n.tech.vaultmanager.util.File;
import op65n.tech.vaultmanager.util.Serializable;
import op65n.tech.vaultmanager.util.Task;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class VaultMenu {

    private final VaultManagerPlugin plugin;
    private final Player player;
    private final PrivateVault vault = new PrivateVault();
    private Gui menu;

    public VaultMenu(final VaultManagerPlugin plugin, final Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void assignContents(final List<ItemStack> contents) {
        for (int i = 0; i < contents.size(); i++)
            vault.setContent(i, contents.get(i));
    }

    /**
     * Assigns a name to the given vault
     *
     * @param name to be assigned
     */
    public void assignVaultName(final String name) {
        this.vault.setVaultName(name);
    }

    /**
     * Constructs a menu from the given vault contents
     *
     * @param index {@link PrivateVault}'s index
     */
    public void constructMenu(final int index) {
        this.menu = new Gui(
                6,
                vault.getVaultName(index)
        );

        vault.getContents().forEach((slot, item) ->
                menu.setItem(slot, new GuiItem(item))
        );

        menu.setCloseGuiAction(event -> {
            Task.async(() -> {
                final String base64 = Serializable.encodeInventoryToBase64(event.getInventory().getContents());

                File.setUserConfiguration(plugin, player, index, base64);
            });
        });
    }

    /**
     * Returns the {@link Gui} object
     *
     * @return constructed menu
     */
    public Gui getVaultGui() {
        return this.menu;
    }

}
