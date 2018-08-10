package com.vuforia.samples.OnSwap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.vuforia.samples.VuforiaSamples.R;

public class Place_Order extends AppCompatActivity {
    public static ProgressDialog pDialog;           //Prograss dialog
    private String TAG = "OnSwap: "+Place_Order.class.getSimpleName();
    Handler handler;
    public static String User_name = "";
    ArrayList<HashMap<String,String>> Swi_list;
    Integer index;
    TextView data;
    Button placeorder1,notify;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_order);
        handler = new Handler();
        data = findViewById(R.id.showdata);
        placeorder1 =  findViewById(R.id.placeorder);
        notify =  findViewById(R.id.updateoperator);
        placeorder1.setBackgroundColor(Color.YELLOW);
        notify.setBackgroundColor(Color.YELLOW);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            User_name = extras.getString("USER");
        }
        pDialog = new ProgressDialog(Place_Order.this);


        Swi_list = (ArrayList<HashMap<String,String>>) getIntent().getSerializableExtra("SWI_LIST");
        index = (Integer) getIntent().getSerializableExtra("Index");
        String place_order1 = "<font color='#EE0000'>Power Supply</font> has failed for this device. ";
        String place_order2 = "Aruba recommends replacing the power supply.";
        String place_order3 = "Please call our Support Center#";
        String place_order4 = "<font color='#0000EE'> 1-800-943-4526 </font>";
        String place_order5 = "or press below button to place an order at Aruba.This will ship the new power supply at operator location ";
        data.setText(Html.fromHtml(place_order1+place_order2+place_order3+place_order4+place_order5));
    }

    public void place_order(View view) {
        pDialog.setMessage("An order has been placed at Aruba. For new powersupply with Serial#JL2345");
        pDialog.setCancelable(false);
        pDialog.show();
        handler.postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 3000); // 3000 milliseconds delay

        placeorder1.setEnabled(false);
        placeorder1.setBackgroundColor(Color.GRAY);
        //Intent intent = new Intent(this, com.vuforia.samples.VuforiaSamples.ui.ActivityList.ActivitySplashScreen.class);
        Intent intent = new Intent(this, com.vuforia.samples.VuforiaSamples.app.ImageTargets.ImageTargets.class);

        startActivity(intent);
    }
    public void send_notify_to_operator(View view) {
        HashMap<String, String> switch_list = Swi_list.get(index);
        pDialog.setMessage("Operator has been notified about the shipment and failed device.");
        pDialog.setCancelable(false);
        pDialog.show();
        notify.setEnabled(false);
        notify.setBackgroundColor(Color.GRAY);
        new writegooglesheet(switch_list,index,User_name,"open",getApplicationContext()).execute();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 3000); // 3000 milliseconds delay
    }
}
