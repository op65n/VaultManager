package op65n.tech.vaultmanager.util.key.impl;

import op65n.tech.vaultmanager.util.key.Keyable;

public record PositionKey(int position) implements Keyable<Integer> {

    @Override
    public Integer getKey() {
        return this.position;
    }

}
