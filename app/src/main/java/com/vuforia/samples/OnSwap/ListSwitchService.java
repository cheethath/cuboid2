package com.vuforia.samples.OnSwap;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.vuforia.samples.OnSwap.Homescreen.pDialog;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.vuforia.samples.VuforiaSamples.R;

public class ListSwitchService extends Service {
    private String TAG = "OnSwap: "+ ListSwitchService.class.getSimpleName();
    private Intent intent;
    private LocalBroadcastManager broadcastManager;
    public int counter=0;
    static ArrayList<HashMap<String,String>> switchList;
    ArrayList<HashMap<String,String>> previous_swilist;
    static  boolean inet_processed = false;
    public final static String DOWNLOAD_ACTION = "com.vuforia.samples.failureswitch";
    public final static String SWITCH_LIST = "switch.list";
    public String User_name;
    public String access_token;
    boolean isSwitchaccess= false;
    static boolean init_state =false;
    boolean send_notify = false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //init Intent
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
   //     super.onStartCommand(intent, flags, startId);
        User_name = intent.getStringExtra("User_name");
        if(User_name.contains("admin")) {
            isSwitchaccess = true;
            new DownloadFileFromGooglesheetTask().execute();
        }
        startTimer();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        if(User_name.contains("admin")){
            //schedule the timer, to wake up every 5 min * (60 * 1000) # 60 sec
            timer.schedule(timerTask, 0,20 * 1000); //
        }else
         {
            //schedule the timer, to wake up every 10 # 60 sec
            timer.schedule(timerTask, 0, 20 * 1000); //
        }
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.d(TAG, "in timer ++++  "+ (counter++));
                /* Validate the hw failure */
                if(User_name.contains("admin")) {
                    if (counter % 30 == 0) {
                        isSwitchaccess = true;
                        new DownloadFileFromGooglesheetTask().execute();
                    } else if (!init_state && counter > 2 && counter % 2 != 0) {
                        isSwitchaccess = false;
                        new DownloadFileFromGooglesheetTask().execute();
                    }
                }else if (User_name.contains("oper")) {
                    if (counter % 2 != 0) {
                        new DownloadFileFromGooglesheetTask().execute();
                    }
                }
            }
        };
    }
    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public static boolean checkConnection(@NonNull Context context) {
        return  ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    private class DownloadFileFromGooglesheetTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... downloadUrl) {
            if(User_name.contains("oper")) {
                try {
                    HttpHandler httpHandler = new HttpHandler();
                    String jsonStr = null;
                    String https_str = "https://";
                    String rest_url = https_str + getString(R.string.read_drive_link) + getString(R.string.google_sheet_id) +
                            "&sheet=" + getString(R.string.sheet_num);
                    switchList = new ArrayList<>();
                    Log.d(TAG, "Google api url is " + rest_url);
                    if (checkConnection(getApplicationContext())) {
                        jsonStr = httpHandler.getDataFromWeb(rest_url);
                        if(jsonStr != null && !jsonStr.contains("error_code_40*")) {
                            switchList = JsonRender.driveparser(jsonStr);
                            inet_processed = true;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Check the network connection", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "rest_url is not reachable: " + rest_url);
                    }

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }else if(User_name.contains("admin")) {
                HttpHandler httpHandler = new HttpHandler();
                String jsonStr = null;
                String https_str = "https://";
                String rest_url;
                if(access_token == null) {
                    init_state=true;
                }
                if(access_token==null || access_token.isEmpty()) {
                    rest_url = https_str +getString(R.string.get_access_token_from_sheet)+"&sheet=Sheet2";
                    Log.d(TAG, "Getting access token from google sheet url:" + rest_url);
                    jsonStr = httpHandler.getAccessTokenFromSheet(rest_url);
                    access_token = JsonRender.get_token_from_sheet(jsonStr);

                    if(access_token == null) {
                        Log.d(TAG, "Access token in Google sheet url is empty");
                        access_token = Get_Access_token.get_Access_token();
                        Log.d(TAG, "Getting access token from Aruba");
                        httpHandler.setAccessTokentoSheet(access_token);
                        Log.d(TAG, "Updating access token into the google sheet");
                    }
                }
                if(access_token == null)
                    return null;
                if(isSwitchaccess) {
                    rest_url = https_str +getString(R.string.aruba_get_switches)+access_token;
                    Log.d(TAG, "Getting switch detail from Aruba url:"+rest_url);
                    switchList = new ArrayList<>();
                }else {
                    rest_url = https_str + getString(R.string.HW_EVENTS) + access_token+"&serial="+getString(R.string.switch_serial_number);
                    Log.d(TAG, "Getting switch Hw_event from Aruba url:"+rest_url);
                }
                if (checkConnection(getApplicationContext())) {
                    if(isSwitchaccess) {
                        jsonStr = httpHandler.getswitchFromAruba(rest_url);
                        if(jsonStr ==null || jsonStr.contains("error_code_40*")) {
                            Log.d(TAG, "Getting access token from Aruba");
                            access_token = Get_Access_token.get_Access_token();
                            httpHandler.setAccessTokentoSheet(access_token);
                            rest_url = https_str + getString(R.string.aruba_get_switches) + access_token;
                            Log.d(TAG, "Generated with access token aruba_rest_url is " + rest_url);
                            jsonStr = httpHandler.getswitchFromAruba(rest_url);
                        }
                        if(jsonStr !=null) {
                            switchList = JsonRender.aruba_url_parser(jsonStr);
                            inet_processed = true;
                        }
                    }else {
                        jsonStr = httpHandler.getHwEvents(rest_url);
                        if(jsonStr ==null || jsonStr.contains("error_code_40*")){
                            Log.d(TAG, "Getting access token from Aruba");
                            access_token = Get_Access_token.get_Access_token();
                            httpHandler.setAccessTokentoSheet(access_token);
                            rest_url = https_str +getString(R.string.HW_EVENTS) + access_token+"&serial="+getString(R.string.switch_serial_number);
                            Log.d(TAG, "Generated with access token aruba_rest_url is " + rest_url);
                            jsonStr = httpHandler.getHwEvents(rest_url);
                            Log.d(TAG, "Checking for any hardware events from aruba" + rest_url);
                        }
                        if(jsonStr !=null) {
                            String Failure = JsonRender.hw_failures(jsonStr);
                            if(!send_notify && Failure != null && Failure.contains("FAILURE"))
                            {
                                for (int i = 0; i < switchList.size(); i++) {
                                    HashMap<String, String> switch_list = switchList.get(i);
                                    if (switch_list.get("serial_number").contains(getString(R.string.switch_serial_number))) {
                                        if(!switch_list.get("faulttype").isEmpty())
                                            break;
                                        send_notify = true;
                                        switch_list.put("faulttype","Power Fault");
                                        switchList.set(i,switch_list);
                                        /* Need to add in product */
                                   //     new writegooglesheet(switch_list,i,User_name,"open",getApplicationContext()).execute();
                                        break;
                                    }
                                }
                            }
                            inet_processed = true;
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Check the network connection", Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            Log.d(TAG, "Process update ++++  "+ (counter));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        @Override
        protected void onPostExecute(String downloadUrl) {
            if (Homescreen.pDialog != null && Homescreen.pDialog.isShowing()) {
                Homescreen.pDialog.dismiss();
            }
            if (User_name.contains("admin") && isSwitchaccess) {
                if (Admin_dashboard.pDialog != null && Admin_dashboard.pDialog.isShowing())
                    Admin_dashboard.pDialog.dismiss();
            }
            if (User_name.contains("oper") || isSwitchaccess || send_notify) {
                if (inet_processed) {
                    boolean update_ui = PushNotification();
                    if (update_ui) {
                        intent = new Intent(DOWNLOAD_ACTION);
                        intent.putExtra("SWI_LIST", switchList);
                        broadcastManager.sendBroadcast(intent);

                    }
                }
               // Toast.makeText(getApplicationContext(), "Download successful!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean check_any_change_in_the_list()
    {
        if(switchList==null || switchList.size()==0) return false;
        if(switchList.get(0).get("mac_address") == "") return false;
        int i = 0;
        for (i = 0; i < switchList.size(); i++) {
            HashMap<String, String> switch_list = switchList.get(i);
            if (switch_list.get("product_name") == null || switch_list.get("mac_address") == null ||
                    switch_list.get("product_number") == null || switch_list.get("faulttype") == null ||
                    switch_list.get("status")==null || switch_list.get("location")==null) {
                switchList.remove(i);
            }
        }
        if(switchList.size()==0) return false;

        if(counter == 1) {
            previous_swilist = new ArrayList<>();
            previous_swilist = switchList;
            return true;
        }
        if(previous_swilist == null)
           previous_swilist = new ArrayList<>();
        if (previous_swilist.size() != switchList.size()) {
            previous_swilist = switchList;
            return true;
        }
        return true;
    }
    public boolean check_to_send_notification() {
        int i = 0;
        if(User_name.contains("oper")) {
            for (; i < previous_swilist.size(); i++) {
                if (!previous_swilist.get(i).get("mac_address").contains(switchList.get(i).get("mac_address"))) {
                    /* Column matches */
                    break;
                } else {
                    if (!previous_swilist.get(i).get("faulttype").contains(switchList.get(i).get("faulttype"))) {
                        if((User_name.equals("oper") && previous_swilist.get(i).get("location").contains("midtown") ||
                          User_name.equals("oper1") && previous_swilist.get(i).get("location").contains("woodstock")) &&
                           !switchList.get(i).get("faulttype").isEmpty())
                            break;
                        else
                            return false;
                    }
                }
            }
            if (i == previous_swilist.size()) {
                return true;
            }else {
                previous_swilist = switchList;
                return false;
            }
        } else if(User_name.contains("admin")) {
            if(!send_notify) return true;
        }
        return false;
    }
    public boolean PushNotification()
    {
        if(!check_any_change_in_the_list()){
          return false;
        }
        if(check_to_send_notification()){
          return true;
        }
        Log.d(TAG,"Push notify"+ (counter++));
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
        Intent ii;
        ii = new Intent(this.getApplicationContext(), Homescreen.class);
        if(User_name.contains("admin")) {
            ii = new Intent(this.getApplicationContext(), Admin_dashboard.class);
        }
        ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ii.putExtra("USER" , User_name);
        ii.putExtra("SWI_LIST" , switchList);
        Log.d("User_name", "-->  "+ (User_name));
        Log.d("List", "-->  "+ switchList);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Aruba notified a switch has reported fault");
        bigText.setBigContentTitle("Aruba Hw Failure");
        bigText.setSummaryText("To verify the switch fault launch OnSwap App");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.notify_logo);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "HW Failure",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
        send_notify = false;
        return TRUE;
    }
}