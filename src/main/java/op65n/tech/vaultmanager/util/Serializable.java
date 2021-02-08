package op65n.tech.vaultmanager.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Serializable {

    private static final String EMPTY_STRING = "";

    /**
     * Deserializes the given contents {@link String} and returns them as a map
     *
     * @param serialization {@link String} to be deserialized
     * @return {@link Map} of the deserialized contents
     */
    @NotNull
    public static Map<Integer, ItemStack> deserialize(@Nullable final String serialization) {
        if (serialization == null || serialization.isEmpty())
            return Collections.emptyMap();

        final YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.loadFromString(serialization);
        } catch (final InvalidConfigurationException exception) {
            return Collections.emptyMap();
        }

        final ConfigurationSection section = configuration.getConfigurationSection("contents");
        if (section == null)
            return Collections.emptyMap();

        final Map<Integer, ItemStack> contents = new HashMap<>();
        for (final String key : section.getKeys(false)) {
            contents.put(
                    Integer.parseInt(key),
                    section.getItemStack(key)
            );
        }

        return contents;
    }

    /**
     * Returns a serialized {@link String} from the given {@link Map}
     * or an empty string if the given contents are empty
     *
     * @param contents to be serialized
     * @return A string containing the serialized contents
     */
    public static String serialize(@NotNull final Map<Integer, ItemStack> contents) {
        if (contents.isEmpty())
            return EMPTY_STRING;

        return handleSerialization(contents);
    }

    /**
     * Serializes the given {@link Map} to a {@link String}
     *
     * @param contents to be serialized
     * @return A string containing the serialized contents
     */
    private static String handleSerialization(@NotNull final Map<Integer, ItemStack> contents) {
        final YamlConfiguration configuration = new YamlConfiguration();

        for (final int slot : contents.keySet()) {
            final ItemStack item = contents.get(slot);

            configuration.set(
                    String.format("contents.%s", slot),
                    item
            );
        }

        return Base64.getEncoder().encodeToString(configuration.saveToString().getBytes());
    }

}
