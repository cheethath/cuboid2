package com.vuforia.samples.OnSwap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;

import com.vuforia.samples.VuforiaSamples.R;

public class view3d extends AppCompatActivity {
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view3d);
        WebView myWebView = (WebView) findViewById(R.id.activity_main_webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        Bundle extras = getIntent().getExtras();
        String swi_type = "";
        if (extras != null) {
            swi_type = extras.getString("swi_type");
        }
        if(JsonRender.islavalist.contains(swi_type)) {
                myWebView.loadUrl("https://www.arubanetworks.com/3d/?prod=https://apps.kaonadn.net/5185710160084992/product.html#1/184&t=Aruba%202930M&width=960&height=650");
        }
        else if(JsonRender.isbondlist.contains(swi_type)){
            myWebView.loadUrl("https://www.arubanetworks.com/3d/?prod=https://apps.kaonadn.net/5185710160084992/product.html#1/168&t=Aruba%203810&width=960&height=650");
        }
        else if(JsonRender.isboltlist.contains(swi_type)){
            myWebView.loadUrl("https://www.arubanetworks.com/3d/?prod=https://apps.kaonadn.net/5185710160084992/product.html#1/165&t=Aruba%205400R&width=960&height=650");
        }
        else if(JsonRender.issantorinilist.contains(swi_type)){
            myWebView.loadUrl("https://www.arubanetworks.com/3d/?prod=https://apps.kaonadn.net/5185710160084992/product.html#1/171&t=Aruba%202930F&width=960&height=650");
        }
    }
}

