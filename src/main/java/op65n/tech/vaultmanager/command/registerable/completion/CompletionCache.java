package op65n.tech.vaultmanager.command.registerable.completion;

import java.util.*;

public final class CompletionCache {

    private final Map<UUID, List<String>> completions = new HashMap<>();

    /**
     * Returns the vault completions for the given player, or an empty collection
     *
     * @param identifier of the given player
     * @return completions for the given player's named vault
     */
    public List<String> getCompletionsFor(final UUID identifier) {
        return this.completions.getOrDefault(identifier, Collections.emptyList());
    }

    /**
     * Set's the completions
     *
     * @param completions given completions
     */
    public void setCompletions(final Map<UUID, List<String>> completions) {
        completions.forEach(completions::put);
    }

    /**
     * Set's the given user's completions
     *
     * @param identifier  of the given player
     * @param completions of the given player
     */
    public void setCompletionsForIdentifier(final UUID identifier, final List<String> completions) {
        this.completions.put(identifier, completions);
    }

}
