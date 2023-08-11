// Import Nylas packages
import com.nylas.NylasClient;
import com.nylas.models.When;

// Import DotEnv to handle .env files
import com.nylas.models.*;
import io.github.cdimascio.dotenv.Dotenv;

// Import Java packages
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class read_calendar_events {
    public static void main(String[] args) throws NylasSdkTimeoutError, NylasApiError {
        // Load the .env file
        Dotenv dotenv = Dotenv.load();
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
}
