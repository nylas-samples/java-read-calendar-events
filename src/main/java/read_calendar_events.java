// Import Java Utilities
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
<<<<<<< Updated upstream
import java.util.Objects;

public class read_calendar_events {
    public static void main(String[] args) throws NylasSdkTimeoutError, NylasApiError {
        // Load the .env file
        Dotenv dotenv = Dotenv.load();
        // Initialize the Nylas client
        NylasClient nylas = new NylasClient.Builder(dotenv.get("V3_TOKEN")).baseUrl(dotenv.get("NYLAS_API_SERVER")).build();
        // Get today's date
        LocalDate today = LocalDate.now();
        // Set time. As we're using UTC we need to add the hours in difference
        // from our own Timezone
        Instant sixPmUtc = today.atTime(8, 0).toInstant(ZoneOffset.UTC);
        // Get time as epoch
        long startTime = sixPmUtc.getEpochSecond();
        // Add 9 hours
        Instant sixPmUtcPlus = sixPmUtc.plus(9, ChronoUnit.HOURS);
        // Get time as epoch
        long endTime = sixPmUtcPlus.getEpochSecond();
        // Build the query parameters to filter our the results
        ListEventQueryParams listEventQueryParams = new ListEventQueryParams.Builder(dotenv.get("CALENDAR_ID"))
                .start(Long.toString(startTime))
                .end(Long.toString(endTime))
                .build();
        // Read the events from our main calendar
        List<Event> events = nylas.events().list(dotenv.get("CALENDAR_ID"), listEventQueryParams).getData();
        // Loop the events
        for (Event event : events){
            // Print the Id and Title
            System.out.print("Id: " + event.getId() + " | ");
            System.out.print("Title: " + event.getTitle());
            //Dates are handled differently depending on the event type
            switch (Objects.requireNonNull(event.getWhen().getObject()).getValue()) {
                case "datespan" -> {
                    // Print event details
                    When.Date date = (When.Date) event.getWhen();
                    System.out.print(" | The date of the event is: " + date.getDate());
                }
                case "timespan" -> {
                    // Print event details
                    When.Timespan timespan = (When.Timespan) event.getWhen();
                    String initDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date((timespan.getStartTime() * 1000L)));
                    String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date((timespan.getEndTime() * 1000L)));
                    System.out.print(" | The time of the event is from: " + initDate + " to " + endDate);
                }
            }
            // Print participant details
            System.out.print(" | Participants: ");
            for(Participant participant : event.getParticipants()){
                System.out.print(" Email: " + participant.getEmail() +
                                 " Name: " + participant.getName() +
                                 " Status: " + participant.getStatus());
            }
            System.out.println("\n");
        }
    }
=======
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
>>>>>>> Stashed changes
}
