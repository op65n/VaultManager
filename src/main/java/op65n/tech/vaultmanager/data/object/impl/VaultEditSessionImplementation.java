package op65n.tech.vaultmanager.data.object.impl;

import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class VaultEditSessionImplementation implements VaultSnapshot {

    private final DataProvider provider;

    private final UUID identifier;
    private final int position;
    private final String name;
    private final Map<Integer, ItemStack> contents;

    public VaultEditSessionImplementation(@NotNull final DataProvider provider, @NotNull final UUID identifier, final int position) {
        this.provider = provider;
        final VaultSnapshot vaultSnapshot = provider.getVaultSnapshot(identifier, position);

        assert vaultSnapshot != null;
        this.identifier = vaultSnapshot.getUniqueIdentifier();
        this.position = vaultSnapshot.getPosition();
        this.name = vaultSnapshot.getDisplayName();
        this.contents = vaultSnapshot.getContents();
    }

    @NotNull
    @Override
    public UUID getUniqueIdentifier() {
        return this.identifier;
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return this.name;
    }

    @NotNull
    @Override
    public Map<Integer, ItemStack> getContents() {
        return this.contents;
    }

    @Override
    public @NotNull Gui construct(@NotNull final String ownerName) {
        final Gui gui = new Gui(
                9,
                this.name == null ? String.format("Vault #%s", position) : this.name
        );

        gui.setCloseGuiAction(event ->
                provider.setVaultContents(identifier, position, getInventoryContents(gui.getGuiItems()))
        );

        this.contents.forEach((slot, item) -> gui.setItem(slot, new GuiItem(item)));

        return gui;
    }

    private Map<Integer, ItemStack> getInventoryContents(final Map<Integer, GuiItem> contents) {
        final Map<Integer, ItemStack> result = new HashMap<>();

        for (final int slot : contents.keySet()) {
            final GuiItem item = contents.get(slot);

            result.put(slot, item.getItemStack());
        }

        return result;
    }

}
