package com.vuforia.samples.OnSwap;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.Gravity;
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
    TranslateAnimation user_ani, pswd_ani, login_ani;
    static String user;
    private String TAG = "OnSwap: "+LoginActivity.class.getSimpleName();
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
        pswd_ani = new TranslateAnimation(800, 0, 0, 0);
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

    public void login() {
        user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if ((user.equals("aruba") && pass.equals("aruba")) || (user.equals("admin") && pass.equals("admin")) ||
                (user.contains("oper") && pass.contains("oper"))) {
            password.onEditorAction(EditorInfo.IME_ACTION_DONE);
            username.onEditorAction(EditorInfo.IME_ACTION_DONE);

            if (user.contains("oper")) {
                Intent opr_usr = new Intent(LoginActivity.this, Homescreen.class);
                opr_usr.putExtra("USER", user);
                startActivity(opr_usr);
            } else {
                new AsyncTaskRunner().execute();
            }
        }
        else
        {
            Toast.makeText(this, "username and password do not matched!", Toast.LENGTH_LONG).show();
        }

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                Log.d(TAG, "Deleting switches in the google sheet url");
                httpHandler.deleteExcelSheet();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            Intent usr = new Intent(getApplicationContext(), Admin_dashboard.class);
            usr.putExtra("USER", user);
            startActivity(usr);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage(getString(R.string.wait));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }
}