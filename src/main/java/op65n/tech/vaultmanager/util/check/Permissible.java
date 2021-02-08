package op65n.tech.vaultmanager.util.check;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public final class Permissible {

    /**
     * Checks if the user has the permission to access the desired vault
     *
     * @param player who's access needs to be checked
     * @param index  index of the specified vault
     * @return {@link Boolean} depending on if the user has specific vault access or not
     */
    public static boolean hasVaultAccess(final Player player, final int index) {
        final Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();

        if (hasAllowedRange(index, permissions))
            return true;

        final Constructor permissionConstructor = new Constructor(index, PermissibleType.VAULT);
        return permissions.stream()
                .map(PermissionAttachmentInfo::getPermission)
                .anyMatch(permission -> permission.equalsIgnoreCase(permissionConstructor.getRequiredPermission()));
    }

    /**
     * Checks if the user has a ranged permission set and the index is within it's range
     *
     * @param index       of the specified vault
     * @param permissions set of player's permissions
     * @return {@link Boolean} depending on if the user has a ranged permission set
     */
    private static boolean hasAllowedRange(final int index, final Set<PermissionAttachmentInfo> permissions) {
        final Constructor permissionConstructor = new Constructor(0, PermissibleType.RANGE);
        final Optional<String> permission = permissions.stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(it -> it.contains(permissionConstructor.getPermissionConstruction()))
                .findAny();

        if (permission.isEmpty())
            return false;

        final String[] components = permission.get().split("\\.");
        return components.length >= 4 && Integer.parseInt(components[3]) >= index;
    }

    /**
     * Returns the allowed vault size for the given player
     *
     * @param player who's vault size needs to be checked
     * @return row size of the allowed vault
     */
    public static int getAllowedVaultSize(final Player player) {
        final Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();

        final Constructor permissionConstructor = new Constructor(0, PermissibleType.VAULT_SIZE);
        final Optional<String> permission = permissions.stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(it -> it.contains(permissionConstructor.getPermissionConstruction()))
                .findAny();

        if (permission.isEmpty())
            return 1;

        final String[] components = permission.get().split("\\.");
        return components.length < 4 ? 1 : Integer.parseInt(components[3]);
    }

    private static class Constructor {

        private final int index;
        private final PermissibleType type;

        Constructor(final int index, final PermissibleType type) {
            this.index = index;
            this.type = type;
        }

        String getRequiredPermission() {
            if (type == PermissibleType.RENAME)
                return PermissibleType.RENAME.getPermissionConstruction();

            return String.format("%s%s", type.getPermissionConstruction(), index);
        }

        String getPermissionConstruction() {
            return type.getPermissionConstruction();
        }

    }

    public enum PermissibleType {

        VAULT_SIZE("vaultmanager.vault.size."),
        VAULT("vaultmanager.vault.access."),
        RANGE("vaultmanager.vault.range."),
        RENAME("vaultmanager.vault.rename"),
        ;

        private final String permission;

        PermissibleType(final String permission) {
            this.permission = permission;
        }

        public String getPermissionConstruction() {
            return this.permission;
        }

    }

}
