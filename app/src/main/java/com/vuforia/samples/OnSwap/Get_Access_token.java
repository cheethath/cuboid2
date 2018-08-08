package com.vuforia.samples.OnSwap;

/* Include this if required */
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class Get_Access_token{
    public Get_Access_token() {
    }
    static Response response_stage_one;
    static Response response_stage_two;
    static Response response_stage_three;
    static String access_token;

    public static String get_Access_token() {
        /* STAGE ONE to get CSRF and SESSION */
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String api_gateway_url = "https://internal-apigw.central.arubanetworks.com";
        String auth_stage_one_url = "/oauth2/authorize/central/api/login";
        String auth_stage_two_url = "/oauth2/authorize/central/api";
        String auth_stage_three_url = "/oauth2/token";
        String CLIENT_ID = "ackcsUUFTjTFmSXwBhAp7lHfdyq8JpZs";
        String CLIENT_SECRET = "99iCcBUY4ZwsggrnKgszr5PNE1Ri99WX";
        String CUSTOMER_ID = "alpha2_sub_749";
        String USERNAME = "michaelpaulgilbert+hpteam@gmail.com";
        String PASSWORD = "aruba@1234567";

        JSONObject json_body_stage_one = new JSONObject();
        try {
            json_body_stage_one.put("username", USERNAME);
            json_body_stage_one.put("password", PASSWORD);

        Log.e("MASTER", "PRING BODY STAGE ONE" + json_body_stage_one);
        String body_stage_one = json_body_stage_one.toString();

        //RequestBody body = RequestBody.create(mediaType, "{\"username\": \"michaelpaulgilbert+hpteam@gmail.com\", \"password\": \"aruba@1234567\"}");
        RequestBody body = RequestBody.create(mediaType, body_stage_one);

        String stage_one_url_prams = "?client_id=" + CLIENT_ID;
        String url = api_gateway_url + auth_stage_one_url + stage_one_url_prams;

        Log.e("MASTER", "Printing URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-type", "application/json")
                .build();

        Log.e("MASTER", "posted the query");

            response_stage_one = client.newCall(request).execute();
            Log.e("MASTER", "RESPONSE for stage 1: " + response_stage_one);

            Log.e("MASTER", "Came out");

            String raw_csrf = response_stage_one.headers("Set-Cookie").get(0);
            int start = raw_csrf.indexOf('=') + 1;
            int end = raw_csrf.indexOf(';');

            String csrf = raw_csrf.substring(start, end);

            Log.e("MASTER", "RAW_CSRF= " + raw_csrf);
            Log.e("MASTER", "CLEANED_CSRF= " + csrf);

            String raw_session = response_stage_one.headers("Set-Cookie").get(1);
            start = 0;
            end = raw_session.indexOf(';');
            String session = raw_session.substring(start, end);
            Log.e("MASTER", "RAW_SESSION= " + raw_session);
            Log.e("MASTER", "CLEANED_SESSION= " + session);

            url = api_gateway_url + auth_stage_two_url + stage_one_url_prams + "&response_type=code&scope=all";

            JSONObject json_body_stage_two = new JSONObject();

                json_body_stage_two.put("customer_id", CUSTOMER_ID);


            Log.e("MASTER", "PRING BODY STAGE TWO" + json_body_stage_two);
            String body_stage_two = json_body_stage_two.toString();

            mediaType = MediaType.parse("application/json");
            body = RequestBody.create(mediaType, body_stage_two);
            //body = RequestBody.create(mediaType, "{\"customer_id\": \"alpha2_sub_749\"}");
            Request request_two = new Request.Builder()
                    //.url("https://internal-apigw.central.arubanetworks.com/oauth2/authorize/central/api?client_id=ackcsUUFTjTFmSXwBhAp7lHfdyq8JpZs&response_type=code&scope=all")
                    .url(url)
                    .post(body)
                    .addHeader("X-CSRF-TOKEN", csrf)
                    .addHeader("Content-type", "application/json")
                    .addHeader("Cookie", session)
                    .build();

            Log.e("MASTER", "posted the query");
            JSONObject messagebody;

                response_stage_two = client.newCall(request_two).execute();
                Log.e("MASTER", "RESPONSE_1" + response_stage_two);
                messagebody = new JSONObject(response_stage_two.body().string());
                Log.e("MASTER", "Body Part of mesage" + messagebody);

                String auth_code = null;

                    auth_code = messagebody.getString("auth_code");

                Log.e("MASTER", "AUTH_CODE Received= " + auth_code);

                url = api_gateway_url + auth_stage_three_url + stage_one_url_prams + "&grant_type=authorization_code&client_secret=" + CLIENT_SECRET + "&code=" + auth_code;
                mediaType = MediaType.parse("application/json");
                body = RequestBody.create(mediaType, body_stage_two);
                Request request_three = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("X-CSRF-TOKEN", csrf)
                        .addHeader("Content-type", "application/json")
                        .addHeader("Cookie", session)
                        .build();
                Log.e("MASTER", "posted the query 3");
                JSONObject messagebodyfinal;
                    response_stage_three = client.newCall(request_three).execute();
                    Log.e("MASTER", "RESPONSE_1" + response_stage_three);
                    //Object obj = parser.parse(response1);
                        messagebodyfinal = new JSONObject(response_stage_three.body().string());
                        Log.e("MASTER", "Body Part of mesage" + messagebodyfinal);

                        access_token = messagebodyfinal.getString("access_token");

                        Log.e("MASTER", "ACCESS_TOCKET Received= " + access_token);

                        String refresh_token = messagebodyfinal.getString("refresh_token");

                        Log.e("MASTER", "refresh_token Received= " + refresh_token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return access_token;
    }
}

