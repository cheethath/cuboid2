package com.vuforia.samples.OnSwap;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import android.content.Context;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;

import com.vuforia.samples.VuforiaSamples.R;

public class readgooglesheet extends AsyncTask<String, Void, String> {
    public static Resources resources;
    static  boolean inet_processed = false;
    static ArrayList<HashMap<String,String>> switchList;
    private String TAG = "OnSwap: "+readgooglesheet.class.getSimpleName();
    private Context context;

    public readgooglesheet(Context context1){
        this.context=context1;
    }
    protected void onPreExecute(){
        resources = Resources.getSystem();
    }

    protected String doInBackground(String... arg0) {
        try{
            HttpHandler httpHandler = new HttpHandler();
            String jsonStr = null;
            String http_str = "http://";
            String https_str = "https://";
            String rest_url = https_str + resources.getString(R.string.read_drive_link) + resources.getString(R.string.google_sheet_id) +
                    "&sheet=" + resources.getString(R.string.sheet_num);
            Log.d(TAG, "rest_url is " + rest_url);
            if(Homescreen.User_name.contains("oper")) {
                Log.d(TAG, "hi is " + rest_url);
                if (checkConnection( this.context.getApplicationContext())) {
                    Log.d(TAG, "CHECK rest_url: " + rest_url);
                    jsonStr = httpHandler.getDataFromWeb(rest_url);
                    switchList = JsonRender.driveparser(jsonStr);
                    inet_processed = true;
                }else {
                    Log.d(TAG, "rest_url is not reachable: " + rest_url);
                }
            }
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
       if(inet_processed) {
           inet_processed = false;
               if(switchList != null)
               {
                //   new Homescreen(switchList);
                   inet_processed = false;
               }
           }else {
               Toast.makeText(context, "No Switches in the present network", Toast.LENGTH_SHORT).show();
           }

    }
    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public static boolean checkConnection(@NonNull Context context) {
        return  ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
