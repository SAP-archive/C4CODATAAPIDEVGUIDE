package main.java.venkyvb.odata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import main.java.venkyvb.odata.ticket.Note;
import main.java.venkyvb.odata.ticket.NoteType;
import main.java.venkyvb.odata.ticket.Ticket;

/*
 * Please use the data appropriate for your scenario in the calls below !!
 */
public class App 
{
	private static final Logger logger = Logger
			.getLogger(App.class.getName());
	
    public static void main( String[] args ) throws Exception
    {
        ServiceTicketODataConsumer consumer = new ServiceTicketODataConsumer();
        
        // Read ticket list
        consumer.readTickets();
        
        // Ticket creation
        Ticket newTicket = new Ticket();
        newTicket.setIssueCategory("CA_199");
        newTicket.setProductId("P400101");
        newTicket.setSubject("Testing ticket creation via OData");
        newTicket.setAccountId("000000000000000000000000000000000000000000000000000001001222");
        newTicket.setContactId("000000000000000000000000000000000000000000000000000001001223");
        
        // Add an incident description
        Note incDesc = new Note();
        incDesc.setNoteType(NoteType.INC_DESC);
        incDesc.setNoteDescription("Incident description for ticket creation via OData");
        
        List<Note> incDescList = new ArrayList<Note>();
        incDescList.add(incDesc);
        
        newTicket.setNotes(incDescList);
        
        Optional<String> ticketUUID = consumer.createTicket(newTicket);
        
        logger.info("Ticket UUID " + ticketUUID.get());
        
        // Read the ticket by Id
        Ticket updateTicket = consumer.readTicketById(ticketUUID.get());
        
       
        // Add a new note..
        Note customerNote = new Note();
        customerNote.setNoteType(NoteType.CUST_NOTE);
        customerNote.setNoteDescription("Updated comment from the customer");
        
        consumer.updateTicket(updateTicket, customerNote);
        
        // Read ticket after the update
        
        Ticket updateResult = consumer.readTicketById(ticketUUID.get());
        
        logger.info(updateResult.toString());
    }
}
