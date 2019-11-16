package com.grsu.guideapp.activities.details;

import static com.grsu.guideapp.project_settings.NotificationBuilder.createNotification;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.RemoteViews;
import com.grsu.guideapp.R;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.project_settings.NotificationBuilder;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.File;
import java.io.IOException;

public class DetailsPlayerService extends Service implements OnPreparedListener,
        OnCompletionListener {

    private static final String TAG = DetailsPlayerService.class.getSimpleName();

    MediaPlayer player = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Logs.e(TAG, "onCreate:");
        RemoteViews bigView = NotificationBuilder.getBigView(this);
        RemoteViews smallView = NotificationBuilder.getSmallView(this);
        Notification notification = createNotification(this, bigView, smallView);
        startForeground(Constants.NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            Logs.e(TAG, "onStartCommand:" + action);
            switch (action) {
                case Constants.NOTIFY_PLAY:
                    play();
                    break;
                case Constants.NOTIFY_PAUSE:
                    pause();
                    break;
                case Constants.NOTIFY_NEXT:
                    next();
                    break;
                case Constants.NOTIFY_DELETE:
                    delete();
                    break;
                case Constants.NOTIFY_PREVIOUS:
                    previous();
                    break;
                case Constants.KEY_RECORD:
                    createPlayer(intent);
                    break;
            }
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logs.e(TAG, "onBind: " + intent.getAction());
        return null;
    }

    private void release() {
        if (player != null) {
            player.release();
        }
    }

    @Override
    public void onDestroy() {
        Logs.e(TAG, "onDestroy:");
        release();
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //Toast.makeText(this, "FINISHED ", Toast.LENGTH_LONG).show();
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            }
            player.seekTo(0);
            NotificationBuilder.pauseUpdate(this);
        }
    }

    @NonNull
    private MediaPlayer create(File pathToRecord) {
        MediaPlayer mp = new MediaPlayer();
        try {
            String RES_PREFIX = "android.resource://com.grsu.guideapp/";
            mp.setDataSource(getApplicationContext(), Uri.parse(RES_PREFIX + R.raw.audio));
            //mp.setDataSource(pathToRecord.getAbsolutePath());
            mp.prepareAsync();
        } catch (IOException e) {
            Logs.e(TAG, "create: " + e.getMessage(), e);
        }
        return mp;
    }

    private void delete() {
        NotificationBuilder.deleteUpdate(this);
        release();
        stopForeground(true);
        stopSelf();
    }

    private void previous() {
        player.seekTo(player.getCurrentPosition() - 10000);
        String text = "NOTIFY_PREVIOUS " + player.getCurrentPosition();
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void next() {
        player.seekTo(player.getCurrentPosition() + 10000);
        String text = "NOTIFY_NEXT " + player.getCurrentPosition();
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }

        //Toast.makeText(this, "NOTIFY_PAUSE", Toast.LENGTH_SHORT).show();
        NotificationBuilder.pauseUpdate(this);
    }

    private void play() {
        player.start();
        //Toast.makeText(this, "NOTIFY_PLAY", Toast.LENGTH_SHORT).show();
        NotificationBuilder.playUpdate(this);
    }

    private void createPlayer(Intent intent) {
        String namePlace = intent.getStringExtra(Constants.KEY_NAME_PLACE_RECORD);
        File pathToAudio = new File("");
        if (pathToAudio != null/* && pathToAudio.exists()*/) {
            Logs.e(TAG, "onStartCommand: " + pathToAudio);

            player = create(pathToAudio);
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
            NotificationBuilder.nameUpdate(this, namePlace);
        } else {
            delete();
        }
    }
}
