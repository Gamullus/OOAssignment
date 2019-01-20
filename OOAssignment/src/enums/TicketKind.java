package enums;

import java.util.Arrays;
import java.util.stream.Collectors;

/** Είδος εισητηρίου. */
public enum TicketKind {

    /** Ηλεκτρονικό */
    DIGITAL("Via email"),

    /** Έντυπο */
    PRINTED("Printed");

    private final String value;

    TicketKind(String value) { this.value = value; }

    public String getValue() { return value; }

    public static String choices() {
        return Arrays.stream(values())
                .map(t -> "'" + (t.ordinal() + 1) + "'")
                .collect(Collectors.joining(", "));
    }

    public static String namedChoices() {
        return Arrays.stream(values())
                .map(t -> (t.ordinal() + 1) + ". " + t.value)
                .collect(Collectors.joining("\n"));
    }

    public static TicketKind fromInput(String input) {
        try {
            return values()[Integer.parseInt(input) - 1];
        } catch(ArrayIndexOutOfBoundsException |
                NumberFormatException ex) {
            return null;
        }
    }
}

