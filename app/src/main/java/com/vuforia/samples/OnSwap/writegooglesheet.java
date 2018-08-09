package com.vuforia.samples.OnSwap;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;

import com.vuforia.samples.VuforiaSamples.R;

public class writegooglesheet extends AsyncTask<String, Void, String> {
        String sheet_id;
        public static Resources resources;
        String Url;
        HashMap<String,String> switch_list;
        Context context;
        int Column = 0;
        String Column_name;
        private String TAG = "OnSwap: "+writegooglesheet.class.getSimpleName();
        String Open_Close;
        String User_name;
    writegooglesheet(HashMap<String, String> list, int index,String User, String open_close, Context context1) {
        this.switch_list = list;
        context = context1;
        Column = index+2;
        Column_name = "E"+String.valueOf(Column);
        Open_Close = open_close;
        User_name = User;
    }
    protected void onPreExecute(){
            sheet_id = context.getApplicationContext().getString(R.string.google_sheet_id);
            if(User_name.contains("oper"))
            {
                Url =  context.getApplicationContext().getString(R.string.update_cell_link);
            }
            else {
                Url = context.getApplicationContext().getString(R.string.write_drive_link);
                if(Open_Close.contains("open")) {
                    Url =  context.getApplicationContext().getString(R.string.update_cell_link);
                }
            }
            Url = "https://" + Url;
        }
    protected String doInBackground(String... arg0) {
        String rsp = null;
        if(User_name.contains("oper") || Open_Close.contains("open")) {
            rsp = updateGoogleSheetFromOperator();
        }
        else if(User_name.contains("admin")) {
            rsp = updateGoogleSheetFromadmin();
        }
        return rsp;
    }

    protected void onPostExecute(String result) {
        if(User_name.contains("oper")) {
            if (result != null && !result.contains("false"))
                Homescreen.adapter.notifyDataSetChanged();
            if (Homescreen.pDialog.isShowing())
                Homescreen.pDialog.dismiss();
        }
        if(Place_Order.pDialog != null && Place_Order.pDialog.isShowing()) {
            Place_Order.pDialog.dismiss();
        }
        if(Operator_fix_order.pDialog!=null && Operator_fix_order.pDialog.isShowing())
            Operator_fix_order.pDialog.dismiss();
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    public String updateGoogleSheetFromadmin() {
        try{

            URL url = new URL(Url);

            JSONObject postDataParams = new JSONObject();

            //int i;
            //for(i=1;i<=70;i++)
            //    String usn = Integer.toString(i);

            postDataParams.put("serial_number",switch_list.get("serial_number"));
            postDataParams.put("product_name",switch_list.get("product_name"));
            postDataParams.put("product_number",switch_list.get("product_number"));
            postDataParams.put("mac_address",switch_list.get("mac_address"));
            postDataParams.put("fault_type","");
            postDataParams.put("status",switch_list.get("status"));
            postDataParams.put("location", switch_list.get("location"));
            postDataParams.put("id", sheet_id);


            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    public String updateGoogleSheetFromOperator () {
        try {
            URL url = new URL(Url);
            JSONObject postDataParams = new JSONObject();

            postDataParams.put("cell",Column_name);
            Log.d(TAG,"Url"+Url+"column"+Column_name);
            if(User_name.contains("admin"))
                postDataParams.put("update_value","Power Fault");
            else
                postDataParams.put("update_value","");
            postDataParams.put("id",sheet_id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


