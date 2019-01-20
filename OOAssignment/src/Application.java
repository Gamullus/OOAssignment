import enums.TicketDuration;
import enums.TicketKind;
import enums.TicketType;
import ticket.SpecialTicket;
import ticket.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Application {

    private static final Scanner SCANNER = new Scanner(System.in, "UTF-8");

    private static final List<Ticket> TICKETS = new ArrayList<>(3);

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        // ηλεκτρονικό, κανονικό, εβδομαδιαίο, 9
        TICKETS.add(new Ticket(
                TicketKind.DIGITAL,
                TicketType.REGULAR,
                TicketDuration.WEEKLY,
                ticketExpiration(TicketDuration.WEEKLY)
        ));
        // έντυπο, μειωμένο, 11, 6
        TICKETS.add(new Ticket(
                TicketKind.PRINTED,
                TicketType.REDUCED,
                TicketDuration.ELEVEN_ROUTES,
                ticketExpiration(TicketDuration.ELEVEN_ROUTES)
        ));
        // ηλεκτρονικό, κανονικό, μηνιαίο, 30
        TICKETS.add(new SpecialTicket(
                TicketKind.DIGITAL,
                TicketType.REGULAR,
                TicketDuration.MONTHLY,
                ticketExpiration(TicketDuration.MONTHLY),
                "Βασίλειος Δαλάκας", randomTicketId()
        ));
        System.out.print("Welcome. ");
        while(true) {
            System.out.printf(
                    "What would you like to do?\n%s\n%s\n%s\n",
                    "1. Issue ticket", "2. Renew ticket", "3. View ticket info"
            );
            String choice = inputPrompt();
            while(!("1".equals(choice) ||
                    "2".equals(choice) ||
                    "3".equals(choice))) {
                System.out.println(
                        "Invalid choice. Please pick one of '1', '2', '3'."
                );
                choice = inputPrompt();
            }
            switch(choice) {
                case "1": issueTicket(); break;
                case "2": renewTicket(); break;
                case "3": ticketInfo(); break;
            }

            System.out.println("Would you like to do anything else? (yes/no)");
            String reply = inputPrompt().toLowerCase();
            while(!reply.matches("^(y(es)?|no?)$")) {
                System.out.println("Please answer with 'yes' or 'no'.");
                reply = inputPrompt().toLowerCase();
            }
            if(reply.matches("^no?$")) break;
        }
    }

    /**
     * {@summary Έκδοση-αγορά.}
     * Στην συγκεκριμένη περίπτωση επιλέγει πρώτα τον τύπο του εισιτηρίου.
     * Ανάλογα με την επιλογή του μπορεί να επιλέξει είτε πλήθος διαδρομών
     * είτε χρονική διάρκεια. Στην περίπτωση του μειωμένου ή μηνιαίου
     * εισιτηρίου του ζητείται να καταχωρήσει επιπλέον τα στοιχεία του.
     * Τέλος του παρουσιάζεται η τιμή και πρέπει να επιλέξει τρόπο εξόφλησης
     * (μετρητά ή κάρτα). Αφού πληρώσει του παρουσιάζεται η επιλογή παραλαβής
     * του εισιτηρίου (ηλεκτρονικό ή εκτύπωση). Στην περίπτωση που επιλέξει
     * ηλεκτρονικό του ζητείται email αποστολής.
     */
    private static void issueTicket() {
        Ticket ticket;
        System.out.printf(
                "Please choose the type of the ticket.\n%s\n",
                TicketType.namedChoices()
        );
        TicketType type = TicketType.fromInput(inputPrompt());
        while(type == null) {
            System.out.printf(
                    "Invalid choice! Please pick one of %s\n",
                    TicketType.choices()
            );
            type = TicketType.fromInput(inputPrompt());
        }
        System.out.printf(
                "Please choose the duration of the ticket.\n%s\n",
                TicketDuration.namedChoices()
        );
        TicketDuration duration = TicketDuration.fromInput(inputPrompt());
        while(duration == null) {
            System.out.printf(
                    "Invalid choice! Please pick one of %s\n",
                    TicketDuration.choices()
            );
            duration = TicketDuration.fromInput(inputPrompt());
        }
        if(type == TicketType.REDUCED || duration == TicketDuration.MONTHLY) {
            System.out.println("Please input your name.");
            String name = inputPrompt();
            while(name.trim().length() == 0) {
                System.out.println("Cannot be blank. Please try again.");
                name = inputPrompt();
            }
            ticket = new SpecialTicket(
                    null, type, duration,
                    ticketExpiration(duration),
                    name, randomTicketId()
            );
        } else {
            ticket = new Ticket(
                    null, type, duration,
                    ticketExpiration(duration)
            );
        }
        sendOrPrintTicket(ticket);
        TICKETS.add(ticket);
        System.out.println("Your ticket has been issued.");
    }

    /**
     * {@summary Ανανέωση.}
     * Αν επιλέξει την συγκεκριμένη ενέργεια μπορεί να αγοράσει είτε έξτρα
     * διαδρομές, είτε έξτρα χρόνο ανάλογα με τον τύπο του εισιτηρίου.
     * Τέλος του παρουσιάζεται η τιμή και πρέπει να επιλέξει τρόπο εξόφλησης
     * (μετρητά ή κάρτα). Αφού πληρώσει του παρουσιάζεται η επιλογή παραλαβής
     * του εισιτηρίου (ηλεκτρονικό ή εκτύπωση). Στην περίπτωση που
     * επιλέξει ηλεκτρονικό του ζητείται email αποστολής.
     */
    private static void renewTicket() {
        System.out.println("Please choose which ticket to renew.");
        ticketInfo();
        int index = 0;
        while(index < 1 || index > TICKETS.size()) {
            try {
                index = Integer.parseInt(inputPrompt());
            } catch(NumberFormatException ex) {
                index = 0;
                System.out.printf(
                        "Must be a number between 1 and %d.\n",
                        TICKETS.size()
                );
            }
        }
        Ticket ticket = TICKETS.remove(index - 1);
        ticket.setExpiration(ticketExpiration(ticket.getDuration()));
        sendOrPrintTicket(ticket);
        TICKETS.add(ticket);
    }

    /**
     * {@summary Ενημέρωση περιεχομένου.}
     * Αν επιλέξει την ενημέρωση περιεχομένου θα πρέπει να του
     * παρουσιαστούν τα στοιχεία του εισιτηρίου.
     */
    private static void ticketInfo() {
        int i = 0;
        for(Ticket t : TICKETS)
            System.out.println("Ticket " + ++i + ":\n  " + t);
    }

    private static String ticketExpiration(TicketDuration duration) {
        // https://stackoverflow.com/a/12576219
        LocalDateTime now = LocalDateTime.now();
        switch(duration) {
            case NINETY_MINUTES: return FORMATTER.format(now.plusMinutes(90));
            case DAILY: return FORMATTER.format(now.plusDays(1));
            case WEEKLY: return FORMATTER.format(now.plusDays(5));
            case MONTHLY: return FORMATTER.format(now.plusMonths(1));
            case SINGLE_ROUTE: return "1 route";
            case FIVE_ROUTES: return "5 routes";
            case ELEVEN_ROUTES: return "11 routes";
            default: return null;
        }
    }

    private static void sendOrPrintTicket(Ticket ticket) {
        System.out.printf(
                "Please choose a payment method.\n%s\n%s\n",
                "1. Cash", "2. Credit card"
        );
        String method = inputPrompt();
        while(!"1".equals(method) && !"2".equals(method)) {
            System.out.println("Invalid choice. Please pick one of '1', '2'.");
            method = inputPrompt();
        }
        System.out.printf(
                "How would you like to receive the ticket?\n%s\n",
                TicketKind.namedChoices()
        );
        TicketKind kind = TicketKind.fromInput(inputPrompt());
        while(kind == null) {
            System.out.printf(
                    "Invalid choice! Please pick one of %s\n",
                    TicketKind.choices()
            );
            kind = TicketKind.fromInput(inputPrompt());
        }
        if(kind == TicketKind.DIGITAL) {
            System.out.println("Please enter your email.");
            String email = inputPrompt();
            while(!email.matches("^[^@]+@[^@]+[.][^@]+$")) {
                System.out.println("Invalid email! Please try again.");
                email = inputPrompt();
            }
        }
        ticket.setKind(kind);
    }

    private static String randomTicketId() {
        // https://stackoverflow.com/a/1389747
        return UUID.randomUUID().toString();
    }

    private static String inputPrompt() {
        System.out.print("> ");
        return SCANNER.nextLine();
    }
}

