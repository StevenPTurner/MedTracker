package com.medtracker.TestingClasses;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.medtracker.Utilities.NotificationReceiver;
import com.medtracker.Utilities.Utility;
import com.medtracker.medtracker.R;

import java.util.Calendar;

//activitiy is used as a place to test tutorials and new ideas
//http://www.vogella.com/tutorials/AndroidNotifications/article.html
//https://gist.github.com/BrandonSmith/6679223
public class TestActivity extends Activity{

    Button fiveSecondsButton;
    Button tenSecondsButton;
    Button fifthteenSecondsButton;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        fiveSecondsButton = (Button) findViewById(R.id.button_5_seconds);
        tenSecondsButton = (Button) findViewById(R.id.button_10_seconds);
        fifthteenSecondsButton = (Button) findViewById(R.id.button_15_seconds);

//        fiveSecondsButton.setOnClickListener(this);
//        tenSecondsButton.setOnClickListener(this);
//        fifthteenSecondsButton.setOnClickListener(this);
    }
//
//    private void scheduleNotification(Notification notification, int id) {
//        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
//        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, id);
//        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        //long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
//        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//    }
//
//
//
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.button_5_seconds:
//                calendar = Calendar.getInstance();
//                calendar.set(Calendar.SECOND, (calendar.get(Calendar.SECOND) + 5));
//                scheduleNotification(Utility.getNotification("5 second delay", this),1);
//                break;
//            case R.id.button_10_seconds:
//                calendar = Calendar.getInstance();
//                calendar.set(Calendar.SECOND, (calendar.get(Calendar.SECOND) + 10));
//                scheduleNotification(Utility.getNotification("10 second delay", this),2);
//                break;
//            case R.id.button_15_seconds:
//                calendar = Calendar.getInstance();
//                calendar.set(Calendar.SECOND, (calendar.get(Calendar.SECOND) + 15));
//                scheduleNotification(Utility.getNotification("15 second delay", this),3);
//                break;
//        }
//    }

}


