// Import Java Utilities
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
// Import Nylas Packages
import com.nylas.RequestFailedException;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.Event;
import com.nylas.EventQuery;
import com.nylas.Events;
import com.nylas.RemoteCollection;
import com.nylas.Participant;
//Import DotEnv to handle .env files
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
public class ReadEvents {
   public static void main(String[] args) throws RequestFailedException, 
                                                 IOException {
       Dotenv dotenv = Dotenv.load();
       // Create the client object
       NylasClient client = new NylasClient();
       // Connect it to Nylas using the Access Token from the .env file
       NylasAccount account = client.account(dotenv.get("ACCESS_TOKEN"));
       // Access the Events endpoint
       Events events = account.events();
       // Get the first 3 events that happen between 
       // today and next month
       RemoteCollection<Event> events_list = events.list(new EventQuery()
                               .calendarId(dotenv.get("CALENDAR_ID")).limit(3)
                               .startsAfter(Instant.now())
                               .endsBefore(Instant.now().plus(30, 
                                           ChronoUnit.DAYS )));
       // Loop through the events
       for (Event event : events_list){
           System.out.print("Title: " + event.getTitle());
           // Dates are handled differently depending on the event type
           switch (event.getWhen().getObjectType()) {
               case "date":
                   Date date = (Date) event.getWhen();
                   System.out.println(" | The date of the event " + 
                                       date.toString());
                   break;
               case "timespan":
                   Event.Timespan timespan = (Event.Timespan) event.getWhen();
                   System.out.println(" | The time of the event is from: " + 
                                      timespan.getStartTime() +
                                      " to " + timespan.getEndTime());
                   break;
           }
           System.out.print(" | Participants: ");
           // Get all participants
           for(Participant participant : event.getParticipants()){
               System.out.println(" Email: " + participant.getEmail() +
                                  " Name: " + participant.getName() +
                                  " Status: " + participant.getStatus());
           }
           System.out.println("");
       }
   }
}
