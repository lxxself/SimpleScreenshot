package com.lxxself.jietu.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.lxxself.jietu.R;
import com.lxxself.jietu.activity.DealActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lxxself.jietu.util.ScreenShot.takeScreenShotNeedRoot;
import static com.lxxself.jietu.util.StatusBarAction.collapseStatusBar;

public class FrontService extends Service {
    private static final String INTENT_NAME = "btnId";
    public static final int INTENT_BTN = 1;
    private static final String ACTION_BTN = "com.lxxself.jietu.btn";
    private Notification notification;

    private Handler fileHandler;

    NotificationBroadcastReceiver mReceiver;
    @Override
    public boolean stopService(Intent name) {
        stopForeground(true);
        return super.stopService(name);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        unregeisterReceiver();
        intiReceiver();
        //自定义notification的view
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        contentView.setImageViewResource(R.id.nImage, R.drawable.iconmonstr_crop_6_icon_256);
        contentView.setTextViewText(R.id.nTextview, "点击后开始截图");

        Intent intent = new Intent(ACTION_BTN);
        intent.putExtra(INTENT_NAME, INTENT_BTN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.notification_layout, pendingIntent);

//        Intent intent2 = new Intent();
//        intent2.setClass(this, MainActivity.class);
//        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent intentContent = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new Notification(R.drawable.iconmonstr_crop_6_icon_256, "开启通知栏消息", System.currentTimeMillis());
        notification.contentView = contentView;
//        notification.contentIntent = intentContent;
        startForeground(1, notification);


    }


    @Override
    public void onStart(Intent intent, int startId) {


    }

    private void intiReceiver() {
        mReceiver = new NotificationBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BTN);
        getApplicationContext().registerReceiver(mReceiver, intentFilter);
    }

    private void unregeisterReceiver() {
        if (mReceiver != null) {
            getApplicationContext().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Myservice","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        unregeisterReceiver();
    }

    public FrontService() {
    }

    private ScreenShotBinder mbind = new ScreenShotBinder();

    class ScreenShotBinder extends Binder {
        public void startScreenShot() {
            Log.d("FrontService","startScreenShot");
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mbind;
    }

    private class NotificationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_BTN)) {
                int btn_id = intent.getIntExtra(INTENT_NAME, 0);
                switch (btn_id) {
                    case INTENT_BTN:
                        collapseStatusBar(FrontService.this);
                        Bitmap bitmap;
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
                                String time = simple.format(new Date());
                                String path  = takeScreenShotNeedRoot(time);

                                Intent intent=new Intent(FrontService.this, DealActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("path", path);
                                startActivity(intent);
                            }
                        }, 500);
                        break;

                }
            }
        }
    }


}
