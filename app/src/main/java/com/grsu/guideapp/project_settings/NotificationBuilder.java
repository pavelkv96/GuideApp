package com.grsu.guideapp.project_settings;

import static com.grsu.guideapp.project_settings.Constants.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.widget.RemoteViews;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.fragments.details.DetailsPlayerService;

public class NotificationBuilder {

    private static RemoteViews remoteViews = null;
    private static RemoteViews smallViews = null;
    private static NotificationCompat.Builder notification = null;

    public static RemoteViews getBigView(Context ctx) {
        if (remoteViews == null) {
            remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.big_notification);
        }
        return remoteViews;
    }

    public static RemoteViews getSmallView(Context ctx) {
        if (smallViews == null) {
            smallViews = new RemoteViews(ctx.getPackageName(), R.layout.status_bar);
        }
        return smallViews;
    }

    @SuppressLint("RestrictedApi")
    public static Notification createNotification(Context ctx, RemoteViews big, RemoteViews small) {
        if (notification == null) {
            Intent i = new Intent(ctx, RouteActivity.class);
            PendingIntent pendingIntent =PendingIntent.getActivity(ctx, 0, i, 0);
            notification = new NotificationCompat.Builder(ctx, CHANNEL_ID);
            notification.setContentIntent(pendingIntent);
            notification.setSmallIcon(R.drawable.ic_action_play);
            notification.setCustomBigContentView(big);
            notification.setCustomContentView(small);
            notification.setContentTitle("Music Player");
            notification.setContentText("Control Audio");
            notification.getContentView().setTextViewText(R.id.status_bar_track_name, "Adele");
            notification.getBigContentView().setTextViewText(R.id.textSongName, "Adele");
            setListeners(ctx, big, small);
        }

        return notification.build();
    }

    private static void setListeners(Context context, RemoteViews big, RemoteViews small) {

        Class<DetailsPlayerService> aClass = DetailsPlayerService.class;
        Intent delete = new Intent(context, aClass).setAction(Constants.NOTIFY_DELETE);
        Intent next = new Intent(context, aClass).setAction(Constants.NOTIFY_NEXT);
        Intent pause = new Intent(context, aClass).setAction(Constants.NOTIFY_PAUSE);
        Intent play = new Intent(context, aClass).setAction(Constants.NOTIFY_PLAY);
        Intent previous = new Intent(context, aClass).setAction(Constants.NOTIFY_PREVIOUS);

        PendingIntent pDelete = PendingIntent.getService(context, 0, delete, 0);
        big.setOnClickPendingIntent(R.id.btnDelete, pDelete);
        small.setOnClickPendingIntent(R.id.status_bar_delete, pDelete);

        PendingIntent pNext = PendingIntent.getService(context, 0, next, 0);
        big.setOnClickPendingIntent(R.id.btnNext, pNext);
        small.setOnClickPendingIntent(R.id.status_bar_next, pNext);

        PendingIntent pPause = PendingIntent.getService(context, 0, pause, 0);
        big.setOnClickPendingIntent(R.id.btnPause, pPause);
        small.setOnClickPendingIntent(R.id.status_bar_pause, pPause);

        PendingIntent pPlay = PendingIntent.getService(context, 0, play, 0);
        big.setOnClickPendingIntent(R.id.btnPlay, pPlay);
        small.setOnClickPendingIntent(R.id.status_bar_play, pPlay);

        PendingIntent pPrevious = PendingIntent.getService(context, 0, previous, 0);
        big.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);
    }

    public static void playUpdate(Context ctx) {
        RemoteViews playBig = NotificationBuilder.getBigView(ctx);
        RemoteViews playSmall = NotificationBuilder.getSmallView(ctx);

        playBig.setViewVisibility(R.id.btnPlay, View.GONE);
        playSmall.setViewVisibility(R.id.status_bar_play, View.GONE);
        playBig.setViewVisibility(R.id.btnPause, View.VISIBLE);
        playSmall.setViewVisibility(R.id.status_bar_pause, View.VISIBLE);
        NotificationBuilder.update(ctx, notification);
    }

    public static void pauseUpdate(Context ctx) {
        RemoteViews pauseBig = NotificationBuilder.getBigView(ctx);
        RemoteViews pauseSmall = NotificationBuilder.getSmallView(ctx);

        pauseBig.setViewVisibility(R.id.btnPlay, View.VISIBLE);
        pauseSmall.setViewVisibility(R.id.status_bar_play, View.VISIBLE);
        pauseBig.setViewVisibility(R.id.btnPause, View.GONE);
        pauseSmall.setViewVisibility(R.id.status_bar_pause, View.GONE);
        NotificationBuilder.update(ctx, notification);
    }

    public static void deleteUpdate(Context ctx) {
        NotificationBuilder.playUpdate(ctx);
        notification = null;
    }

    private static void update(Context ctx, Builder notificationBuilder) {
        Object systemService = ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager manager = (NotificationManager) systemService;
        if (manager != null) {
            manager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    @SuppressLint("RestrictedApi")
    public static void nameUpdate(Context ctx, String name) {
        notification.getContentView().setTextViewText(R.id.status_bar_track_name, name);
        notification.getBigContentView().setTextViewText(R.id.textSongName, name);
        update(ctx, notification);
    }
}
