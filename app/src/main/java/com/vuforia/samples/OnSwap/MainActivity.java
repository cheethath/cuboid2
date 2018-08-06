package com.vuforia.samples.OnSwap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vuforia.samples.VuforiaSamples.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToSampleApp();

            }

        });
    }

    private void goToSampleApp() {

        Intent intent = new Intent(this, com.vuforia.samples.VuforiaSamples.ui.ActivityList.ActivitySplashScreen.class);

        startActivity(intent);

    }
}
