package op65n.tech.vaultmanager.data.object;

import com.github.frcsty.frozenactions.util.Color;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface VaultSnapshot {

    /**
     * Returns the owning player's {@link UUID}
     *
     * @return {@link UUID} of the owning player
     */
    @NotNull UUID getUniqueIdentifier();

    /**
     * Returns the vault's position
     *
     * @return the index position of the vault
     */
    int getPosition();

    /**
     * Returns the vault's custom name or null if not present
     *
     * @return vault's custom name or null
     */
    @Nullable String getDisplayName();

    /**
     * Returns the vault's contents or an empty map
     *
     * @return contents of the vault or an empty map
     */
    @NotNull Map<Integer, ItemStack> getContents();

    /**
     * Constructs an inspect menu as a default
     *
     * @param ownerName name of the given player
     * @return A gui constructed from the vault contents
     */
    @NotNull
    default Gui construct(@NotNull final String ownerName, final boolean inspect) {
        final Gui menu = new Gui(
                6,
                Color.translate(getDisplayName() == null
                        ? String.format("&4Shramba #%s &8- %s", getPosition(), ownerName)
                        : String.format("&4Shramba &5\"%s\" &8(&0#%s&8) - %s", getDisplayName(), getPosition(), ownerName))
        );

        menu.setDefaultClickAction(event -> event.setCancelled(true));

        getContents().forEach((slot, item) -> menu.setItem(slot, new GuiItem(item)));

        return menu;
    }

}
