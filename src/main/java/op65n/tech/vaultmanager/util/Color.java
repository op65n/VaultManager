package op65n.tech.vaultmanager.util;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Color {

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
        return input.stream().map(Color::translate).collect(Collectors.toList());
    }

}
