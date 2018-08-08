package com.vuforia.samples.OnSwap;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ramotion.circlemenu.CircleMenuView;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.vuforia.samples.OnSwap.JsonRender.swi_img;
import static com.vuforia.samples.OnSwap.ListSwitchService.SWITCH_LIST;

import com.vuforia.samples.VuforiaSamples.R;

public class
Homescreen extends AppCompatActivity {

    /* Start UI Items */
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static String User_name = "";
    public static boolean scanned = false;
    public static String scanned_text;
    private String TAG = "OnSwap: "+Homescreen.class.getSimpleName();
    private ImageButton btnScan;              //Button btnScan;
    public static ProgressDialog pDialog;           //Prograss dialog

    /* Start Variables Definition */
    static ArrayList <HashMap<String,String>> switchList;

    String[] subnet_component;
    String subnet;
    ListView theListView;
    /* End Variables Definition */
    public static FoldingCellListAdapter adapter;
    public static ArrayList<Item> items;
    Intent int_failure_receive;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }
    private Intent intent;
    private BroadcastReceiver broadcastReceiver;
    public static CircleMenuView menu;
    public static int Index_on_list=0;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            User_name = extras.getString("USER");
        }
        setContentView(R.layout.activity_main);

        menu = findViewById(R.id.circle_menu);
        theListView = findViewById(R.id.mainListView);
        switchList = new ArrayList<>();
        pDialog = new ProgressDialog(Homescreen.this);
        menu.setEventListener(new CircleMenuView.EventListener() {
                @Override
                public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                    Log.d(TAG, "onMenuOpenAnimationStart");
                }

                @Override
                public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                    Log.d(TAG, "onMenuOpenAnimationEnd");
                }

                @Override
                public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                    Log.d(TAG, "onMenuCloseAnimationStart");
                }

                @Override
                public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                    Log.d(TAG, "onMenuCloseAnimationEnd");
                    menu.setVisibility(View.GONE);
                    theListView.setVisibility(View.VISIBLE);
                }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                Log.d(TAG, "onButtonClickAnimationStart| index: " + index);
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Log.d(TAG, "onButtonClickAnimationEnd| index: " + index);
                menu_invoke_with_index(index);
                /* Need to add the code here */
            }

            @Override
            public boolean onButtonLongClick(@NonNull CircleMenuView view, int index) {
                Log.d(TAG, "onButtonLongClick| index: " + index);
                return true;
            }

            @Override
            public void onButtonLongClickAnimationStart(@NonNull CircleMenuView view, int index) {
                Log.d(TAG, "onButtonLongClickAnimationStart| index: " + index);
            }

            @Override
            public void onButtonLongClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Log.d(TAG, "onButtonLongClickAnimationEnd| index: " + index);
                menu_invoke_with_index(index);
            }
            });

        menu.setVisibility(View.INVISIBLE);
        theListView.setVisibility(View.VISIBLE);
        items = new ArrayList<>();
        if (User_name != null) {
            if (User_name.contains("oper")) {
                int_failure_receive = new Intent(getApplicationContext(), ListSwitchService.class);
                int_failure_receive.putExtra("User_name", User_name);
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        prepare_elements_to_display_by_service(intent);
                    }
                };
                if (!isMyServiceRunning(ListSwitchService.class)) {
                    startService(int_failure_receive);
                    pDialog.setMessage(getString(R.string.wait));
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
            } else if (User_name.contains("admin")) {
                //   setContentView(R.layout.admin_dashboaard);
                Toast.makeText(getApplicationContext(), "User is selected admin so contents are static to display the view", Toast.LENGTH_SHORT).show();
                ArrayList<HashMap<String, String>> Swi_list = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("SWI_LIST");
                String show_state = extras.getString("STATE");
                if(Swi_list != null) {
                    ArrayList<Item> swi_items = new ArrayList<>();
                    for (int i = 0; i < Swi_list.size(); i++) {
                        HashMap<String, String> switch_list = Swi_list.get(i);
                        if (switch_list.get("product_name") == null || switch_list.get("mac_address") == null ||
                                switch_list.get("product_number") == null || switch_list.get("faulttype") == null ||
                                switch_list.get("status") == null || switch_list.get("location")==null) {
                            continue;
                        }
                        int swi_image = Integer.parseInt(switch_list.get("product_image"));

                        if (show_state.contains("Up") && switch_list.get("status").contains("Down")) {
                            continue;
                        } else if (show_state.contains("Down") && switch_list.get("status").contains("Up")) {
                            continue;
                        }
                        swi_items.add(new Item(swi_image, "6 mins", switch_list.get("product_name"), switch_list.get("mac_address"), 0,
                                switch_list.get("product_name"), switch_list.get("product_number"), switch_list.get("faulttype"), switch_list.get("status"),switch_list.get("location")));
                    }
                    if(adapter != null)
                    {
                        adapter.clear();
                        adapter.addAll(swi_items);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = new FoldingCellListAdapter(this, swi_items);
                    }

                    switchList = Swi_list;
                    prepare_elements_to_display(Swi_list, swi_items);
                }
            }
        }
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
    @Override
    public void onStart() {
        super.onStart();
        if (User_name.contains("oper")) {
            LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                    new IntentFilter(ListSwitchService.DOWNLOAD_ACTION)
            );
        }
    }
    private void menu_invoke_with_index(int Index) {
        scanned = false;
        switch (Index) {
            case 1:
                Intent usr = new Intent(Homescreen.this, Barcode_scanner.class);
                usr.putExtra("USER" , "oper");
                usr.putExtra("Index_choosed" ,Index_on_list);
                startActivity(usr);
                break;
            case 3:
                HashMap<String, String> switch_list = switchList.get(Index_on_list);
                pDialog.setMessage(getString(R.string.updating_job));
                pDialog.setCancelable(false);
                pDialog.show();
                items.get(Index_on_list).setStatus("closed");
                adapter.add(items.get(Index_on_list));
                new writegooglesheet(switch_list,Index_on_list,User_name,"close",getApplicationContext()).execute();
                break;
        }
        Log.d(TAG,"Select index"+Index_on_list);
    }
    private void prepare_elements_to_display_by_service(Intent intent) {
        ArrayList<HashMap<String,String>> Swi_list = (ArrayList<HashMap<String,String>>) intent.getSerializableExtra("SWI_LIST");
        ArrayList<HashMap<String,String>> dashboard;
        ArrayList<Item> swi_items  = new ArrayList<>();
        dashboard = new ArrayList<>();
        for (int i = 0; i < Swi_list.size(); i++) {
                HashMap<String, String> switch_list = Swi_list.get(i);
                if(switch_list.get("product_name") == null || switch_list.get("mac_address") == null ||
                        switch_list.get("product_number") == null || switch_list.get("faulttype")==null ||
                        switch_list.get("status")==null || switch_list.get("location")==null) {
                    continue;
                }

            dashboard.add(Swi_list.get(i));
                     if(User_name.equals("oper1")) {
                    if(switch_list.get("location").contains("midtown")){
                        continue;
                    }
                }else if(User_name.equals("oper")) {
                    if(switch_list.get("location").contains("woodstock")) {
                        continue;
                    }
                }
            int swi_image = Integer.parseInt(switch_list.get("product_image"));
            swi_items.add(new Item(swi_image, "6 mins", switch_list.get("product_name"), switch_list.get("mac_address"), 0,
                    switch_list.get("product_name"), switch_list.get("product_number"),switch_list.get("faulttype"),switch_list.get("status"),switch_list.get("location")));

        }

        if(adapter != null)
        {
            adapter.clear();
            adapter.addAll(swi_items);
            adapter.notifyDataSetChanged();
        } else {

            adapter = new FoldingCellListAdapter(this, swi_items);
        }

        switchList = Swi_list;
        prepare_elements_to_display(dashboard,swi_items);
       // menu.bringToFront();
    }
    private void prepare_elements_to_display(final ArrayList<HashMap<String,String>> Swilist, final ArrayList<Item> swiitems) {

        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedpos = "Value getTag:" + v.getTag();
         //       if(User_name.contains("oper")) {
                    int index = (int) v.getTag();
                    Log.d (TAG ,"Check index:"+ index);
                    if(Swilist != null) {
                        Index_on_list=index;
                        items = swiitems;
                        int i = 0;
                        for ( ; i < switchList.size(); i++) {
                            HashMap<String, String> switch_list = switchList.get(i);
                            if(switch_list.get("mac_address").contains(swiitems.get(index).getMac_address())) {
                                Log.d(TAG,"selected switch for hw change:"+switch_list.get("product_name"));
                                 break;
                            }
                        }
                        if(i==switchList.size())
                            return;
                        /* Need code here for all type of admin */
                        //menu.setVisibility(View.VISIBLE);
                        //menu.open(true);
                        if(User_name.contains("admin")) {
                           Intent usr = new Intent(Homescreen.this, Place_Order.class);
                            usr.putExtra("USER", User_name);
                            usr.putExtra("SWI_LIST",Swilist);
                            usr.putExtra("Index",i);
                            startActivity(usr);
                        }else {
                            if(!swiitems.get(index).getFaulttype().isEmpty()) {
                                Intent usr = new Intent(Homescreen.this, Operator_fix_order.class);
                                usr.putExtra("serial_number",switchList.get(i).get("serial_number"));
                                usr.putExtra("Index",i);
                                usr.putExtra("SWI_LIST",switchList.get(i));
                                usr.putExtra("USER",User_name);
                                startActivity(usr);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "This option is used for Hardware Failure request", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        });

        adapter.setDefaultview3dBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                String selectedpos = swiitems.get(index).getJltype();
                //Toast.makeText(getApplicationContext(),selectedpos, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Homescreen.this, view3d.class);
                i.putExtra("swi_type" , selectedpos);
                startActivity(i);
            }
        });
        adapter.setDefaultspecClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                String selectedpos =  swiitems.get(index).getJltype();
                //Toast.makeText(getApplicationContext(),selectedpos, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Homescreen.this, datasheet.class);
                i.putExtra("swi_type" , selectedpos);
                startActivity(i);
            }
        });
        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });
        menu.bringToFront();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        if(scanned)
            menu.open(true);
    }

    @Override
    protected void onDestroy() {
        Log.d("MAINACT", "onDestroy!");
        if(User_name.contains("oper")) {
            LocalBroadcastManager.getInstance(Homescreen.this).unregisterReceiver(broadcastReceiver);
            stopService(int_failure_receive);
        }
        super.onDestroy();
    }
}
