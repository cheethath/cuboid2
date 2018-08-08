package com.vuforia.samples.OnSwap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import com.vuforia.samples.VuforiaSamples.R;

public class Operator_fix_order extends Activity {
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static Operator_Custom_Adapter adapter;
    String Barcode_data;
    private String TAG = "OnSwap: "+Operator_fix_order.class.getSimpleName();
    static String scanned_data="";
    HashMap<String, String> switch_list;
    String Username;
    public static ProgressDialog pDialog;           //Prograss dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operator_final);
        listView=(ListView)findViewById(R.id.listview);
        pDialog = new ProgressDialog(Operator_fix_order.this);

        Username = (String) getIntent().getSerializableExtra("USER");
        switch_list = (HashMap<String, String>)getIntent().getSerializableExtra("SWI_LIST");
        Barcode_data = (String) getIntent().getSerializableExtra("serial_number");
        final Integer index = (Integer) getIntent().getSerializableExtra("Index");
        dataModels= new ArrayList<>();

        dataModels.add(new DataModel("Open Bar Code Scanner", "Scan and Identify the received HW is valid"));
        dataModels.add(new DataModel("Identify the Switch", "Scan the switch in AR to identify for replacing the HW"));
        dataModels.add(new DataModel("Replace the Hardware", "It will leads to how to replace a hardware"));
        dataModels.add(new DataModel("Validate the Hardware", "It will update the admin"));
        adapter= new Operator_Custom_Adapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DataModel dataModel= dataModels.get(position);
            switch (position) {
                case 0:
                    Log.d(TAG, "Barcode_scanning selected");
                    Intent usr = new Intent(Operator_fix_order.this, Barcode_scanner.class);
                    usr.putExtra("serial_number" ,Barcode_data);
                    startActivity(usr);
                    dataModels.add(new DataModel("", ""));
                    break;
                case 1:
                    if(scanned_data.isEmpty()){

                        Toast.makeText(getApplicationContext(), "Scanned data is not valid", Toast.LENGTH_SHORT).show();
                    }else {
                        //Intent intent = new Intent(this, com.vuforia.samples.VuforiaSamples.ui.ActivityList.ActivitySplashScreen.class);

                        //startActivity(intent);
                    }
                    Log.d(TAG, "Launching Vuforia selected");
                    break;
                case 2:
                    Log.d(TAG, "Replace the hardware");
                    break;
                case 3:
                    Log.d(TAG, "Update the admin");
                    pDialog.setMessage(getString(R.string.updating_job));
                    pDialog.setCancelable(false);
                    pDialog.show();
                    new writegooglesheet(switch_list,index,Username,"close",getApplicationContext()).execute();
                    break;
            }
        }
    });
  }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        if(!scanned_data.isEmpty())
            adapter.setNotifyOnChange(true);
    }
}