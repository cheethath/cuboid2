package com.vuforia.samples.OnSwap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import java.util.ArrayList;

import com.vuforia.samples.VuforiaSamples.R;

public class datasheet extends AppCompatActivity {
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specification);
        WebView myWebView = (WebView) findViewById(R.id.spec_webview);
        myWebView.getSettings().setJavaScriptEnabled(true);

        Bundle extras = getIntent().getExtras();
        String swi_type = "";
        if (extras != null) {
            swi_type = extras.getString("swi_type");
        }
        if(JsonRender.islavalist.contains(swi_type)) {
            myWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=https://www.arubanetworks.com/assets/ds/DS_2930MSwitchSeries.pdf");
        }
        else if(JsonRender.isbondlist.contains(swi_type)){
            myWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=https://www.arubanetworks.com/assets/ds/DS_3810SwitchSeries.pdf");
        }
        else if(JsonRender.isboltlist.contains(swi_type)){
            myWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=https://www.arubanetworks.com/assets/ds/DS_5400Rzl2SwitchSeries.pdf");
        }
        else if(JsonRender.issantorinilist.contains(swi_type)){
            myWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=https://www.arubanetworks.com/assets/ds/DS_2930FSwitchSeries.pdf");
        }
    }
}

