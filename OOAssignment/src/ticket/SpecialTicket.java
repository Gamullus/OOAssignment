package ticket;

import enums.TicketDuration;
import enums.TicketKind;
import enums.TicketType;

public class SpecialTicket extends Ticket {

    private String name;

    private String ticketId;

    public SpecialTicket() {}

    public SpecialTicket(TicketKind kind, TicketType type,
            TicketDuration duration, String expiration,
            String name, String ticketId) {
        super(kind, type, duration, expiration);
        this.name = name;
        this.ticketId = ticketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", Name: %s, ID: %s", name, ticketId
        );
    }
}
