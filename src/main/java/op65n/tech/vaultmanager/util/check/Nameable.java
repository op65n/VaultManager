package op65n.tech.vaultmanager.util.check;

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
        LENGTH("deny-response.length"),
        UNKNOWN("deny-response.unknown");

        private final String path;

        Response(final String path) {
            this.path = path;
        }

        /**
         * Returns the path of reason for the denial
         *
         * @return {@link String} path to the reason for name denial
         */
        public String getReasonPath() {
            return this.path;
        }
    }

}
