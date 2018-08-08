package com.vuforia.samples.OnSwap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.Result;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import com.vuforia.samples.VuforiaSamples.R;

public class Barcode_scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView BarScanner;
    private String TAG = "OnSwap: "+Homescreen.class.getSimpleName();
    private Result scan_data;
    String barcode_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);                // Set the scanner view as the content view
        BarScanner = (ZXingScannerView)findViewById(R.id.scanner_fragment);
        barcode_data = (String)getIntent().getSerializableExtra("serial_number");
    }

    @Override
    public void handleResult(Result result) {
        Log.d(TAG,"handleresult:"+result);

        // If you would like to resume scanning, call this method below:
   //   BarScanner.resumeCameraPreview(this);
        scan_data = result;
        Toast.makeText(getApplicationContext(), "scanned Barcode: " + result, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResume() {
        super.onResume();
        BarScanner.setResultHandler(this); // Register ourselves as a handler for scan results.
        BarScanner.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        BarScanner.stopCamera();           // Stop camera on pause
    }

    public void rescan_barcode(View view) {
        Toast.makeText(getApplicationContext(), "Rescan Barcode: ", Toast.LENGTH_SHORT).show();
        BarScanner.setResultHandler(this); // Register ourselves as a handler for scan results.
        BarScanner.startCamera();
    }
    public void scan_complete (View view) {
        Toast.makeText(getApplicationContext(), "Scan complete: ", Toast.LENGTH_SHORT).show();
        BarScanner.stopCamera();
//       Homescreen.scanned=true;
        if(scan_data!=null) {
    //        Homescreen.scanned_text = scan_data.getText();
            Log.d(TAG, "scan format" + scan_data.getText());
            //need to comment
            Operator_fix_order.scanned_data=scan_data.getText();
            if(barcode_data!=null && barcode_data.contains(scan_data.getText()))
            {
                Log.d(TAG, "Data is sucess");
                Operator_fix_order.scanned_data=scan_data.getText();
            }
        }
       // Homescreen.scanned_info=scan_data.getBarcodeFormat();
        finish();
    }
}
