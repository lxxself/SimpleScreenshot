package com.lxxself.jietu.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;

import com.lxxself.jietu.activity.DealActivity;
import com.lxxself.jietu.listener.ShakeListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lxxself.jietu.util.ScreenShot.takeScreenShotNeedRoot;

public class ShakeService extends Service {
    private ShakeListener shakeListener;
    private Context mContext;
    public ShakeService() {
    }

    @Override
    public void onCreate() {
        mContext = this;
        shakeListener = new ShakeListener(this);//创建一个对象
        shakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {//调用setOnShakeListener方法进行监听

            public void onShake() {
                //对手机摇晃后的处理（如换歌曲，换图片，震动……）
                onVibrator();
                //截图功能
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
                        String time = simple.format(new Date());
                        String path = takeScreenShotNeedRoot(time);
                        Intent intent = new Intent(ShakeService.this, DealActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("path", path);
                        startActivity(intent);
                    }
                }, 500);
            }

        });
    }

    private void onVibrator() {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null) {
            Vibrator localVibrator = (Vibrator) mContext.getApplicationContext()
                    .getSystemService(Context.VIBRATOR_SERVICE);
            vibrator = localVibrator;
        }
        vibrator.vibrate(100L);
    }


    @Override
    public void onDestroy() {
        shakeListener.stop();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
