package enums;

import java.util.Arrays;
import java.util.stream.Collectors;

/** Διάρκεια εισητηρίου */
public enum TicketDuration {

    /** 90 λεπτά */
    NINETY_MINUTES("90 minutes"),

    /** ημερίσιο */
    DAILY("Daily"),

    /** εβδομαδιαίο */
    WEEKLY("Weekly"),

    /** μηνιαίο */
    MONTHLY("Monthly"),

    /** 1 διαδρομή */
    SINGLE_ROUTE("1 route"),

    /** 5 διαδρομές */
    FIVE_ROUTES("5 routes"),

    /** 11 διαδρομές */
    ELEVEN_ROUTES("10 + 1 routes");

    private final String value;

    TicketDuration(String value) { this.value = value; }

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

    public static TicketDuration fromInput(String input) {
        try {
            return values()[Integer.parseInt(input) - 1];
        } catch(ArrayIndexOutOfBoundsException |
                NumberFormatException ex) {
            return null;
        }
    }
}

