package com.example.user.cartroubleshooting.MysqlProcess;

//註:資料庫的方法不可在主執行緒中執行, 需另外使用執行續執行, 否則會出錯

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class MysqlConnect {
    // 資料庫查詢方法 (取得特定欄位: Column1, Column2) 回傳值為String array版本
    public String[] Mysql_Select_array(String url, String type, String Column1, String Column2) {
        InputStream is = null;
        String result = ""; // 查詢結果
        String line = "";
        String [] data = null; // 取得資料

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();	//餵給php端的參數
        nameValuePairs.add(new BasicNameValuePair("type", type)); // 查詢user_ID (須依據情況修改php檔)
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.i("pass 1", "connection success ");
        } catch (Exception e) {
            Log.i("Error 1", "IP Address Error!");
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();
            //System.out.println("Result: "+result);
        } catch (Exception e) {
            Log.e("Error 2", "Return Error!");
        }

        try {
            JSONArray jsonArray = new JSONArray(result);
            data = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                data[i] = jsonData.getString(Column1)+","+jsonData.getString(Column2)+","; // 只取得特定欄位資料
            }
            Log.i("pass 3", "return success ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // 資料庫新增方法 (參數: 新增的欄位資料   註:須依據情況修改php檔)
    public int Mysql_Insert(String url, String id, String passwd, String name) {
        InputStream is = null;
        String result = ""; // 查詢結果
        String line = "";
        int code = 0;

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();	//餵給php端的參數
        nameValuePairs.add(new BasicNameValuePair("id", id)); // 新增user_ID資料 (須依據情況修改php檔)
        nameValuePairs.add(new BasicNameValuePair("passwd", passwd)); // 新增user_passwd資料 (須依據情況修改php檔)
        nameValuePairs.add(new BasicNameValuePair("name", name)); // 新增 data資料 (須依據情況修改php檔)

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.i("pass 1", "connection success ");
        } catch (Exception e) {
            Log.i("Error 1", "IP Address Error!");
            e.printStackTrace();
            return 0;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();
            Log.i("return data", result);
        } catch (Exception e) {
            Log.e("Error 2", "Return Error!");
            return 0;
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if(code == 1)
                Log.i("Success", "Insert Into Success");
            else
                Log.i("Error", "Insert Into Error");
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return code;
    }


    // 資料庫更新方法
    public int Mysql_Update(String url, String userID, String update_data) {
        InputStream is = null;
        String result = ""; // 查詢結果
        String line = "";
        int code = 0;

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();	//餵給php端的參數
        nameValuePairs.add(new BasicNameValuePair("id", userID)); // 查詢user_ID (須依據情況修改php檔)
        nameValuePairs.add(new BasicNameValuePair("data", update_data)); // 要更新的資料 data (須依據情況修改php檔)

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.i("pass 1", "connection success ");
        } catch (Exception e) {
            Log.i("Error 1", "IP Address Error!");
            e.printStackTrace();
            return 0;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();
            Log.i("return data", result);
        } catch (Exception e) {
            Log.e("Error 2", "Return Error!");
            return 0;
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if(code == 1)
                Log.i("Success", "Upload Success");
            else
                Log.i("Error", "Upload Error");
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return code;
    }


    // 資料庫刪除方法
    public int Mysql_Delete(String url, String userID) {
        InputStream is = null;
        String result = ""; // 查詢結果
        String line = "";
        int code = 0;

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();	//餵給php端的參數
        nameValuePairs.add(new BasicNameValuePair("id", userID)); // 查詢user_ID, 來決定要delete的資料 (須依據情況修改php檔)

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.i("pass 1", "connection success ");
        } catch (Exception e) {
            Log.i("Error 1", "IP Address Error!");
            e.printStackTrace();
            return 0;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();
            Log.i("return data", result);
        } catch (Exception e) {
            Log.e("Error 2", "Return Error!");
            return 0;
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if(code == 1)
                Log.i("Success", "Delete Success");
            else
                Log.i("Error", "Delete Error");
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return code;
    }
}
