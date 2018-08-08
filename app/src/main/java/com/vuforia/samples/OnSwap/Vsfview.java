package com.vuforia.samples.OnSwap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.toBinaryString;

import com.vuforia.samples.VuforiaSamples.R;

public class Vsfview extends AppCompatActivity {
    /* Start UI Items */
    private String TAG = Vsfview.class.getSimpleName();
    static boolean flag = false;
    Spinner config;
    ImageView swi_image;
    Button locateimage;
    TextView switchname ;
    TextView vsflink2,vsflink1;
    String put_param;
    String put_url;
    LinearLayout layout_linear;
    static String swi_imagename,swi_productname,swi_macaddress,swi_stackornot,swi_ipadd;
    private ProgressDialog pDialog;           //Prograss dialog
    private static String rest_url = null;    //rest_url to put switchInfos JSON
    Animation animation;
    HashMap<String, String> sw_info;
    Spinner spinner_id,vsfport1_spinner,vsfport2_spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descriptor);

        swi_image = (ImageView) findViewById(R.id.swi_image);
        locateimage = (Button) findViewById(R.id.locateimage);
        switchname = (TextView) findViewById(R.id.switchname);
        /*
        spinner_id = (Spinner) findViewById(R.id.spinner_id);
        vsfport1_spinner= (Spinner) findViewById(R.id.vsfport1_spinner);
        vsfport2_spinner = (Spinner) findViewById(R.id.vsfport2_spinner);
        vsflink2 = (TextView) findViewById(R.id.vsflink2);
        vsflink1 =  (TextView) findViewById(R.id.vsflink1);
*/

        /* Spinner */
        /*
        final List<String> list = new ArrayList<String>();
        list.add("Spanning tree");
        list.add("Vsf enable");
        list.add("Vlan Configure");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_id.setAdapter(dataAdapter);

        List<String> vsfport1 = new ArrayList<String>();
        for (int i=1;i<48;i++){
            vsfport1.add(Integer.toString(i));
        }
        ArrayAdapter<String> vsfport1dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, vsfport1);
        vsfport1dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vsfport1_spinner.setAdapter(vsfport1dataAdapter);

        List<String> vsfport2 = new ArrayList<String>();
        for (int j=1;j<48;j++){
            vsfport1.add(Integer.toString(j));
        }
        ArrayAdapter<String> vsfport2dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, vsfport2);
        vsfport2dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vsfport2_spinner.setAdapter(vsfport2dataAdapter);

*/
        Intent intent = getIntent();
        sw_info = (HashMap<String, String>) intent.getSerializableExtra("sw_info");
        swi_imagename = sw_info.get("product_image").toString();
        swi_productname = sw_info.get("product_name").toString();
        swi_macaddress = sw_info.get("mac_address").toString();
        swi_stackornot = sw_info.get("stack_or_not").toString();
        swi_ipadd = sw_info.get("serial_number").toString();
        locateimage.setBackgroundResource(R.drawable.led_off);
        int swit_int = Integer.parseInt(swi_imagename);
        swi_image.setImageResource(swit_int);
        switchname.setText(swi_productname);

        animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        locateimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_url = getString(R.string.locateled_url);
                if(flag) {
                    put_param = getString(R.string.located_led_off);
                    flag = false;
                }
                else {
                    put_param = getString(R.string.located_led_blink);
                    flag = true;
                }
                new locator().execute();
            }
        });
        /*
        spinner_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(list.get(position) == "Vsf enable" ) {
                    vsflink1.setVisibility(View.VISIBLE);
                    vsflink2.setVisibility(View.VISIBLE);
                    vsfport1_spinner.setVisibility(View.VISIBLE);
                    vsfport2_spinner.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        */
    }
    /**
     * Async task class to get json and pingable ip to connect to the switch
     */
    class locator extends AsyncTask<Void, Void , Void> {
        /* Adding to get the pingable ip */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Vsfview.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            final int lower_subnet = 100;    /* Start of subnet*/
            final int upper_subnet = 150;    /* End of subnet*/
            final int ping_timeout = 10;

            HttpHandler httpHandler = new HttpHandler();
            String http_str = "http://";
            rest_url = http_str + swi_ipadd + put_url;
            httpHandler.putServiceCall(rest_url,put_param);
            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /* *
             * Updating parsed JSON data into ListView
             * */
            if(flag)
            {
            /*  Locator led  */
                locateimage.setBackgroundResource(R.drawable.led_on);
                locateimage.startAnimation(animation);
            }
            else
            {
                locateimage.clearAnimation();
                locateimage.setBackgroundResource(R.drawable.led_off);
            }
        }
    }
}
