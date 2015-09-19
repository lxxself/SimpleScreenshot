package com.lxxself.jietu.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxxself.jietu.R;

public class AboutActivity extends AppCompatActivity {

    private TextView versioncode;
    private ImageView iconemail;
    private ImageView iconlink;
    private ImageView icongithub;
    private String version = "1.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initialize();
        try {
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versioncode.setText("version: "+ version);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_share) {
            shareApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void shareApp() {
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); // 纯文本
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "分享自简截:\n为\n 了\n  更\n   好\n    的\n     截\n      图");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));

            }
        });
        thread.start();
    }

    private void initialize() {

        versioncode = (TextView) findViewById(R.id.version_code);
        iconemail = (ImageView) findViewById(R.id.icon_email);
        iconlink = (ImageView) findViewById(R.id.icon_link);
        icongithub = (ImageView) findViewById(R.id.icon_github);
    }
}
