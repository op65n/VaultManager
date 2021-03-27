package op65n.tech.vaultmanager.database.api;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DataSource {

    void borrow();

    void borrow(final int queries);

    void free();

    @NotNull PreparedStatement prepare(@Language("MariaDB") final @NotNull String query) throws SQLException;

}
