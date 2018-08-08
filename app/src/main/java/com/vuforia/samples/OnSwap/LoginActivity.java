package com.vuforia.samples.OnSwap;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vuforia.samples.VuforiaSamples.R;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText username, password;
    TranslateAnimation user_ani,pswd_ani,login_ani;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.btnlogin);
        username = (EditText) findViewById(R.id.txtuser);
        password = (EditText) findViewById(R.id.txtpass);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
        user_ani = new TranslateAnimation(-800, 0, 0, 0);
        pswd_ani =  new TranslateAnimation(800, 0, 0, 0);
        login_ani = new TranslateAnimation(0, 0, 1800, 0);
        user_ani.setDuration(1000); // duartion in ms
        user_ani.setFillAfter(false);
        pswd_ani.setDuration(1000); // duartion in ms
        pswd_ani.setFillAfter(false);
        login_ani.setDuration(1000); // duartion in ms
        login_ani.setFillAfter(false);
        username.startAnimation(user_ani);
        password.startAnimation(pswd_ani);
        login.startAnimation(login_ani);
    }
    public void login(){
        String user=username.getText().toString().trim();
        String pass=password.getText().toString().trim();
        if((user.equals("aruba")&& pass.equals("aruba")) || (user.equals("admin")&& pass.equals("admin")) ||
                (user.contains("oper")&& pass.contains("oper"))  )
        {
            password.onEditorAction(EditorInfo.IME_ACTION_DONE);
            username.onEditorAction(EditorInfo.IME_ACTION_DONE);
            if(user.contains("oper")) {
                Intent usr = new Intent(LoginActivity.this, Homescreen.class);
                usr.putExtra("USER", user);
                startActivity(usr);
            }else {
                Intent usr = new Intent(LoginActivity.this, Admin_dashboard.class);
                usr.putExtra("USER", user);
                startActivity(usr);
            }

        }else {
            Toast.makeText(this,"username and password do not matched!",Toast.LENGTH_LONG).show();
        }
    }
}