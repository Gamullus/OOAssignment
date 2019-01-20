package ticket;

import enums.TicketDuration;
import enums.TicketKind;
import enums.TicketType;

public class Ticket {
    private TicketKind kind;

    private TicketType type;

    private TicketDuration duration;

    private String expiration;

    public Ticket() {}

    public Ticket(TicketKind kind, TicketType type,
            TicketDuration duration, String expiration) {
        this.kind = kind;
        this.type = type;
        this.duration = duration;
        this.expiration = expiration;
    }

    public TicketKind getKind() {
        return kind;
    }

    public void setKind(TicketKind kind) {
        this.kind = kind;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public TicketDuration getDuration() {
        return duration;
    }

    public void setDuration(TicketDuration duration) {
        this.duration = duration;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    // https://athenacard.gr/komistra.dev
    public double getPrice() {
        switch(duration) {
            case DAILY: return 4.5;
            case WEEKLY: return 9;
            case MONTHLY: return (type == TicketType.REDUCED) ? 15 : 30;
            case FIVE_ROUTES: return (type == TicketType.REDUCED) ? 3 : 6.5;
            case ELEVEN_ROUTES: return (type == TicketType.REDUCED) ? 6 : 13.5;
            default: return type.getPrice();
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Kind: %s, Type: %s, Duration: %s, " + (
                        expiration.contains("route") ?
                                "Remainder" : "Expiration"
                ) + ": %s, Price: %s€",
                kind.getValue(), type.getValue(),
                duration.getValue(), expiration, getPrice()
        );
    }
}

