package com.lxxself.jietu.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Administrator on 2015/6/10.
 */
public class ScreenShot {
    public static String takeScreenShotNoRoot(View view,String time) throws Exception {
        String filePath = null;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Canvas canvas = new Canvas(bitmap);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
//        canvas.drawText(time , w / 10 , h - 16 , paint) ;
        //canvas.save();
        //canvas.restore();
        String path = "/sdcard/Jietu/"+time+"NoRoot.png";
        FileOutputStream fos = null;
        File file = new File(path);
        fos = new FileOutputStream(file);
        if (fos != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Log.i("tag", "success");
        }
        bitmap = null;
        return path;
    }

    public static String takeScreenShotNeedRoot(String shotTime) {
        String time = shotTime;
        String path = "/sdcard/Jietu/"+time+"NeedRoot.png";
        try {
            Process sh = null;
            sh = Runtime.getRuntime().exec("su", null, null);
            OutputStream os = sh.getOutputStream();
            os.write(("/system/bin/screencap -p " + path).getBytes("ASCII"));
            os.flush();
            os.close();
            sh.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("path", path);
        return path;
    }
}
