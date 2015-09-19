package com.lxxself.jietu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.lxxself.jietu.R;
import com.lxxself.jietu.service.FileListenerService;
import com.lxxself.jietu.service.FrontService;
import com.lxxself.jietu.service.ShakeService;

import java.io.File;

import static com.lxxself.jietu.util.CheckRoot.isRoot;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static String folder ;
    private Intent intent;
    private Switch swNotify;
    private Switch swShake;
    private Switch swListen;
    private boolean isFirstIn = true;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean("swNotify", swNotify.isChecked());
        editor.putBoolean("swShake", swShake.isChecked());
        editor.putBoolean("swListen", swListen.isChecked());
        editor.putBoolean("isFirstIn", isFirstIn);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("简截");
        initialize();
        folder = newSDPath();
        swNotify.setOnCheckedChangeListener(this);
        swShake.setOnCheckedChangeListener(this);
        swListen.setOnCheckedChangeListener(this);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        if (pref != null) {
            swNotify.setChecked(pref.getBoolean("swNotify",true));
            swShake.setChecked(pref.getBoolean("swShake",false));
            swListen.setChecked(pref.getBoolean("swListen", false));
            isFirstIn = pref.getBoolean("isFirstIn", false);

        }
//        if (savedInstanceState != null) {
//            swNotify.setChecked(savedInstanceState.getBoolean("swNotifyState"));
//            swShake.setChecked(savedInstanceState.getBoolean("swShakeState"));
//            swListen.setChecked(savedInstanceState.getBoolean("swListenState"));
//        }
        if (isFirstIn) {
            isFirstIn = false;
            if (isRoot()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("设备已root" + "\n" + "请通过app的root请求" + "\n" + "小米用户需自行前往设置");
                builder.setTitle("提示");
                builder.create().show();
                swNotify.setChecked(true);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("设备未root，截图功能受限" + "\n" + "请尝试解决开启设备root权限");
                builder.setTitle("提示");
                builder.create().show();
                swListen.setChecked(true);
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean("swNotifyState", swNotify.isChecked());
//        outState.putBoolean("swShakeState", swShake.isChecked());
//        outState.putBoolean("swListenState", swListen.isChecked());
    }



    public String newSDPath() {
        File sdDir = null ;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ; //判断sd卡是否存在
        if(sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory() ;//获取跟目录
        }

        String path=sdDir.getPath()+"/Jietu";
        File file=new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        Log.d("newSDPath",Environment.getExternalStorageDirectory()+"");
        Log.d("newSDPath",Environment.getDataDirectory()+"");
        Log.d("newSDPath",Environment.getRootDirectory()+"");
        Log.d("newSDPath", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"");
        return path;
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume ", "onResume ");
    }
    //singleTop或者singleInstance模式下传递参数需要重载onNewIntent()方法
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //here we can use getIntent() to get the extra data.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initialize() {

        swNotify = (Switch) findViewById(R.id.swNotify);
        swShake = (Switch) findViewById(R.id.swShake);
        swListen = (Switch) findViewById(R.id.swListen);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.swNotify:
                intent = new Intent(MainActivity.this, FrontService.class);
                if (isChecked) {
                    swNotify.setChecked(true);
                    startService(intent);
                } else {
                    swNotify.setChecked(false);
                    stopService(intent);
                }
                break;
            case R.id.swShake:
                intent = new Intent(MainActivity.this, ShakeService.class);
                if (isChecked) {
                    swShake.setChecked(true);
                    startService(intent);
                } else {
                    stopService(intent);
                    swShake.setChecked(false);
                }
                break;
            case R.id.swListen:
                intent = new Intent(MainActivity.this, FileListenerService.class);
                if (isChecked) {
                    startService(intent);
                    swListen.setChecked(true);
                } else {
                    stopService(intent);
                    swListen.setChecked(false);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
