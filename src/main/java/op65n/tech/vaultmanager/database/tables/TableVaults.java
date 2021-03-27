package op65n.tech.vaultmanager.database.tables;

import op65n.tech.vaultmanager.database.adapter.ITable;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

public class TableVaults implements ITable {

    @NotNull
    @Language("MariaDB")
    private String database = "vault_manager";

    public TableVaults(@Language("MariaDB") final @NotNull String database) {
        this.database = database;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    @Language("MariaDB")
    public @NotNull String getCreateQuery() {
        return "CREATE TABLE IF NOT EXISTS `" + database + "`.`player_vaults` ( " +
                "  `id` INT NOT NULL AUTO_INCREMENT, " +
                "  `player_uuid` BINARY(16) NOT NULL, " +
                "  `vault_id` INT NOT NULL, " +
                "  `vault_name` VARCHAR(16) NULL DEFAULT NULL, " +
                "  `vault_contents` MEDIUMTEXT NULL, " +
                "  PRIMARY KEY (`id`)) " +
                "ENGINE = InnoDB;";
    }

}
