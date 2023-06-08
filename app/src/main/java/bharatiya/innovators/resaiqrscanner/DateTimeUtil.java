package bharatiya.innovators.resaiqrscanner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DAY_OF_WEEK_FORMAT = "EEE";

    public DateTimeUtil() {
        getTimeStamp();
        FirebaseDatabase.getInstance().getReference()
                .child("timestamp").setValue(ServerValue.TIMESTAMP);
    }

    // Method to retrieve the current timestamp in milliseconds
    static long timeStamp;

    public static long getTimeStamp() {
        FirebaseDatabase.getInstance().getReference()
                .child("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Object timestampObj = dataSnapshot.getValue();
                        if (timestampObj instanceof Long) {
                            timeStamp = (Long) timestampObj;
                            // Use the server timestamp here
                        } else {
                            // Handle error: Invalid server timestamp
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error: Failed to retrieve server timestamp
                    }
                });
        return timeStamp;
    }

    public interface TimeCallback {
        void onTimeReceived(long serverTime);

        void onTimeError(String errorMessage);
    }

    // Method to get the current datetime as a formatted string
    public String getCurrentDateTime() {
        return formatTimestamp(getTimeStamp(), DATE_FORMAT + " " + TIME_FORMAT);
    }

    private static String formatTimestamp(long timestamp, String format) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }

    public boolean isTimeElapsed(long previousTimestamp, int minutes) {
        long elapsedSeconds = (getTimeStamp() - previousTimestamp) / 1000;
        long elapsedMinutes = elapsedSeconds / 60;
        return elapsedMinutes >= minutes;
    }

    // Helper method to get the Calendar instance
    private static Calendar getCalendar() {
        return Calendar.getInstance();
    }
}
