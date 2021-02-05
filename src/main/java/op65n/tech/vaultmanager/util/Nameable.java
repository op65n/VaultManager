package op65n.tech.vaultmanager.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Nameable {

    private static final Pattern PATTERN = Pattern.compile("/\\^\\w{3,16}\\$/i");

    public static Response checkValidity(final String input) {
        final Matcher matcher = PATTERN.matcher(input);

        if (matcher.matches())
            return Response.VALID;

        if (input.length() < 3 || input.length() > 16)
            return Response.LENGTH;

        return Response.UNKNOWN;
    }

    public enum Response {

        // Confirmation
        VALID(null),

        // Denial
        LENGTH("The given name is too short, or too long."),
        UNKNOWN("The given name was rejected for an unknown reason, contact an Administrator if you think this is false.")
        ;

        private final String reason;
        Response(final String reason) {
            this.reason = reason;
        }

        /**
         * Returns the reason for the denial
         *
         * @return {@link String} reason for name denial
         */
        public String getReason() {
            return this.reason;
        }
    }

}
