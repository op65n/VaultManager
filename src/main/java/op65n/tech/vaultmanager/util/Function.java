package op65n.tech.vaultmanager.util;

import java.util.concurrent.CompletableFuture;

public final class Function {

    /**
     * Executes a supplied runnable async
     *
     * @param runnable to be performed
     * @return {@link CompletableFuture<Void>} of the given {@link Runnable}
     */
    public static CompletableFuture<Void> perform(final Runnable runnable) {
        return CompletableFuture.runAsync(
                runnable
        );
    }

}
