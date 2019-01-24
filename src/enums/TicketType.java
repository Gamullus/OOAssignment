package enums;

import java.util.Arrays;
import java.util.stream.Collectors;

/** Τύπος εισητηρίου. */
public enum TicketType {

    /** Κανονικό */
    REGULAR("Regular", 1.4),

    /** Μειωμένο */
    REDUCED("Reduced", 0.6);

    private final String value;

    private final double price;

    TicketType(String value, double price) {
        this.value = value;
        this.price = price;
    }

    public String getValue() { return value; }

    public double getPrice() { return price; }

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

    public static TicketType fromInput(String input) {
        try {
            return values()[Integer.parseInt(input) - 1];
        } catch(ArrayIndexOutOfBoundsException |
                NumberFormatException ex) {
            return null;
        }
    }
}

