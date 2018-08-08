package com.vuforia.samples.OnSwap;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vuforia.samples.VuforiaSamples.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.vuforia.samples.OnSwap.ListSwitchService.SWITCH_LIST;
import com.vuforia.samples.VuforiaSamples.R;


public class Admin_dashboard extends AppCompatActivity {
    ArrayList<HashMap<String,String>> list_of_type;
    Intent int_failure_receive;
    public static ProgressDialog pDialog;           //Prograss dialog
    private String TAG = "OnSwap: "+Admin_dashboard.class.getSimpleName();
    private BroadcastReceiver adminbroadcastReceiver;
    private String User_name;
    TextView overall,live,dead,hwfailure;
    LinearLayout overall_layout,live_layout,nonlive_layout,hwfail_layout;
    TranslateAnimation anime_over,anime_live,anime_dead,anime_fail;
    ArrayList<HashMap<String,String>> Swi_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboaard);
        pDialog = new ProgressDialog(Admin_dashboard.this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            User_name = extras.getString("USER");
        }
        overall = (TextView)findViewById(R.id.overall);
        live = (TextView)findViewById(R.id.liveswitch);
        dead = (TextView)findViewById(R.id.nonliveswitch);
        hwfailure = (TextView)findViewById(R.id.hwfailure);
        overall_layout = (LinearLayout)findViewById(R.id.layout_1);
        live_layout= (LinearLayout)findViewById(R.id.layout_2);

        int_failure_receive = new Intent(getApplicationContext(),ListSwitchService.class);
        int_failure_receive.putExtra("User_name",User_name);
        adminbroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                prepare_elements_to_display_admin_service(intent);
            }
        };
        if (!isMyServiceRunning(ListSwitchService.class)) {
            if(!User_name.isEmpty()) {

                startService(int_failure_receive);
                pDialog.setMessage(getString(R.string.wait));
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((adminbroadcastReceiver),
                new IntentFilter(ListSwitchService.DOWNLOAD_ACTION)
        );
    }
    private void prepare_elements_to_display_admin_service(Intent intent) {
        overall_layout.setVisibility(View.VISIBLE);
        live_layout.setVisibility(View.VISIBLE);
        Integer up = 0 , down = 0;
        Swi_list = (ArrayList<HashMap<String,String>>) intent.getSerializableExtra("SWI_LIST");
        Log.d(TAG,"swilist"+ Swi_list);
        if(Swi_list == null)
            return;
        Integer nosswi = Swi_list.size();
        up=nosswi;
        String no_of_swi = nosswi.toString();
        for (int i = 0; i < Swi_list.size(); i++) {
            HashMap<String, String> switch_list = Swi_list.get(i);
            if (switch_list.get("product_name") == null || switch_list.get("mac_address") == null ||
                    switch_list.get("product_number") == null || switch_list.get("faulttype") == null ||
                    switch_list.get("status")==null || switch_list.get("location")==null) {
                    continue;
            }
            if(switch_list.get("status").equals("Down"))
            {
                up--;
            }
        }
        down=nosswi-up;
        String swi_up = up.toString();
        String swi_dwn = down.toString();
        Integer hw_fil = nosswi - (up + down);
        String hw_fail = hw_fil.toString();
        overall.setText(no_of_swi);
        live.setText(swi_up);
        dead.setText(swi_dwn);
        hwfailure.setText(hw_fail);
        list_of_type=Swi_list;
        Log.d(TAG,"Updating Google sheet with the collected data");
        update_switches_to_google_sheet(Swi_list);
    }

    private boolean update_switches_to_google_sheet(ArrayList<HashMap<String,String>> switchs){
        if (switchs == null)
            return false;
        if(ListSwitchService.init_state) {
            for (int i = 0; i < switchs.size(); i++) {
                HashMap<String, String> switch_list = switchs.get(i);
                if (switch_list.get("product_name") == null || switch_list.get("mac_address") == null ||
                        switch_list.get("product_number") == null || switch_list.get("faulttype") == null ||
                        switch_list.get("status") == null ||  switch_list.get("location")==null) {
                    continue;
                }
                /* close to mention for updating entire row for admin */
                new writegooglesheet(switch_list, i, User_name, "close", getApplicationContext()).execute();
                ListSwitchService.init_state=false;
            }
        }
        return true;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d (TAG ,"isMyServiceRunning?"+ true+"");
                return true;
            }
        }
        Log.d (TAG,"isMyServiceRunning?"+false+"");
        return false;
    }

    public void Overall_Invoke(View view) {
        if(list_of_type!=null) {
            Intent usr = new Intent(Admin_dashboard.this, Homescreen.class);
            usr.putExtra("STATE", "Overall");
            usr.putExtra("SWI_LIST", list_of_type);
            usr.putExtra("USER", User_name);
            startActivity(usr);
        }
    }

    public void Live_Switch(View view) {
        if(list_of_type!=null) {
            Intent usr = new Intent(Admin_dashboard.this, Homescreen.class);
            usr.putExtra("STATE", "Up");
            usr.putExtra("SWI_LIST", list_of_type);
            usr.putExtra("USER", User_name);
            startActivity(usr);
        }
    }
    public void NonLive_Switch(View view) {
        if(list_of_type!=null) {
            Intent usr = new Intent(Admin_dashboard.this, Homescreen.class);
            usr.putExtra("STATE", "Down");
            usr.putExtra("SWI_LIST", list_of_type);
            usr.putExtra("USER", User_name);
            startActivity(usr);
        }
    }
    public void Failure_Invoke(View view) {
        boolean i = false;
        if (i) {
            if (list_of_type != null) {
                Intent usr = new Intent(Admin_dashboard.this, Homescreen.class);
                usr.putExtra("STATE", "Down");
                usr.putExtra("SWI_LIST", list_of_type);
                usr.putExtra("USER", User_name);
                startActivity(usr);
            }
        }
    }

    protected void onDestroy() {
        Log.d("MAINACT", "onDestroy!");
            LocalBroadcastManager.getInstance(Admin_dashboard.this).unregisterReceiver(adminbroadcastReceiver);
            stopService(int_failure_receive);
        super.onDestroy();
    }
}
