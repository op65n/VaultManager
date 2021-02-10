package op65n.tech.vaultmanager.data.impl;

import op65n.tech.vaultmanager.VaultManagerPlugin;
import op65n.tech.vaultmanager.data.object.VaultSnapshot;
import op65n.tech.vaultmanager.data.object.impl.VaultSnapshotImplementation;
import op65n.tech.vaultmanager.data.provider.DataProvider;
import op65n.tech.vaultmanager.database.DataSource;
import op65n.tech.vaultmanager.util.File;
import op65n.tech.vaultmanager.util.Serializable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public final class DatabaseImplementation implements DataProvider {

    private final VaultManagerPlugin plugin;
    private final DataSource dataSource;

    public DatabaseImplementation(final VaultManagerPlugin plugin) {
        this.plugin = plugin;

        File.saveResources(
                "database.yml"
        );

        final java.io.File file = new java.io.File(plugin.getDataFolder(), "database.yml");
        final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        this.dataSource = new DataSource(configuration);

        try {
            var connection = this.dataSource.connection();

            var createTable = connection.prepareStatement(DataSource.CREATE_TABLE_QUERY);
            createTable.execute();
            createTable.close();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public VaultSnapshot getVaultSnapshot(@NotNull final UUID identifier, final int position) {
        final byte[] binaryUUID = uuidToBin(identifier);

        try {
            final Connection connection = dataSource.connection();

            final PreparedStatement fetchVaultQuery = connection.prepareStatement(DataSource.FETCH_VAULT);
            fetchVaultQuery.setBytes(1,binaryUUID);
            fetchVaultQuery.setInt(2, position);

            final ResultSet resSet = fetchVaultQuery.getResultSet();
            fetchVaultQuery.close();

            if (resSet.next()) {
                final String vaultName = resSet.getString("vault_name");
                final String contentsBase64 = resSet.getString("vault_contents");

                return new VaultSnapshotImplementation(
                        identifier, position, vaultName, Serializable.deserialize(contentsBase64)
                );
            }

            connection.close();
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
            final Connection connection = dataSource.connection();

            final Integer vaultID = getVaultID(connection, binaryUUID, position);

            if (vaultID == null) {
                createVault(connection, binaryUUID, position, "", name);
                connection.close();
                return;
            }

            final PreparedStatement nameChangeQuery = connection.prepareStatement(DataSource.UPDATE_VAULT_NAME);
            nameChangeQuery.setString(1, name);
            nameChangeQuery.setInt(2, vaultID);
            nameChangeQuery.execute();
            nameChangeQuery.close();

            connection.close();
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
            final Connection connection = dataSource.connection();

            final PreparedStatement fetchVaultID = connection.prepareStatement(DataSource.GET_VID_BY_NAME);
            fetchVaultID.setBytes(1, binaryUUID);
            fetchVaultID.setString(2, name);

            final ResultSet resSet = fetchVaultID.executeQuery();
            fetchVaultID.close();

            if (resSet.next()) vaultPosition = resSet.getInt("vault_id");
            connection.close();
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
            final Connection connection = dataSource.connection();

            final Integer vaultID = getVaultID(connection, binaryUUID, position);

            if (vaultID == null) {
                createVault(connection, binaryUUID, position, contentsBase64, null);
                connection.close();
                return;
            }

            final PreparedStatement updateVaultQuery = connection.prepareStatement(DataSource.UPDATE_VAULT_CONTENTS);
            updateVaultQuery.setString(1, contentsBase64);
            updateVaultQuery.setInt(2, vaultID);
            updateVaultQuery.execute();
            updateVaultQuery.close();

            connection.close();
        } catch (SQLException e) {
            // TODO: (frosty) add error handling here, maybe shut down a plugin or create some sort of a backup
            e.printStackTrace();
        }
    }

    private Integer getVaultID(final Connection connection, final byte[] binaryUUID, final int position) throws SQLException {
        final PreparedStatement fetchVID = connection.prepareStatement(DataSource.CHECK_FOR_EXISTING_VAULT);
        fetchVID.setBytes(1, binaryUUID);
        fetchVID.setInt(2, position);

        final ResultSet resSet = fetchVID.executeQuery();
        fetchVID.close();

        return resSet.next() ? resSet.getInt("id") : null;
    }

    private void createVault(final Connection connection, final byte[] binaryUUID, final int position, final String contentsBase64, final String name) throws SQLException {
        PreparedStatement vaultCreationQuery = connection.prepareStatement(DataSource.CREATE_NEW_VAULT);
        vaultCreationQuery.setBytes(1, binaryUUID);
        vaultCreationQuery.setInt(2, position);
        vaultCreationQuery.setString(3, contentsBase64);
        vaultCreationQuery.setString(4, name);

        vaultCreationQuery.execute();
        vaultCreationQuery.close();
    }

    private byte[] uuidToBin(final UUID uuid){
        return ByteBuffer.wrap(new byte[16])
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
    }

}
