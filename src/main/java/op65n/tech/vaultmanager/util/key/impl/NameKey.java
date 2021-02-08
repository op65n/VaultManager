package op65n.tech.vaultmanager.util.key.impl;

import op65n.tech.vaultmanager.util.key.Keyable;

public final class NameKey implements Keyable<String> {

    private final String name;

    public NameKey(final String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return this.name;
    }

}
