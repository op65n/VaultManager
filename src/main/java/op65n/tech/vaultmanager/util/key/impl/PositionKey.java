package op65n.tech.vaultmanager.util.key.impl;

import op65n.tech.vaultmanager.util.key.Keyable;

public final class PositionKey implements Keyable<Integer> {

    private final int position;

    public PositionKey(final int position) {
        this.position = position;
    }

    @Override
    public Integer getKey() {
        return this.position;
    }

}
