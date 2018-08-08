package com.vuforia.samples.OnSwap;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ravi Tamada on 01/09/16.
 * www.androidhive.info
 */
public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String putServiceCall(String reqUrl,String... strpr) {
        String data = "";
        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        //RequestBody body = RequestBody.create(mediaType, "{\n\t\"led_blink_status\":false\n}");
        RequestBody body = RequestBody.create(mediaType, strpr[0]);
        Request request = new Request.Builder()
                .url(reqUrl)
                .post(body)
                .build();
        //.addHeader("content-type", "application/json")
          //      .addHeader("cache-control", "no-cache")
        //.addHeader("postman-token", "64cc26d3-52ff-3bae-3a2b-eadf3cc80b47")

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  data;
    }
    public static void deleteExcelSheet() {
        String url = "https://script.google.com/macros/s/AKfycbwlCyCTS1qWpznJnLa32DM0X41DBb58ESYXTiAH1JsIM0Mph4g/exec?id=1eLKTlTgip_EfvlWANLvFgMJcoPgUQmS815DUH0DFlEE";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.e("MASTER", "Response is this: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ;
    }
    public static String setAccessTokentoSheet(String update_value) {
        String base_url = "https://script.google.com/macros/s/AKfycbwGnSuIGv9IWMTHNj37FSfc7TgZGVU3PYUoCaS5TXgw7l9RYNbH/exec?id=1eLKTlTgip_EfvlWANLvFgMJcoPgUQmS815DUH0DFlEE&update_value=";
        String url = base_url + update_value;
        if(update_value == null)
            return "FAILURE";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.e("MASTER", "Response is this: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    public  String getAccessTokenFromSheet(String ARUBA_URL){
        String strrsp = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ARUBA_URL)
                .build();
        try {
            Response response = client.newCall(request).execute();
            strrsp = response.body().string();
            return strrsp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  String getDataFromAruba(String ARUBA_URL){
        String strrsp = null;
        try {

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"username\": \"michaelpaulgilbert+hpteam@gmail.com\", \"password\": \"aruba@1234567\"}");
            Request request = new Request.Builder()
                    .url(ARUBA_URL)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "d39cca0c-d9a3-4330-b6e7-0bba782e265f")
                    .build();
            Response response = client.newCall(request).execute();
            strrsp = response.body().string();
            return strrsp;
        } catch (@NonNull IOException e) {
            Log.d(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }
    public String getswitchFromAruba (String ARUBA_URL) {
        String strrsp = null;
        try {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ARUBA_URL)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if(response.code() >= 400)
            return  "error_code_40*";
        strrsp = response.body().string();
        return strrsp;
        } catch (@NonNull IOException e) {
            Log.d(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }
    public String getHwEvents (String ARUBA_URL) {
        String strrsp = null;
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ARUBA_URL)
                    .addHeader("Content-type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() >= 400)
                return "error_code_40*";
            strrsp = response.body().string();
            return strrsp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
        public String getDataFromWeb(String MAIN_URL) {
        String strrsp = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MAIN_URL)
                    .build();
            Response response = client.newCall(request).execute();
            if(response.code() >= 400)
                return  "error_code_40*";
            strrsp = response.body().string();
            return  strrsp;
        } catch (@NonNull IOException e) {
            Log.d(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }
    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    private String chassislocate_led(String on_off) {
        OkHttpClient client = new OkHttpClient();
        Log.e("MASTER", "Creating Query");
        MediaType mediaType = MediaType.parse("application/json");
        String access_token = "PBdcVcdXhyOorQdvxeHMZd2Cgt1zAb1Z";
        String central_url = "https://internal-apigw.central.arubanetworks.com/device_management/v1/device/";
        String switch_serial_number = "SG50GPR1J8";
        Response blink_on_response = null;

        mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, new byte[0]);
        String led_on_command = central_url + switch_serial_number + "/action/" + on_off +"?access_token=" + access_token;
        Request request = new Request.Builder()
                .post(body)
                .url(led_on_command)
                .addHeader("Content-type", "application/json")
                .build();

        Log.e("MASTER", "posted the query");
        try {
            blink_on_response = client.newCall(request).execute();
            Log.e("MASTER", "Display response= " + blink_on_response + "Code Received= " + blink_on_response.code());

            if( blink_on_response.code() >= 400)
            {
                Log.e("MASTER", "ERROR: Unable to turn on the LED");
                return "error_code_40*";
            }else {
                return "OK";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}