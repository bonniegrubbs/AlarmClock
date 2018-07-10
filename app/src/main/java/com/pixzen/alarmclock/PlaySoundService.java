package com.pixzen.alarmclock;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class PlaySoundService extends Service {

    MediaPlayer mediaSound;
    int startId;
    boolean isPlaying;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // required method
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // gets the extra string values
        String state = intent.getExtras().getString("extra");

        // converts extra strings to startIds (0 or 1)
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }


        if (!this.isPlaying && startId == 1) {
            // if no music and user pressed "alarm on", play music
            // soundPool helps to preload sound and prevent sound lag
            SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
            int soundID = soundPool.load(this, R.raw.meadowlark, 1);
            soundPool.play(soundID, 1, 1, 0, 0, 1);

            mediaSound = MediaPlayer.create(this, R.raw.meadowlark);
            mediaSound.setLooping(true);
            mediaSound.setVolume(20,20);
            mediaSound.start();

            // make phone vibrate for specified time
            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(getApplication().VIBRATOR_SERVICE);
            vibrator.vibrate(8000);

            this.isPlaying = true;
            this.startId = 0;

        } else if (this.isPlaying && startId == 0) {
            // if music is playing and user pressed "alarm off", stop playing
            mediaSound.stop();
            mediaSound.reset();

            this.isPlaying = false;
            this.startId = 0;

        } else if (!this.isPlaying && startId == 0) {
            // for random button pushers
            this.isPlaying = false;
            this.startId = 0;

        } else if (this.isPlaying && startId == 1) {
            // for random button pushers
            this.isPlaying = true;
            this.startId = 1;

        } else {
            // if all else fails....
            // do nothing
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        // Tell the user that alarm stopped
        Toast.makeText(this, "alarm stopped", Toast.LENGTH_SHORT).show();
    }


}
