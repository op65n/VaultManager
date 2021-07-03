package op65n.tech.vaultmanager.data.object.impl;

import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public record VaultSnapshotImplementation(UUID identifier, int position, String name, Map<Integer, ItemStack> contents) implements VaultSnapshot {

    public VaultSnapshotImplementation(@NotNull final UUID identifier, final int position, @Nullable final String name, @NotNull final Map<Integer, ItemStack> contents) {
        this.identifier = identifier;
        this.position = position;
        this.name = name;
        this.contents = contents;
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
}
