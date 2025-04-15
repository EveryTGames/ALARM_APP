package com.etgames.alarm;


import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {



    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShowWhenLocked(true);
        setTurnScreenOn(true);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardManager.requestDismissKeyguard(this, null);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();

        try {
            // Set the audio stream to ALARM
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);

            // Set the data source to the default alarm sound
            mediaPlayer.setDataSource(this, android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI);

            mediaPlayer.setLooping(true); // Set to loop the sound
            mediaPlayer.prepare();       // Prepare the MediaPlayer
            mediaPlayer.start();         // Start playing
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.d("infoo","the activity srtarted");
        }


        public void Silence(View e)
        {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }

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
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
