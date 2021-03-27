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
    }

    private long reservedUntil = 0;
    private final Connection connection;
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
        return connection.prepareStatement(query);
    }

}