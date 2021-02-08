package op65n.tech.vaultmanager.data.object;

import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface VaultSnapshot {

    @NotNull
    UUID getUniqueIdentifier();

    int getPosition();

    @Nullable
    String getDisplayName();

    @NotNull
    Map<Integer, ItemStack> getContents();

    @NotNull
    default Gui construct(@NotNull final String ownerName) {
        final Gui menu = new Gui(
                9,
                getDisplayName() == null ? String.format("Vault #%s - %s", getPosition(), ownerName) : String.format("Vault %s (#%s) - %s", getDisplayName(), getPosition(), ownerName)
        );

        menu.setDefaultClickAction(event -> event.setCancelled(true));

        getContents().forEach((slot, item) -> menu.setItem(slot, new GuiItem(item)));

        return menu;
    }

}
