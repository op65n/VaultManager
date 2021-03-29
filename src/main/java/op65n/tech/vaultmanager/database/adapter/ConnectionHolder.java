package op65n.tech.vaultmanager.database.adapter;

import op65n.tech.vaultmanager.database.Database;
import op65n.tech.vaultmanager.database.api.DataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionHolder implements DataSource {

    public ConnectionHolder(final @NotNull Connection connection, final int CID) {
        this.connection = connection;
        this.CID = CID;
        creationTime = System.currentTimeMillis();
    }

    private long creationTime;
    private long reservedUntil = 0;
    private Connection connection;
    public final int CID;

    public boolean isReserved() {
        return reservedUntil == -1 || System.currentTimeMillis() < reservedUntil + 5L;
    }

    public void terminate() throws SQLException {
        if (!connection.isClosed()) connection.close();
    }

    public void take() {
        reservedUntil = -1;
    }

    @Override
    public void borrow() {
        reservedUntil = System.currentTimeMillis() + 6000L;
        Database.INSTANCE.connectionHolder(this);
    }

    @Override
    public void borrow(final int queries) {
        reservedUntil = System.currentTimeMillis() + ((long) queries * 6000L);
        Database.INSTANCE.connectionHolder(this);
    }

    @Override
    public void free() {
        this.reservedUntil = 0;
        Database.INSTANCE.connectionHolder(this);
    }

    @Override
    public @NotNull PreparedStatement prepare(final @NotNull String query) throws SQLException {
        if (System.currentTimeMillis() - creationTime > 600000) {
            try {
                connection.close();
            } catch (final SQLException e) {
                System.out.println("Show this error to Nzd_1");
                e.printStackTrace();
                System.out.println("Show this error to Nzd_1");
            }
            connection = Database.INSTANCE.hikariDataSource.getConnection();
            creationTime = System.currentTimeMillis();
        }
        return connection.prepareStatement(query);
    }

}