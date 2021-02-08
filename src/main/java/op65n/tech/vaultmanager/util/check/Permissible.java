package op65n.tech.vaultmanager.util.check;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Set;

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

        final Constructor permissionConstructor = new Constructor(index, PermissibleType.VAULT);
        return permissions.stream()
                .map(PermissionAttachmentInfo::getPermission)
                .anyMatch(permission -> permission.equalsIgnoreCase(permissionConstructor.getRequiredPermission()));
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

    }

    public enum PermissibleType {

        VAULT("vaultmanager.vault.access."),
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
