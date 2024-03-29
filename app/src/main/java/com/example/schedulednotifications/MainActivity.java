package com.example.schedulednotifications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    @SuppressLint("ScheduleExactAlarm")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission();
        }


        findViewById(R.id.button).setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);


            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            long timeAtButtonClicked = System.currentTimeMillis();
            long time = 1000 * 10;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClicked + time, pendingIntent);
                    Toast.makeText(MainActivity.this, "Scheduled", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Match Reminder";
            String description = "Notification for match linup out";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("matchReminders", name, importance);
            notificationChannel.setDescription(description);


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);


        }

    }


    // Method to check if the permission is granted
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.USE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED;
    }

    // Method to request the permission
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_EXACT_ALARM}, PERMISSION_REQUEST_CODE);
    }

    // Method to handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // You can proceed with scheduling exact alarms or notifications here

            } else {
                // Permission denied
                // Handle the denial gracefully
                Toast.makeText(MainActivity.this, "Permission Denied for Notification", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Call this method when you need to check or request the permission
    private void checkOrRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!isPermissionGranted()) {
                requestPermission();
            } else {
                // Permission already granted
                // You can proceed with scheduling exact alarms or notifications here
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }
        }
    }
}