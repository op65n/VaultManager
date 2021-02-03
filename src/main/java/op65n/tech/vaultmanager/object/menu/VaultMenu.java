package op65n.tech.vaultmanager.object.menu;

import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.object.impl.PrivateVault;
import op65n.tech.vaultmanager.util.File;
import op65n.tech.vaultmanager.util.Serializable;
import org.bukkit.entity.Player;

public final class VaultMenu {

    private final VaultManagerPlugin plugin;
    private final Player player;

    private PrivateVault vault;

    private Gui menu;

    public VaultMenu(final VaultManagerPlugin plugin, final Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    /**
     * Assigns a {@link PrivateVault} to be used within this menu
     *
     * @param vault to be assigned to this menu
     */
    public void assignVault(final PrivateVault vault) {
        this.vault = vault;
    }

    /**
     * Constructs a menu from the given vault contents
     *
     * @param index {@link PrivateVault}'s index
     */
    public void constructMenu(final int index) {
        this.menu = new Gui(
                9,
                vault.getVaultName(index)
        );

        vault.getContents().forEach((slot, item) ->
            menu.setItem(slot, new GuiItem(item))
        );

        menu.setCloseGuiAction(event -> {
            final String base64 = Serializable.toBase64(event.getInventory().getContents());

            File.setUserConfiguration(plugin, player, vault, index, base64);
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
