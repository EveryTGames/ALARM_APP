package com.etgames.alarm;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;


    int REQUEST_CODE = 59;
    private static final int JOB_ID = 1;
    public static final String CHANNEL_ID = "myAlarm";



    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionState = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS);
        // If the permission is not granted, request it.
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }




//        //ask for permission for exac alarm
//        Intent intent = new Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//        startActivityForResult(intent, REQUEST_CODE);


        //alarm pending intent






        Log.d("infoo"," " + isNotificationListenerEnabled(this));

       // NotificationManagerCompat.from(MainActivity.this).requestNotificationChannelPermission("*");

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);




//        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//            @SuppressLint("ScheduleExactAlarm")
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay,
//                                  int minute) {
//                // Handle the selected
//
//                long alarmTime = System.currentTimeMillis() + (long) Calendar.MINUTE * minute + (long) Calendar.HOUR_OF_DAY * hourOfDay;
//                Log.d("infoo","alarmTime" +  alarmTime);
//                // ... set the alarm using alarmTime
//
//                // ... set the button's click listener to show the time picker dialog
////                JobScheduler jobScheduler = getSystemService(JobScheduler.class);
////                JobInfo jobInfo = new JobInfo.Builder(JOB_ID, new ComponentName(MainActivity.this, AlarmService.class))
////                        .setPeriodic(6000)
////                        .build();
////                //jobScheduler.schedule(jobInfo);
////                int resultCode = jobScheduler.schedule(jobInfo);
////                if (resultCode == JobScheduler.RESULT_SUCCESS) {
////                    Log.d("infoo", "Job scheduled successfully");
////                } else {
////                    Log.e("infoo", "Failed to schedule job: " + resultCode);
////                }
////                Toast.makeText(MainActivity.this,"service started", Toast.LENGTH_SHORT).show();
////                Log.d("infoo","it started");
//
//            }
//        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);









    }







    //Helper method to show a dialog window
    private void showMessageForFloatingPermission(String message) {
        new android.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        checkFloatingPermission();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //User opted not to use this feature
                        //finish();

                    }
                })
                .create()
                .show();
    }





    //Helper method for checking over lay floating permission
    public void checkFloatingPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityFloatingPermission.launch(intent);//this will open device settings for over lay permission window

        }
    }





    //Initialize ActivityResultLauncher. Note here that no need custom request code

    ActivityResultLauncher<Intent> startActivityFloatingPermission = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //Permission granted
                    }else{
                        //If there is no permission allowed yet, still a dialog window will open unless a user opted not to use the feature.
                        if (!Settings.canDrawOverlays(MainActivity.this)) {
                            // You don't have permission yet, show a dialog reasoning
                            showMessageForFloatingPermission("To use this feature requires over lay permission");

                        }
                    }
                }
            });











    private boolean isNotificationListenerEnabled(Context context) {
        ComponentName cn = new ComponentName(context, AppNotificationListenerService.class);
        String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        return flat != null && flat.contains(cn.flattenToString());
    }


    public void PermissionService(View v)
    {

        if (!isNotificationListenerEnabled(this)) {
            // Request permission
            AppNotificationListenerService.requestNotificationListenerAccess(this);
        }

        int permissionState = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS);
        // If the permission is not granted, request it.
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }


        if ("xiaomi".equalsIgnoreCase(Build.MANUFACTURER) && !Settings.canDrawOverlays(this)) {
            try {
                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", getPackageName());
                startActivity(intent);
            } catch (Exception e) {
                // Fallback if the above intent fails
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(this, "Unable to open permissions settings", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }





    }
    public void StopService(View v)
    {
        // Remove the permanent notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(55);
        }
        // Close the app completely
        finishAffinity(); // Close all activities
        System.exit(0);   // Force the app process to stop
    }

    @Override
    protected void onRestart() {
        Log.d("infoo","restart have been called from main activity, ?");

        super.onRestart();
    }


    public boolean stop() {

//        packageManager = getPackageManager();
//        componentName =  new ComponentName(this, AppNotificationListenerService.class);
//        // Disable the component:
//        packageManager.setComponentEnabledSetting(componentName,
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);

//        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.createNotificationChannel(channel);
//        NotificationChannel chaanneeel =   manager.getNotificationChannel(CHANNEL_ID);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("the app is closed")
                .setContentText("the service stopped, will no longer be able to listen to messages")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(false);
        Log.d("infoo","ummmmm");

        // delete the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(55,builder.build());

        Log.d("infoo","stop function have been called from main activity, service stopped?");
        return true;

    }
    PackageManager packageManager;
    ComponentName componentName ;
    @Override
    protected void onStart() {
        Log.d("infoo", "on start have been called from main activity, service stopped?");
        packageManager = getPackageManager();
        componentName =  new ComponentName(this, AppNotificationListenerService.class);

        // Disable the component:
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
// Enable the component:
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        super.onStart();

    }
    @Override
    public void onDestroy()
    {
        Log.d("infoo","destroy have been called from main activity, service stopped?");



       Log.d("infoo","the sstop function returned with  "+ stop());

        Log.d("infoo","hmmmmmmmm");


        super.onDestroy();

    }


}