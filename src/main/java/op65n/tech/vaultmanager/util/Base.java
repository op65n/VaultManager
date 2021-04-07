package op65n.tech.vaultmanager.util;

import net.md_5.bungee.api.ChatColor;
import op65n.tech.vaultmanager.util.item.ItemNBT;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class Base {

    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]){6}");

    /**
     * Translates Hex and ChatColor's within a given string
     *
     * @param input Given {@link String} to be colorized
     * @return Translated string
     */
    public static String translate(String input) {
        Matcher matcher = HEX_PATTERN.matcher(input);

        while (matcher.find()) {
            final String hexString = matcher.group();

            final ChatColor hex = ChatColor.of(hexString);
            final String before = input.substring(0, matcher.start());
            final String after = input.substring(matcher.end());

            input = before + hex + after;
            matcher = HEX_PATTERN.matcher(input);
        }

        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * Translates Hex and ChatColor's within a given string
     *
     * @param input Given {@link List<String>} to be colorized
     * @return Translated list
     */
    public static List<String> translate(final List<String> input) {
        return input.stream().map(Base::translate).collect(Collectors.toList());
    }

    /**
     * Colorizes and sends a message to the specified {@link CommandSender}
     *
     * @param sender  Receiver of the message
     * @param message Message to be sent
     */
    public static void sendMessage(final CommandSender sender, final String message, final Object... replacements) {
        sender.sendMessage(translate(replaceString(message, replacements)));
    }

    public static void sendMessage(final CommandSender sender, final List<String> message, final Object... replacements) {
        message.forEach(it -> sendMessage(sender, it, replacements));
    }

    /**
     * Replaces a list with the given {@link Object...} replacement pairs
     *
     * @param input  list of values
     * @param values of replacement pairs
     * @return Input list with the given values replaced
     */
    public static List<String> replaceList(final List<String> input, Object... values) {
        if (values == null) {
            return input;
        }

        final Map<Object, Object> replacements = getReplacements(values);

        final List<String> replacedLore = new ArrayList<>();
        for (String line : input) {
            for (final Object key : replacements.keySet()) {
                line = line.replace(key.toString(), replacements.get(key).toString());
            }
            replacedLore.add(line);
        }
        return replacedLore;
    }

    /**
     * Replace a string with the given {@link Object...} replacement pairs
     *
     * @param input  string value
     * @param values of replacement pairs
     * @return Input string with the given values replaced
     */
    public static String replaceString(final String input, final Object... values) {
        if (values == null) {
            return input;
        }

        final Map<Object, Object> replacements = getReplacements(values);
        String replacedString = input;
        for (final Object key : replacements.keySet()) {
            replacedString = replacedString.replace(key.toString(), replacements.get(key).toString());
        }
        return replacedString;
    }

    /**
     * Returns a {@link Map} with the mapped replacement pairs
     *
     * @param values of replacement pairs
     * @return {@link Map} of mapped replacements
     */
    private static Map<Object, Object> getReplacements(final Object... values) {
        final Map<Object, Object> replacements = new HashMap<>();
        for (int i = 0; i < values.length - 1; i += 2) {
            final Object key = values[i];
            final Object value = values[i + 1];
            replacements.put(key, value);
        }

        return replacements;
    }

    /**
     * Clears the NBT Tag data within a users inventory
     * for the key "mf-gui"
     *
     * @param inventory Inventory to clear nbt data for
     */
    public static void removeNBTData(final Inventory inventory) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType() == Material.AIR) continue;

            item = ItemNBT.removeNBTTag(item, "mf-gui");

            inventory.setItem(slot, item);
        }

        inventory.getViewers().stream().map(it -> (Player) it).forEach(Player::updateInventory);
    }

}
