package com.lxxself.jietu.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.lxxself.jietu.activity.DealActivity;
import com.lxxself.jietu.listener.MultiFileObserver;

public class FileListenerService extends Service {

    private FileListener multiFileObserver = null;

    private Handler loginHandler;
    private String currentPath = "";


    public FileListenerService() {
    }


    @Override
    public void onCreate() {
        multiFileObserver = new FileListener(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Screenshots");
        multiFileObserver.startWatching();


    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    public class FileListener extends MultiFileObserver {

        public FileListener(String path) {
            super(path);
        }

        @Override
        public void onEvent(int event, final String path) {
//            super.onEvent(event,path);
            //TODO 确切的截图目录微解决
            if (event == CREATE) {
                Log.i("MultiFileObserver", "CREATE: " + path);
                currentPath = path;
            }
//            if (event == MODIFY) {
//                Log.i("MultiFileObserver", "MODIFY: " + path);
//            }
//            if (event == CLOSE_NOWRITE) {
//                Log.i("MultiFileObserver", "CLOSE_NOWRITE: " + path);
//            }
            if (event == CLOSE_WRITE && currentPath.equals(path)) {
                Log.i("MultiFileObserver", "CLOSE_WRITE: " + path);
                new Handler(getApplicationContext().getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(FileListenerService.this, DealActivity.class);
                        intent.putExtra("path", path);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }, 500);
                Log.d("onEvent ",currentPath);
                currentPath = "";
            }

        }
    }
}
