package op65n.tech.vaultmanager.data.impl;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.object.impl.VaultSnapshotImplementation;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.database.Database;
import op65n.tech.vaultmanager.database.api.ConcurrentConnection;
import op65n.tech.vaultmanager.database.api.DataSource;
import op65n.tech.vaultmanager.util.File;
import op65n.tech.vaultmanager.util.Serializable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public final class DatabaseImplementation implements DataProvider {

    private final VaultManagerPlugin plugin;

    public DatabaseImplementation(final VaultManagerPlugin plugin) {
        this.plugin = plugin;

        File.saveResources(
                "database.yml"
        );

        final java.io.File file = new java.io.File(plugin.getDataFolder(), "database.yml");
        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        Database.masterWorkerID = Thread.currentThread().getId();

        final Database database = new Database();
        database.createAdapter(configuration);
    }

    @NotNull
    @Override
    public VaultSnapshot getVaultSnapshot(@NotNull final UUID identifier, final int position) {
        final byte[] binaryUUID = uuidToBin(identifier);

        try {
            final DataSource dataSource = new ConcurrentConnection().borrow();

            final PreparedStatement fetchVaultQuery = dataSource.prepare("SELECT vault_contents, vault_name FROM player_vaults WHERE player_uuid = ? AND vault_id = ?;");
            fetchVaultQuery.setBytes(1,binaryUUID);
            fetchVaultQuery.setInt(2, position);

            final ResultSet resSet = fetchVaultQuery.executeQuery();
            fetchVaultQuery.close();

            if (resSet.next()) {
                final String vaultName = resSet.getString("vault_name");
                final String contentsBase64 = resSet.getString("vault_contents");

                return new VaultSnapshotImplementation(
                        identifier, position, vaultName, Serializable.deserialize(contentsBase64)
                );
            }

            dataSource.free();
        } catch (SQLException e) {
            // TODO: (frosty) add error handling here, maybe shut down a plugin or create some sort of a backup
            e.printStackTrace();
        }

        return new VaultSnapshotImplementation(
                identifier, position,
                null, Collections.emptyMap()
        );
    }

    @Override
    public void setVaultDisplayName(@NotNull final UUID identifier, final int position, @NotNull final String name) {
        final byte[] binaryUUID = uuidToBin(identifier);

        try {
            final Integer vaultID = getVaultID(binaryUUID, position);
            if (vaultID == null) {
                createVault(binaryUUID, position, "", name);
                return;
            }

            final DataSource dataSource = new ConcurrentConnection().borrow();
            final PreparedStatement nameChangeQuery = dataSource.prepare("UPDATE player_vaults SET vault_name = ? WHERE id = ?;");

            nameChangeQuery.setString(1, name);
            nameChangeQuery.setInt(2, vaultID);
            nameChangeQuery.executeQuery();
            nameChangeQuery.close();

            dataSource.free();
        } catch (SQLException e) {
            // TODO: (frosty) add error handling here, maybe shut down a plugin or create some sort of a backup
            e.printStackTrace();
        }
    }

    @Override
    public Integer getVaultPositionByName(@NotNull final UUID identifier, @NotNull final String name) {
        final byte[] binaryUUID = uuidToBin(identifier);
        Integer vaultPosition = null;

        try {
            final DataSource dataSource = new ConcurrentConnection().borrow();

            final PreparedStatement fetchVaultID = dataSource.prepare("SELECT vault_id FROM player_vaults WHERE player_uuid = ? AND vault_name = ?;");
            fetchVaultID.setBytes(1, binaryUUID);
            fetchVaultID.setString(2, name);

            final ResultSet resSet = fetchVaultID.executeQuery();
            fetchVaultID.close();

            if (resSet.next()) vaultPosition = resSet.getInt("vault_id");

            dataSource.free();
        } catch (SQLException e) {
            // TODO: (frosty) add error handling here, maybe shut down a plugin or create some sort of a backup
            e.printStackTrace();
        }

        return vaultPosition;
    }

    @Override
    public void setVaultContents(@NotNull final UUID identifier, final int position, @NotNull final Map<Integer, ItemStack> contents) {
        final byte[] binaryUUID = uuidToBin(identifier);
        final String contentsBase64 = Serializable.serialize(contents);

        try {
            final Integer vaultID = getVaultID(binaryUUID, position);
            if (vaultID == null) {
                createVault(binaryUUID, position, contentsBase64, null);
                return;
            }

            final DataSource dataSource = new ConcurrentConnection().borrow();
            final PreparedStatement updateVaultQuery = dataSource.prepare("UPDATE player_vaults SET vault_contents = ? WHERE id = ?;");

            updateVaultQuery.setString(1, contentsBase64);
            updateVaultQuery.setInt(2, vaultID);
            updateVaultQuery.executeQuery();
            updateVaultQuery.close();

            dataSource.free();
        } catch (SQLException e) {
            // TODO: (frosty) add error handling here, maybe shut down a plugin or create some sort of a backup
            e.printStackTrace();
        }
    }

    private Integer getVaultID(final byte[] binaryUUID, final int position) throws SQLException {
        final DataSource dataSource = new ConcurrentConnection().borrow();

        final PreparedStatement fetchVID = dataSource.prepare("SELECT id FROM player_vaults WHERE player_uuid = ? AND vault_id = ?;");
        fetchVID.setBytes(1, binaryUUID);
        fetchVID.setInt(2, position);

        final ResultSet resSet = fetchVID.executeQuery();
        fetchVID.close();
        dataSource.free();

        return resSet.next() ? resSet.getInt("id") : null;
    }

    private void createVault(final byte[] binaryUUID, final int position, final String contentsBase64, final String name) throws SQLException {
        final DataSource dataSource = new ConcurrentConnection().borrow();

        PreparedStatement vaultCreationQuery = dataSource.prepare("INSERT INTO player_vaults (player_uuid, vault_id, vault_contents, vault_name) VALUES (?, ?, ?, ?);");
        vaultCreationQuery.setBytes(1, binaryUUID);
        vaultCreationQuery.setInt(2, position);
        vaultCreationQuery.setString(3, contentsBase64);
        vaultCreationQuery.setString(4, name);

        vaultCreationQuery.executeQuery();
        vaultCreationQuery.close();

        dataSource.free();
    }

    private byte[] uuidToBin(final UUID uuid){
        return ByteBuffer.wrap(new byte[16])
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
    }

}
