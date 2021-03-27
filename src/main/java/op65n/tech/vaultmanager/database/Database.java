package op65n.tech.vaultmanager.database;

import op65n.tech.vaultmanager.database.adapter.ConnectionAdapter;
import op65n.tech.vaultmanager.database.adapter.ConnectionHolder;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Database {

    public static Database INSTANCE = null;
    public static long masterWorkerID;

    public Database() {
        if (INSTANCE != null) throw new RuntimeException("There is already instance created for database, aborting!");
        Database.INSTANCE = this;
    }

    public final ConcurrentHashMap<Integer, ConnectionHolder> connectionHolders = new ConcurrentHashMap<>();
    public final ConcurrentLinkedQueue<Thread> workerQueue = new ConcurrentLinkedQueue<>();

    /**
     * Replaces connection holder with a new one, effectively updating it
     *
     * @param holder ConnectionHolder instance
     */
    public void connectionHolder(final @NotNull ConnectionHolder holder) {
        connectionHolders.replace(holder.CID, holder);
    }

    public void createAdapter(final FileConfiguration configuration) {
        final ConnectionAdapter adapter = new ConnectionAdapter();
        final ConnectionAdapter.InitStatus initStatus = adapter.initialize(connectionHolders, configuration);

        if (initStatus == ConnectionAdapter.InitStatus.ERROR) {
            throw new RuntimeException(adapter.error());
        }
    }

    public void terminateAdapter() {
        connectionHolders.forEach((id, holder) -> {
            try {
                holder.terminate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        connectionHolders.clear();
    }

}
