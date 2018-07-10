package com.pixzen.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {


    private Button setalarmButton;
    private Button alarmoffButton;
    private TextView time;
    private TimePicker timePicker;
    private Calendar calendar;
    private String amPm = "";
    private String minString = "";
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize objects and create instances
        setalarmButton = (Button) findViewById(R.id.setalarmButton);
        alarmoffButton = (Button) findViewById(R.id.alarmoffButton);
        timePicker = (TimePicker) findViewById(R.id.timePicker1);
        time = (TextView) findViewById(R.id.timeTextView);
        calendar = Calendar.getInstance();
        final AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        final Intent alarmIntent = new Intent(MainActivity.this, Alarm.class);

        // set to portrait mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // allow volume buttons to set the alarm volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // set calendar instance to default timezone
        calendar.setTimeZone(java.util.TimeZone.getDefault());

        setalarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set calendar from picker
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());

                // get the hour and min
                int hour = timePicker.getHour();
                int min = timePicker.getMinute();

                // set user text
                setAlarmText("Alarm is set to: ", hour, min);

                // add extra string into intent to tell the clock that the "alarm on" button was pressed
                alarmIntent.putExtra("extra", "alarm on");

                // pending intent to delay the intent until the specified time
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);

                // set the alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });

        alarmoffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set the text to let the user know the alarm is off
                time.setText("Alarm Off");

                // Cancel the pending intent
                if (pendingIntent == null) {
                    // tell user there is no alarm set
                    time.setText("No alarm currently set");
                } else {
                    alarmManager.cancel(pendingIntent);
                }

                // add extra string into intent to tell the clock that the "alarm off" button was pressed
                alarmIntent.putExtra("extra", "alarm off");

                // stop the alarm
                sendBroadcast(alarmIntent);
            }
        });
    }

    private void setAlarmText(String output, int hour, int min) {
        // changes time to 12 hr AM/PM format, ensures proper format
        if (hour == 0) {
            hour += 12;
            amPm = "AM";
        } else if (hour == 12) {
            amPm = "PM";
        } else if (hour > 12) {
            hour -= 12;
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        if (min < 10) {
            minString = "0" + min;
        } else {
            minString = Integer.toString(min);
        }

        time.setText(new StringBuilder().append(output).append(" ").append(hour).append(" : ").append(minString).append(" ").append(amPm));
    }

}
