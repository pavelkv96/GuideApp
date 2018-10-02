package com.grsu.guideapp.fragments.details;

import static com.grsu.guideapp.project_settings.NotificationBuilder.createNotification;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
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
        Notification notification = createNotification(this, NotificationBuilder.getBigView(this),
                NotificationBuilder.getSmallView(this));
        startForeground(Constants.NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            Logs.e(TAG, "onStartCommand:" + intent.getAction());
            switch (intent.getAction()) {
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
                    String stringExtra = intent.getStringExtra(Constants.KEY_RECORD);

                    player = DetailsPlayerService.create(this, R.raw.music);
                    player.setOnPreparedListener(this);
                    player.setOnCompletionListener(this);
                    Logs.e(TAG, "onStartCommand: " + stringExtra);
                    NotificationBuilder
                            .nameUpdate(this, stringExtra);
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
        Toast.makeText(this, "FINISHED ", Toast.LENGTH_LONG).show();
        player.pause();
        player.seekTo(0);
        NotificationBuilder.pauseUpdate(this);
    }

    @Deprecated
    @NonNull
    private static MediaPlayer create(Context context, int resId) {
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
            if (afd == null) {
                return null;
            }
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepareAsync();
            return mp;
        } catch (IOException ex) {
            Log.d(TAG, "create failed:", ex);
            // fall through
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "create failed:", ex);
            // fall through
        } catch (SecurityException ex) {
            Log.d(TAG, "create failed:", ex);
            // fall through
        }
        return null;
    }

    @NonNull
    private static MediaPlayer create(File pathToRecord) {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(pathToRecord.getAbsolutePath());
        } catch (IOException e) {
            Logs.e(TAG, "create: " + e.getMessage(), e);
        }
        mp.prepareAsync();
        return mp;
    }

    private void delete() {
        Toast.makeText(this, "NOTIFY_DELETE", Toast.LENGTH_SHORT).show();
        NotificationBuilder.deleteUpdate(this);
        release();
        stopForeground(true);
        stopSelf();
    }

    private void previous() {
        player.seekTo(player.getCurrentPosition() - 10000);
        Toast.makeText(this, "NOTIFY_PREVIOUS " + player.getCurrentPosition(),
                Toast.LENGTH_SHORT).show();
    }

    private void next() {
        player.seekTo(player.getCurrentPosition() + 10000);
        Logs.e(TAG, "NOTIFY_NEXT " + player.getCurrentPosition());
    }

    private void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
        Toast.makeText(this, "NOTIFY_PAUSE", Toast.LENGTH_SHORT).show();
        NotificationBuilder.pauseUpdate(this);
    }

    private void play() {
        player.start();
        Toast.makeText(this, "NOTIFY_PLAY", Toast.LENGTH_SHORT).show();
        NotificationBuilder.playUpdate(this);
    }
}
