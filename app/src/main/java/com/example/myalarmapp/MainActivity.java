package com.example.myalarmapp;

import static com.example.myalarmapp.R.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private AlarmManager alarmManager;
    private TextView textView;
    private MaterialTimePicker picker; // Use the class variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.alarmtext);

        Button addAlarm = findViewById(R.id.add_alarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });

        Button setAlarm = findViewById(R.id.set_alarm);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        Button cancelAlarm = findViewById(R.id.cancel_alarm);
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    private void cancelAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        } else {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void addAlarm() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build();

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = picker.getHour();
                int minute = picker.getMinute();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String formattedTime = dateFormat.format(calendar.getTime());

                textView.setText(formattedTime);
            }
        });

        picker.show(getSupportFragmentManager(), "alarm");
    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (picker != null) {
            int hour = picker.getHour();
            int minute = picker.getMinute();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(
                    this,
                    "Alarm set for " + hour + ":" + minute,
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(this, "Picker is not initialized", Toast.LENGTH_SHORT).show();
        }
    }
}






