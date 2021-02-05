package op65n.tech.vaultmanager.util;

import op65n.tech.vaultmanager.object.impl.PrivateVault;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public final class Serializable {

    private static final String EMPTY_STRING = "";

    /**
     * Serializes the given {@link ItemStack[]} and returns
     * the serialized {@link String} for storing
     *
     * @param contents {@link ItemStack[]} to be serialized
     * @return Base64 {@link String} from the given vault or empty string if an issue occurred
     */
    public static String toBase64(final ItemStack[] contents) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BukkitObjectOutputStream dataOutputStream;

        try {
            dataOutputStream = new BukkitObjectOutputStream(outputStream);

            dataOutputStream.writeInt(contents.length);
        } catch (final IOException exception) {
            return EMPTY_STRING;
        }

        for (int index = 0; index < contents.length; index++) {
            final ItemStack item = contents[index];

            if (item == null || item.getType() == Material.AIR)
                continue;

            try {
                dataOutputStream.writeInt(index);
                dataOutputStream.writeObject(item);
            } catch (final IOException ignored) {
            }
        }

        try {
            dataOutputStream.close();
        } catch (final IOException exception) {
            return EMPTY_STRING;
        }

        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    /**
     * Deserializes a given Base64 to a {@link PrivateVault} object
     *
     * @param base64 {@link String} to be deserialized into a {@link PrivateVault}
     * @return {@link PrivateVault} deserialized from the input Base64 {@link String}
     */
    public static PrivateVault fromBase64(final String base64) {
        final PrivateVault result = new PrivateVault();
        if (base64 == null || base64.isEmpty() || base64.isBlank())
            return result;

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decode(base64));
        final BukkitObjectInputStream dataInputStream;

        try {
            dataInputStream = new BukkitObjectInputStream(inputStream);

            for (int index = 0; index < dataInputStream.readInt(); index++) {
                result.setContent(dataInputStream.readInt(), (ItemStack) dataInputStream.readObject());
            }

            dataInputStream.close();
        } catch (final IOException | ClassNotFoundException exception) {
            return result;
        }

        return result;
    }

    public static String encodeInventoryToBase64(final ItemStack[] contents) {
        final YamlConfiguration configuration = new YamlConfiguration();

        configuration.set("contents", contents);
        return Base64.getEncoder().encodeToString(configuration.saveToString().getBytes());
    }

    @SuppressWarnings("unchecked")
    public static List<ItemStack> decodeBase64ToInventory(final String base64) {
        final YamlConfiguration configuration = new YamlConfiguration();

        if (base64 == null || base64.isEmpty() || base64.isBlank())
            return Collections.emptyList();

        try {
            configuration.loadFromString(base64);
        } catch (final InvalidConfigurationException exception) {
            return Collections.emptyList();
        }

        return ((List<ItemStack>) configuration.get("contents"));
    }

}
