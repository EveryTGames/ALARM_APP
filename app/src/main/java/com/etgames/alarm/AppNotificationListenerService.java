package com.etgames.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Objects;

public class AppNotificationListenerService extends android.service.notification.NotificationListenerService {

    private static final String LOGGER_TAG = "infoo";




    private static final String CHANNEL_ID = "ALARM_CHANNEL";
    private static final int NOTIFICATION_ID = 123;
    private NotificationManager notificationManager;
    private Handler handler = new Handler();
    private boolean alarmActive = false;

    public void AlarmNotifier(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Alarm Notifications",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
    }

    private Notification buildNotification(Context context) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Alarm Ringing!")
                .setContentText("Tap to dismiss")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
    }

    public void startRepeatingNotifications(Context context) {
        if (alarmActive) return; // Avoid multiple triggers
        alarmActive = true;

        Runnable sendNotification = new Runnable() {
            @Override
            public void run() {
                if (!alarmActive) return;
                notificationManager.notify(NOTIFICATION_ID, buildNotification(context));
                handler.postDelayed(this, 2000); // Sends notification every 2 seconds
            }
        };

        handler.post(sendNotification);
    }

    public void stopNotifications() {
        alarmActive = false;
        notificationManager.cancel(NOTIFICATION_ID);
    }









    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Handle notification events here
       // Log.d(LOGGER_TAG, "Notification posted: " + sbn.getNotification().toString());
        AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);
        final Calendar myCalender = Calendar.getInstance();
        Intent intent = new Intent(this,AlarmReceiver.class);
        intent.setAction("com.etgames.trigerAlarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            if (Objects.equals(sbn.getPackageName(), "com.whatsapp")) {
                // Notification from WhatsApp received

                // Extract message content (adjust key as needed)
                Bundle extras = sbn.getNotification().extras;
                if (extras != null) {
                   // Log.d("infoo","extras not null");

                    String message = extras.getString("android.text");
                    if (message != null && message.contains("//alarmnow")) {


                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("My Notification")
                                .setContentText("This is a notification from your app, the command received.")
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setAutoCancel(true);
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(652, builder.build());



                        // Process the message
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pendingIntent);


                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pendingIntent);


                        Log.d(LOGGER_TAG, "WhatsApp message using extras: " + message);
                    }
                }
               // Log.d("infoo", "whatsapp messages using this thing :  " +sbn.getNotification().contentIntent);
            }
            else if(Objects.equals(sbn.getPackageName(), "com.discord") || Objects.equals(sbn.getPackageName(), "com.facebook.orca"))
            {
                // Notification from Discord received

                // Extract message content (adjust key as needed)
                Bundle extras = sbn.getNotification().extras;
                if (extras != null) {
                    // Log.d("infoo","extras not null");

                    String message = extras.getString("android.text");
                    String SenderName = extras.getString("android.title");
                    if (message != null && SenderName != null && (message.contains("//alarmnow")  || Objects.equals(sbn.getPackageName(), "com.facebook.orca") )) {


                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("My Notification")
                                .setContentText("This is a notification from your app, the command received.")
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setAutoCancel(true);
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(652, builder.build());


                        // Process the message

                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pendingIntent);



                        startRepeatingNotifications(this);


                        Log.d(LOGGER_TAG, "Discord message using extras: " + message);


                    }
                        Log.d(LOGGER_TAG, "Discord message sender name using extras: " + SenderName);

                }
            }
            else if(Objects.equals(sbn.getPackageName(), "com.zhiliaoapp.musically"))
            {
                // Notification from tiktok received

                // Extract message content (adjust key as needed)
                Bundle extras = sbn.getNotification().extras;
                if (extras != null) {
                    // Log.d("infoo","extras not null");

                    String message = extras.getString("android.text");
                    if (message != null && message.contains("//alarmnow")) {


                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("My Notification")
                                .setContentText("This is a notification from your app, the command received.")
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setAutoCancel(true);
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(652, builder.build());


                        // Process the message

                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pendingIntent);


                        Log.d(LOGGER_TAG, "Tiktok message using extras: " + message);
                    }
                }
            }


    }



    public static void requestNotificationListenerAccess(Context context) {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        context.startActivity(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("infoo","on start command called, service started?");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("infoo","unbind have been called, service stopped?");

        return super.onUnbind(intent);
    }
    @Override
    public IBinder onBind(Intent intent)
    {


       Log.d("infoo","onbind called, service started?");

       //this part is for the repeat notification for the xiami smart watch
        AlarmNotifier(this);








        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("the app is Running")
                .setContentText("the service started and listening for notifications,plz dont close the app")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(55, builder.build());

       return super.onBind(intent);
    }
    @Override
    public void onDestroy()
    {
        Log.d("infoo","destroy have been called, service stopped?");
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("infoo","taskRemoved have been called, service stopped?");

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("infoo","rebind have been called, service stopped?");

        super.onRebind(intent);
    }

}