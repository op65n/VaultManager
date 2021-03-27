package op65n.tech.vaultmanager.database.api;

import op65n.tech.vaultmanager.database.Database;
import op65n.tech.vaultmanager.database.adapter.ConnectionHolder;

public class ConcurrentConnection {

    public DataSource borrow() {
        return connectionHolder(Request.BORROW);
    }

    public DataSource take() {
        return connectionHolder(Request.TAKE);
    }

    /**
     * Returns a ConnectionHolder that has a free connection if none are available at the time
     * this method hangs until it finds a ConnectionHolder. Because of that this cannot be
     * used on main server thread, if this method is called from the main thread "Server thread"
     * method will throw an exception.
     *
     * @return ConnectionHolder instance with a free connection
     */
    protected ConnectionHolder connectionHolder(final Request request) {
        final Thread worker = Thread.currentThread();

        if (worker.getId() == Database.masterWorkerID) {
            throw new RuntimeException("You cannot request a connection from the main server thread!");
        }
        Database.INSTANCE.workerQueue.add(worker);

        while (true) {
            for (final ConnectionHolder holder : Database.INSTANCE.connectionHolders.values()) {
                // If entry is reserved move to the next one
                if (holder.isReserved()) continue;
                // Worker que shouldn't be empty if this is running
                assert Database.INSTANCE.workerQueue.peek() != null;
                // FIFO design, this doesn't scale well but I don't see a point in improving this right now
                if (Database.INSTANCE.workerQueue.peek().getId() != worker.getId()) continue;

                // Prepare ConnectionHolder for use, update it in the map and remove worker from que
                holder.free();
                if (request == Request.BORROW) holder.borrow();
                if (request == Request.TAKE) holder.take();
                Database.INSTANCE.connectionHolder(holder);
                Database.INSTANCE.workerQueue.remove(worker);

                return holder;
            }
        }
    }

    private enum Request {
        BORROW, TAKE
    }

}
