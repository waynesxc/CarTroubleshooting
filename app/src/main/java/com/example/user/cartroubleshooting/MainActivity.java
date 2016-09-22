package com.example.user.cartroubleshooting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetwork()){
                    FaultPage();
                }else{
                    Toast.makeText(MainActivity.this, "請開啟網路設定", Toast.LENGTH_LONG).show();
                    SettingWifi();
                }
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn1 = (ImageButton) findViewById(R.id.imageButton1);

        toolbar.setTitle("  " + getString(R.string.app_name));
        toolbar.setLogo(R.drawable.app_icon);
        setSupportActionBar(toolbar);
    }

    private void FaultPage(){
        /*Pair<View, String> p1 = Pair.create((View) btn1, "btn1");   //紀錄元件 id 與 transitionName
        Pair<View, String> p2 = Pair.create((View) btn2, "btn2");
        Pair<View, String> p3 = Pair.create((View) btn3, "btn3");
        Pair<View, String> p4 = Pair.create((View) btn4, "img4");*/
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, FaultActivity.class);
        startActivity(intent, options.toBundle());
        MainActivity.this.finish();
    }

    //判斷網路方法
    private boolean CheckNetwork() {
        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        //判斷是否有網路
        if (info == null || !info.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }

    //開啟網路方法
    private void SettingWifi() {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
        Toast.makeText(MainActivity.this, "請將網路功能開啟", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // 返回鍵的監聽事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("結束程式")
                    .setIcon(R.drawable.exit)
                    .setMessage("是否離開此系統?")
                    .setNegativeButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    MainActivity.this.finish();
                                }
                            })
                    .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
