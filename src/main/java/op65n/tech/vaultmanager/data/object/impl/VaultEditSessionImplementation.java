package op65n.tech.vaultmanager.data.object.impl;

import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.util.Task;
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
    private final int size;
    private final String name;
    private final Map<Integer, ItemStack> contents;

    public VaultEditSessionImplementation(@NotNull final DataProvider provider, @NotNull final UUID identifier, final int position, final int size) {
        this.provider = provider;
        final VaultSnapshot vaultSnapshot = provider.getVaultSnapshot(identifier, position);

        this.identifier = vaultSnapshot.getUniqueIdentifier();
        this.position = vaultSnapshot.getPosition();
        this.name = vaultSnapshot.getDisplayName();
        this.contents = vaultSnapshot.getContents();
        this.size = size;
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
                size,
                this.name == null ? String.format("Vault #%s", this.position) : String.format("Vault %s (#%s)", this.name, this.position)
        );

        gui.setCloseGuiAction(event ->
                Task.async(() -> provider.setVaultContents(this.identifier, this.position, getInventoryContents(event.getInventory().getContents())))
        );

        this.contents.forEach((slot, item) -> gui.setItem(slot, new GuiItem(item)));

        return gui;
    }

    private Map<Integer, ItemStack> getInventoryContents(final ItemStack[] contents) {
        final Map<Integer, ItemStack> result = new HashMap<>();

        for (int slot = 0; slot < contents.length; slot++) {
            final ItemStack item = contents[slot];

            result.put(slot, item);
        }

        return result;
    }

}
