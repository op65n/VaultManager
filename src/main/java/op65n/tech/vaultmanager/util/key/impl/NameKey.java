package op65n.tech.vaultmanager.util.key.impl;

import op65n.tech.vaultmanager.util.key.Keyable;

public record NameKey(String name) implements Keyable<String> {

    @Override
    public String getKey() {
        return this.name;
    }

}
