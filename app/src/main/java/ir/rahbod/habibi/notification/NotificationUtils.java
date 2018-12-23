package ir.rahbod.habibi.notification;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ir.rahbod.habibi.R;


public class NotificationUtils {

    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(NotificationData notificationData, Intent intent) {
        showNotificationMessage(notificationData, intent, null);
    }

    public void showNotificationMessage(NotificationData notificationData, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(notificationData.getDescription()))
            return;

        // notification icon
        final int icon = R.drawable.logo;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, Config.MEY_ChannelId);
        mBuilder.setChannelId(Config.MEY_ChannelId);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, notificationData, resultPendingIntent, alarmSound);
                } else {
                    showSmallNotification(mBuilder, icon, notificationData, resultPendingIntent, alarmSound);
                }
            }
        } else {
            showSmallNotification(mBuilder, icon, notificationData, resultPendingIntent, alarmSound);
//            playNotificationSound();
        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, NotificationData notificationData, PendingIntent resultPendingIntent, Uri alarmSound) {


        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Config.MEY_ChannelId, Config.MEY_ChannelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);

        }

        Notification notification;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            notification = mBuilder.setSmallIcon(icon).setTicker(notificationData.getTitle()).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(notificationData.getTitle())
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationData.getDescription()))
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(notificationData.getDescription())
                    .setChannelId(Config.MEY_ChannelId)
                    .build();

        }else{
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.addLine(notificationData.getDescription());

            notification = mBuilder.setSmallIcon(icon).setTicker(notificationData.getTitle()).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(notificationData.getTitle())
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(inboxStyle)
//                .setWhen(getTimeMilliSec(timeStamp))
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(notificationData.getDescription())
                    .setChannelId(Config.MEY_ChannelId)
                    .build();

        }

        notificationManager.notify(Config.NOTIFICATION_ID, notification);
    }

    private void showBigNotification(
            Bitmap bitmap,
            NotificationCompat.Builder mBuilder,
            NotificationData notificationData,
//            String timeStamp,
            PendingIntent resultPendingIntent
            , Uri alarmSound) {

//        Log.d(TAG, " Location 137 : NotificationUtils.java" );

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(notificationData.getTitle());
        bigPictureStyle.setSummaryText(Html.fromHtml(notificationData.getDescription()).toString());
        bigPictureStyle.bigPicture(bitmap);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Config.MEY_ChannelId, Config.MEY_ChannelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);

        }

        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.logo).setTicker(notificationData.getTitle()).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(notificationData.getTitle())
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
//                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo))
                .setContentText(notificationData.getDescription())
                .build();

        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
//            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                    + "://" + mContext.getPackageName() + "/raw/notification");
//            Log.d(TAG, " Location 193 : NotificationUtils.java" );
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
//            Log.d(TAG, " Location 200 : NotificationUtils.java" );
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
//        Log.d(TAG, " Location 209 : NotificationUtils.java" );
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

//    public static long getTimeMilliSec(String timeStamp) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date date = format.parse(timeStamp);
//            return date.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
}
