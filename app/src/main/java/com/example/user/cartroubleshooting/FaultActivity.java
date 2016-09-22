package com.example.user.cartroubleshooting;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.cartroubleshooting.MysqlProcess.MysqlConnect;

public class FaultActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Spinner sp1, sp2;
    private ListView listView;
    private int FaultType = 0;    //紀錄故障類別用
    //故障狀況
    private String[] Situation = {"請選擇", "車發不動", "煞車問題", "引擎過熱", "行車有問題"};
    //故障類別
    private String [] Reason = null;
    private ArrayAdapter<String> Situation_List, Reason_List, Phenomenon_List;
    private MysqlConnect MC = new MysqlConnect();
    private String [] Phenomenon = null, Solve = null;  //故障現象陣列, 解決方法陣列
    //設定ListView清單用
    private MyAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault);

        init();
        setupWindowAnimations();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePage();
            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    SettingSpinner2(position);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (CheckNetwork()) {
                        FaultType = FaultTypeCheck(Reason[position]);   //紀錄故障型態 type
                        //Toast.makeText(FaultActivity.this, "TYPE:"+FaultType, Toast.LENGTH_SHORT).show();
                        new Mysql_Select().execute();
                    } else {
                        Toast.makeText(FaultActivity.this, "請開啟網路設定", Toast.LENGTH_LONG).show();
                        SettingWifi();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        sp1 = (Spinner) findViewById(R.id.sp_situation);
        sp2 = (Spinner) findViewById(R.id.sp_reason);
        listView = (ListView) findViewById(R.id.listView);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle("  " + getString(R.string.app_name));
        toolbar.setLogo(R.drawable.app_icon);

        //設置 Spinner 故障狀況清單
        Situation_List = new ArrayAdapter<String>(FaultActivity.this, android.R.layout.simple_spinner_item, Situation);
        sp1.setAdapter(Situation_List);
    }

    private void HomePage(){
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(FaultActivity.this);
        Intent intent = new Intent();
        intent.setClass(FaultActivity.this, MainActivity.class);
        startActivity(intent);
        FaultActivity.this.finish();
    }

    private void setupWindowAnimations() {
        Transition transition;
        transition = TransitionInflater.from(FaultActivity.this).inflateTransition(R.transition.slide_and_changebounds);
        getWindow().setEnterTransition(transition);
    }

    //設定故障原因清單
    private void SettingSpinner2(int pos){
        sp2.setAdapter(null);
        switch (pos){
            case 1:
                Reason = new String[]{"請選擇", "電器引起的引擎故障", "燃料引起的引擎故障", "冷卻系統引起的引擎故障"};
                break;
            case 2:
                Reason = new String[]{"請選擇", "制動系統故障"};
                break;
            case 3:
                Reason = new String[]{"請選擇", "冷卻系統故障"};
                break;
            case 4:
                Reason = new String[]{"請選擇", "操控性能失常", "懸吊系統故障", "電器系統故障", "傳動系統故障"};
                break;
        }

        //設置 Spinner 故障類別清單
        Reason_List = new ArrayAdapter<String>(FaultActivity.this, android.R.layout.simple_spinner_item, Reason);
        sp2.setAdapter(Reason_List);
    }

    /*設定故障現象清單
    private void SettingSpinner3(){
        sp3.setAdapter(null);
        //設置 Spinner 故障類別清單
        Phenomenon_List = new ArrayAdapter<String>(FaultActivity.this, android.R.layout.simple_spinner_item, Phenomenon);
        sp3.setAdapter(Phenomenon_List);
    }*/

    //故障類型判斷
    private int FaultTypeCheck(String Fault){
        int type = 0;
        if(Fault.equals("電器引起的引擎故障"))
            type = 1;
        else if(Fault.equals("燃料引起的引擎故障"))
            type = 2;
        else if(Fault.equals("冷卻系統引起的引擎故障"))
            type = 3;
        else if(Fault.equals("制動系統故障"))
            type = 4;
        else if(Fault.equals("操控性能失常"))
            type = 5;
        else if(Fault.equals("冷卻系統故障"))
            type = 6;
        else if(Fault.equals("懸吊系統故障"))
            type = 7;
        else if(Fault.equals("電器系統故障"))
            type = 8;
        else
            type = 9;
        return type;
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
        Toast.makeText(FaultActivity.this, "請將網路功能開啟", Toast.LENGTH_SHORT).show();
    }

    // 返回鍵的監聽事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            new AlertDialog.Builder(FaultActivity.this)
                    .setTitle("結束程式")
                    .setIcon(R.drawable.exit)
                    .setMessage("是否離開此系統?")
                    .setNegativeButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    FaultActivity.this.finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 連接 Mysql 資料庫 (Select)
    class Mysql_Select extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listView.setAdapter(null);
            pDialog = new ProgressDialog(FaultActivity.this);
            pDialog.setMessage("查詢資料庫中...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            try {
                int sign1 = 0, sign2 = 0;   //字串擷取用 (字串格式: 故障現象,解決方法,故障現象,解決方法, ....)
                String [] temp = null;  //暫存用

                //查詢資料庫 (取得 故障現象、解決方法 欄位)
                temp = MC.Mysql_Select_array("http://10.200.27.219:8080/Car/select.php", String.valueOf(FaultType), "reason", "solve");
                Phenomenon = new String[temp.length];
                Solve = new String[temp.length];

                Thread.sleep(500);

                for(int i=0; i < temp.length; i++){
                    //System.out.println("Temp: "+temp[i]);
                    sign2 = temp[i].indexOf(",", 0);    //找出第1個 "," 的位置
                    Phenomenon[i] = temp[i].substring(sign1, sign2);
                    sign1 = sign2 + 1;

                    sign2 = temp[i].indexOf(",", sign1);    //找出第2個 "," 的位置
                    Solve[i] = temp[i].substring(sign1, sign2);
                    sign1 = 0;  //將字首歸0
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            adapter = new MyAdapter(FaultActivity.this, Phenomenon, Solve);
            listView.setAdapter(adapter);
        }
    }
}