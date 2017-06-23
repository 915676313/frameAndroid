package com.arlen.frame.common.operation;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.arlen.frame.R;
import com.arlen.frame.common.AppContext;

import java.util.HashMap;
import java.util.Map;

/**
 *  Created by Arlen on 2017/6/18 17:05
 */
@SuppressLint("NewApi")
public class UpdateService extends Service {
    private String url;
    private NotificationManager manager;
    private RemoteViews view;
    private Map<String, Notification> notificationMap = new HashMap<String, Notification>();
    private int mStartId;
    private Notification noti;
    private ProgressHelper progressHelper;
    private AppContext appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = (AppContext) getApplication();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (manager != null)
//            manager.cancel(R.mipmap.ic_launcher);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        this.mStartId = startId;
        url = intent.getStringExtra("url");
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        view = new RemoteViews(getPackageName(),
                R.layout.notification_download);
        manager.notify(R.mipmap.ic_launcher, getNotifation(0, "正在下载..."));
        manager.cancel(R.mipmap.ic_launcher);
        progressHelper = new ProgressHelper();
        progressHelper.retrofitDownload(url);
        progressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long progress, long total, boolean done) {
                if ((progress * 100 / total) % 10 == 0) {
                    manager.notify(R.mipmap.ic_launcher, getNotifation((int) (progress * 100 / total),
                            (int) (progress * 100 / total) + "%"));
                }
            }

            @Override
            protected void downFailure() {
//                manager.notify(R.mipmap.ic_launcher, getNotifation(0, "出错"));
            }

            @Override
            protected void downComplete() {
                installApk();
                progressHelper.cancelDown();
                stopSelf(mStartId);
            }
        });
        return START_NOT_STICKY;
    }

    public Notification getNotifation(int progress, String percent) {
        if (Build.VERSION.SDK_INT >= 14) {
            noti = new NotificationCompat.Builder(appContext)
                    .setContentTitle(
                            getResources().getString(R.string.app_name))
                    .setProgress(100, progress, false)
                    .setContentText(percent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(false)
                    .setShowWhen(true)
                    .setSmallIcon(R.mipmap.ic_launcher).getNotification();
            return noti;
        } else {
            if (notificationMap.get("notifation") == null) {
                noti = new Notification();
                noti.contentView = view;
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                noti.icon = R.mipmap.ic_launcher;
            }
            return notificationMap.get("notifation");
        }
    }

    private void installApk() {
        // 核心是下面几句代码
        if (progressHelper.getApkFile() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(progressHelper.getApkFile()),
                    "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0x888,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = getNotifation(100, "点击安装");
            notification.contentIntent = pendingIntent;
//            notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL;
            manager.notify(R.mipmap.ic_launcher, notification);
            startActivity(intent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
