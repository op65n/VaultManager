package op65n.tech.vaultmanager.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

public class DataSource {

    private final HikariDataSource hikariDataSource;

    public DataSource(final @NotNull FileConfiguration configuration) {
        // Custom Hikari configuration
        final ConfigurationSection propertiesSection = configuration.getConfigurationSection("properties");
        final HikariConfig hikariConfig = loadHikariConfig(propertiesSection);

        // Hikari Pool name
        final String hikariPoolName = configuration.getString("pool");
        hikariConfig.setPoolName(hikariPoolName);

        // Database JDBC URI
        final String jdbc = configuration.getString("jdbc");
        final String ip = configuration.getString("ip");
        final String port = configuration.getString("port");
        final String database = configuration.getString("database");
        hikariConfig.setJdbcUrl("jdbc:" + jdbc + "://" + ip + ":" + port + "/" + database);

        // Driver class path
        final String driverClassPath = configuration.getString("driver");
        hikariConfig.setDriverClassName(driverClassPath);

        // Username and passwd for the database
        final String username = configuration.getString("username");
        hikariConfig.setUsername(username);
        final String passwd = configuration.getString("passwd");
        hikariConfig.setPassword(passwd);

        hikariDataSource = new HikariDataSource(hikariConfig);
        DataSource.DATABASE = database;
    }

    private HikariConfig loadHikariConfig(final @Nullable ConfigurationSection section) {
        if (section == null) return new HikariConfig();

        final Properties generatedHikariProps = new Properties();
        final Set<String> keys = section.getKeys(false);
        keys.forEach(key -> {
            final String value = section.getString(key);
            generatedHikariProps.setProperty(key, value);
        });

        return new HikariConfig(generatedHikariProps);
    }

    public Connection connection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    public static String DATABASE = "vault_manager_db";

    @Language("MariaDB")
    @NotNull
    public static String CREATE_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS `" + DATABASE + "`.`player_vaults` ( " +
            "  `id` INT NOT NULL AUTO_INCREMENT, " +
            "  `player_uuid` BINARY(16) NOT NULL, " +
            "  `vault_id` INT NOT NULL, " +
            "  `vault_name` VARCHAR(16) NULL DEFAULT NULL, " +
            "  `vault_contents` MEDIUMTEXT NULL, " +
            "  PRIMARY KEY (`id`)) " +
            "ENGINE = InnoDB;";

    @Language("MariaDB")
    @NotNull
    public static String CHECK_FOR_EXISTING_VAULT = "SELECT id FROM player_vaults WHERE player_uuid = ? AND vault_id = ?;";

    @Language("MariaDB")
    @NotNull
    public static String CREATE_NEW_VAULT = "INSERT INTO player_vaults (player_uuid, vault_id, vault_contents, vault_name) VALUES (?, ?, ?, ?);";

    @Language("MariaDB")
    @NotNull
    public static String UPDATE_VAULT_CONTENTS = "UPDATE player_vaults SET vault_contents = ? WHERE id = ?;";

    @Language("MariaDB")
    @NotNull
    public static String GET_VID_BY_NAME = "SELECT vault_id FROM player_vaults WHERE player_uuid = ? AND vault_name = ?;";

    @Language("MariaDB")
    @NotNull
    public static String UPDATE_VAULT_NAME = "UPDATE player_vaults SET vault_name = ? WHERE id = ?;";

    @Language("MariaDB")
    @NotNull
    public static String FETCH_VAULT = "SELECT vault_contents, vault_name FROM player_vaults WHERE player_uuid = ? AND vault_id = ?;";



}
