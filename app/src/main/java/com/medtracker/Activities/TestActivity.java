package com.medtracker.Activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.medtracker.TestReceiver;
import com.medtracker.medtracker.R;

//http://www.vogella.com/tutorials/AndroidNotifications/article.html
//https://gist.github.com/BrandonSmith/6679223
public class TestActivity extends Activity implements View.OnClickListener {

    Button fiveSecondsButton;
    Button tenSecondsButton;
    Button fifthteenSecondsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        fiveSecondsButton = (Button) findViewById(R.id.button_5_seconds);
        tenSecondsButton = (Button) findViewById(R.id.button_10_seconds);
        fifthteenSecondsButton = (Button) findViewById(R.id.button_15_seconds);

        fiveSecondsButton.setOnClickListener(this);
        tenSecondsButton.setOnClickListener(this);
        fifthteenSecondsButton.setOnClickListener(this);
    }

    private void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(this, TestReceiver.class);
        notificationIntent.putExtra(TestReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(TestReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_pill_black_48dp);
        return builder.build();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_5_seconds:
                scheduleNotification(getNotification("5 second delay"), 5000);
                break;
            case R.id.button_10_seconds:
                scheduleNotification(getNotification("10 second delay"), 10000);
                break;
            case R.id.button_15_seconds:
                scheduleNotification(getNotification("15 second delay"), 15000);
                break;
        }
    }

    public void androidGuide (View view) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_pill_black_48dp)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, HomeActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }
}


