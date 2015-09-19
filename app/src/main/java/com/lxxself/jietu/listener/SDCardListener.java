package com.lxxself.jietu.listener;

import android.content.Intent;
import android.os.FileObserver;
import android.util.Log;

/**
 * Created by Administrator on 2015/6/18.
 */
public class SDCardListener extends FileObserver {
    public SDCardListener(String path) {
              /*
               * 这种构造方法是默认监听所有事件的,如果使用 super(String,int)这种构造方法，
               * 则int参数是要监听的事件类型.
               */
        super(path);
    }

    @Override
    public void onEvent(int event, String path) {
        String resultStr=null;
        switch(event) {
            case FileObserver.ALL_EVENTS:
                Log.d("SDlistener", "all-path:" + path);
                break;
            case FileObserver.CREATE:
                Log.d("SDlistener", "create-path:"+ path);
                break;
        }
    }
}
