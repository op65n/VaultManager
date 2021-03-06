package op65n.tech.vaultmanager.util.check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Nameable {

    private static final Pattern PATTERN = Pattern.compile("^\\w{3,16}$");

    /**
     * Checks the validity of a given {@link String} and returns
     * a corresponding {@link Response}
     *
     * @param input {@link String} to be checked
     * @return {@link Response} conforming the result of the validity check
     */
    public static Response checkValidity(final String input) {
        if (input == null || input.isEmpty())
            return Response.INVALID;

        final Matcher matcher = PATTERN.matcher(input);

        if (matcher.matches())
            return Response.VALID;

        if (!matcher.matches())
            return Response.INVALID;

        if (input.length() < 3 || input.length() > 16)
            return Response.LENGTH;

        return Response.UNKNOWN;
    }

    public enum Response {

        // Confirmation
        VALID(null),

        // Denial
        LENGTH("deny-response.length"),
        INVALID("deny-response.invalid"),
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
