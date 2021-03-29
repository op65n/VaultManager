package op65n.tech.vaultmanager.database.adapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import op65n.tech.vaultmanager.database.tables.TableVaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionAdapter {

    private InitStatus status = InitStatus.OK;
    private HikariDataSource hikariDataSource;

    private HikariDataSource createHikariDataSource(final DatabaseConfiguration configuration) {
        final Properties hikariProperties = new Properties();

        // Add all properties in a map to properties
        if (!configuration.properties.isEmpty()) {
            configuration.properties.forEach(hikariProperties::setProperty);
        }

        // Apply the properties to HikariConfig
        HikariConfig hikariConfig = new HikariConfig(hikariProperties);
        // Set some common properties manually from the config
        hikariConfig.setPoolName(configuration.pool);
        hikariConfig.setMaximumPoolSize(configuration.poolSize);
        hikariConfig.setJdbcUrl("jdbc:" + configuration.jdbc + "://" + configuration.ip + ":" + configuration.port + "/" + configuration.database);
        hikariConfig.setUsername(configuration.username);
        hikariConfig.setPassword(configuration.passwd);
        hikariConfig.setDriverClassName(configuration.driverClassName);

        // Create hikari dataSource from the hikari config
        return new HikariDataSource(hikariConfig);
    }

    private void createTables(@Language("MariaDB") String database, final HikariDataSource hikariDataSource) throws SQLException {
        // List of all database table
        final List<ITable> tables = ITable.sort(
                new TableVaults(database)
        );

        final Connection connection = hikariDataSource.getConnection();

        for (final ITable table : tables) {
            final PreparedStatement createQuery = connection.prepareStatement(table.getCreateQuery());
            createQuery.execute();
            createQuery.close();
        }

        connection.close();
    }

    public InitStatus initialize(final @NotNull ConcurrentHashMap<Integer, ConnectionHolder> connectionHolders, final FileConfiguration dbConfig) {
        final DatabaseConfiguration configuration = new DatabaseConfiguration(dbConfig);
        hikariDataSource = createHikariDataSource(configuration);

        try {
            createTables(configuration.database, hikariDataSource);
            for (int index = 0; index < configuration.poolSize; index++) {
                final ConnectionHolder holder = new ConnectionHolder(hikariDataSource.getConnection(), index);
                connectionHolders.put(index, holder);
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            status = InitStatus.ERROR;
            return status;
        }

        return status;
    }

    public HikariDataSource hikariDataSource() {
        return hikariDataSource;
    }

    public enum InitStatus {
        OK, ERROR
    }
}
