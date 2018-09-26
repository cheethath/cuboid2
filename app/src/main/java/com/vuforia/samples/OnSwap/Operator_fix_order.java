package com.vuforia.samples.OnSwap;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import com.vuforia.samples.VuforiaSamples.R;

public class Operator_fix_order extends Activity {
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static Operator_Custom_Adapter adapter;
    String Barcode_data;
    Integer index_pos=0;
    private String TAG = "OnSwap: "+Operator_fix_order.class.getSimpleName();
    static String scanned_data="";
    HashMap<String, String> switch_list;
    String Username;
    Intent usr_barcode;
    public static ProgressDialog pDialog;           //Prograss dialog
   static PopupWindow popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operator_final);
        listView=(ListView)findViewById(R.id.listview);
        pDialog = new ProgressDialog(Operator_fix_order.this);
        Username = (String) getIntent().getSerializableExtra("USER");
        switch_list = (HashMap<String, String>)getIntent().getSerializableExtra("SWI_LIST");
        Barcode_data = (String) getIntent().getSerializableExtra("serial_number");
        final Integer[] index = {(Integer) getIntent().getSerializableExtra("Index")};
        dataModels= new ArrayList<>();
      //  LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dataModels.add(new DataModel(R.drawable.barcode,"Open Bar Code Scanner", "Identify the received HW is valid"));
        dataModels.add(new DataModel(R.drawable.swi_5400,"Demo to Scan the Switch", "Video will display how to scan the switch"));
        dataModels.add(new DataModel(R.drawable.swi_5400,"Scan the Switch", "Scan the chassislocate led from camera"));
        dataModels.add(new DataModel(R.drawable.tech,"Replace the Hardware", "Video will display to replace the hardware"));
        dataModels.add(new DataModel(R.drawable.shareinfo,"Updating Hardware has replaced", "It will send update to the admin"));
        adapter= new Operator_Custom_Adapter(dataModels,getApplicationContext());
       // popup = new PopupWindow(inflater.inflate(R.layout.thumps_up, null, false),100,100, true);
     //   popup.showAtLocation(Operator_fix_order.findViewById(R.id.popup), Gravity.CENTER, 0, 0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DataModel dataModel= dataModels.get(position);
            switch (position) {
                case 0:
                    index_pos = position;
                    if (ContextCompat.checkSelfPermission(Operator_fix_order.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Operator_fix_order.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    } else {
                        usr_barcode = new Intent(Operator_fix_order.this, Barcode_scanner.class);
                        usr_barcode.putExtra("serial_number", Barcode_data);
                        startActivity(usr_barcode);
                    }
                    Log.d(TAG, "Barcode_scanning selected");
                    break;
                case 1:

                    if(!scanned_data.isEmpty()) {
                        Intent intent = new Intent(Operator_fix_order.this, Play_Video.class);
                        intent.putExtra("selected_video",0);
                        startActivity(intent);
                    } else
                    {
                        Toast.makeText(getApplicationContext(), "Scanned data is invalid", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    index_pos = 1;
                    if (ContextCompat.checkSelfPermission(Operator_fix_order.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Operator_fix_order.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                        } else {
                            // Intent intent = new Intent(Operator_fix_order.this, com.vuforia.samples.VuforiaSamples.app.ImageTargets.ImageTargets.class);
                            Intent intent = new Intent(Operator_fix_order.this, com.vuforia.samples.VuforiaSamples.app.VuMark.VuMark.class);
                            startActivity(intent);
                        }
                    Log.d(TAG, "Launching Vuforia selected");
                    break;
                case 3:
                    Log.d(TAG, "Replace the hardware");
                    Intent intent = new Intent(Operator_fix_order.this, Play_Video.class);
                    intent.putExtra("selected_video",1);
                    startActivity(intent);
                    break;
                case 4:
                    Log.d(TAG, "Update the admin");
                    pDialog.setMessage(getString(R.string.updating_job));
                    pDialog.setCancelable(false);
                    pDialog.show();
                   // showPopup(Operator_fix_order.this,p);
                    new writegooglesheet(switch_list, index[0],Username,"close",getApplicationContext()).execute();
                    break;
            }
        }
    });
  }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(index_pos == 0) {
                    Intent usr = new Intent(Operator_fix_order.this, Barcode_scanner.class);
                    usr.putExtra("serial_number", Barcode_data);
                    startActivity(usr);
                }else if(index_pos == 1) {
                    //Intent intent = new Intent(this, com.vuforia.samples.VuforiaSamples.app.ImageTargets.ImageTargets.class);
                    Intent intent = new Intent(Operator_fix_order.this, com.vuforia.samples.VuforiaSamples.app.VuMark.VuMark.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error case", Toast.LENGTH_LONG).show();
            }
        }
    }
    // The method that displays the popup.
    private void showPopup(final Activity context, Point p) {
        int popupWidth = 200;
        int popupHeight = 150;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.thumps_up, viewGroup);

        // Creating the PopupWindow
        popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }
}