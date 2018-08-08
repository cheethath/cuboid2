package com.vuforia.samples.OnSwap;

import android.util.Log;
import android.widget.Toast;

import com.vuforia.samples.VuforiaSamples.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.vuforia.samples.VuforiaSamples.R;

public class JsonRender {
    private static final String TAG = JsonRender.class.getSimpleName();
    public static final String KEY_CONTACTS = "Sheet1";
    // Array of integers points to images stored in /res/drawable-ldpi/
    static int[] swi_img = new int[]{
            R.drawable.swi_2930f,
            R.drawable.swi_2930m,
            R.drawable.swi_3810m,
            R.drawable.swi_5400,
    };
    public static final String  ARUBA_SWITCHES = "switches";
    public class Keys {
        public static final String KEY_CONTACTS = "Sheet1";
        public static final String KEY_NAME = "name";
        public static final String KEY_COUNTRY = "country";

    }

    static String[] islava = new String[]{"JL320A", "JL322A", "JL323A","JL324A","JL992A","JL993A"};
    static String[] issantorini = new String[]{"JL257A","JL258A","JL253A","JL254A","JL255A","JL256A","JL259A","JL260A","JL261A","JL262A","JL558A","JL557A"};
    static String[] isbond = new String[]{"JL074A","JL073A","JL076A","JL072A","JL071A","JL075A","JL077A"};
    static String[] isbolt = new String[]{"J9821A","J9822A"};
    public static List<String> islavalist = Arrays.asList(islava);
    public static List<String> issantorinilist = Arrays.asList(issantorini);
    public static List<String> isbondlist = Arrays.asList(isbond);
    public static List<String> isboltlist = Arrays.asList(isbolt);



    public JsonRender() {
    }
    public static HashMap<String, String> switchscanParser(String jsonStr, String ip) {
        HashMap<String, String> returnList = null;
        String product_name = null;
        String product_number = null;
        String mac_address = null;
        String stack_or_not = null;
        String switch_type = null;
        String[] stack = {"vsf_stack","bps_stack"};

        if (jsonStr != null) {
            JSONObject reader = null;
            try {
                reader = new JSONObject(jsonStr);
                product_name = reader.getString("product_name");
                product_number = reader.getString("product_number");
                mac_address = reader.getString("mac_address");
                switch_type = reader.getString("switch_type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(product_name != null) {
                returnList = new HashMap<>();
                // adding each child node to HashMap key => value
                returnList.put("ip_address", (ip != null) ? ip : "");
                returnList.put("product_name", (product_name != null) ? product_name : "");
                returnList.put("product_number", (product_number != null) ? product_number : "");
                returnList.put("mac_address", (mac_address != null) ? mac_address : "");
                returnList.put("stack_or_not", "");
                if (issantorinilist.contains(product_number)) {
                    if (switch_type == "ST_STACKED") {
                        returnList.put("stack_or_not", stack[0]);
                    }
                    returnList.put("product_image", Integer.toString(swi_img[0]));
                } else if (islavalist.contains(product_number)) {
                    if (switch_type == "ST_STACKED") {
                        returnList.put("stack_or_not", stack[1]);
                    }
                    returnList.put("product_image", Integer.toString(swi_img[1]));
                } else if (isbondlist.contains(product_number)) {
                    if (switch_type == "ST_STACKED") {
                        returnList.put("stack_or_not", "bps_stack");
                    }
                    returnList.put("product_image", Integer.toString(swi_img[2]));
                }
            }
        }
        return returnList;
    }
    public static ArrayList <HashMap<String,String>> driveparser(String jsonStr) {
        HashMap<String, String> returnList = null;

        ArrayList <HashMap<String,String>> switchList = null;
        String mac_address = null;
        String fault_state = null;
        String product_name = null;
        String product_number = null;
        String stack_or_not = null;
        String serial_number = null;
        String status = null;
        String location = null;
        ArrayList<Item> items = Item.getTestingList();
        int jIndex = 0;
        if (jsonStr != null) {
            JSONObject reader = null;
            try {
                reader = new JSONObject(jsonStr);
                JSONArray array = reader.getJSONArray(KEY_CONTACTS);
                int lenArray = array.length();
                switchList = new ArrayList<>();
                if (lenArray > 0) {
                    for (; jIndex < lenArray; jIndex++) {
                        returnList = new HashMap<>();
                        /**
                         * Getting Inner Object from contacts array...
                         * and
                         * From that We will get Name of that Contact
                         *
                         */
                        JSONObject innerObject = array.getJSONObject(jIndex);
                        mac_address = innerObject.getString("mac_address");
                        fault_state = innerObject.getString("fault_type");
                        product_name = innerObject.getString("product_name");
                        product_number = innerObject.getString("product_number");
                        status = innerObject.getString("status");
                        serial_number = innerObject.getString("serial_number");
                        location = innerObject.getString("location");
                        if(product_number == null || product_name == null ||  mac_address == null || serial_number == null || status == null)
                            continue;
                        returnList.put("mac_address", mac_address);
                        returnList.put("faulttype", fault_state);
                        returnList.put("product_name", product_name);
                        returnList.put("product_number", product_number);
                        returnList.put("status", status);
                        returnList.put("stack_or_not", "");
                        if (issantorinilist.contains(product_number)) {
                               returnList.put("product_image", Integer.toString(swi_img[0]));
                        } else if (islavalist.contains(product_number)) {
                               returnList.put("product_image", Integer.toString(swi_img[1]));
                        } else if (isbondlist.contains(product_number)) {
                               returnList.put("product_image", Integer.toString(swi_img[2]));
                        } else if (isboltlist.contains(product_number)) {
                               returnList.put("product_image", Integer.toString(swi_img[3]));
                        }
                        returnList.put("serial_number", serial_number);
                        returnList.put("stack_or_not", "");
                        returnList.put("location",location);
                        switchList.add(returnList);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return switchList;
    }

    public static ArrayList <HashMap<String,String>> aruba_url_parser(String jsonStr) {
        HashMap<String, String> returnList = null;

        ArrayList <HashMap<String,String>> switchList = null;
        String mac_address = null;
        String fault_state = null;
        String product_name = null;
        String product_number = null;
        String stack_or_not = null;
        String serial_number = null;
        String location = null;
        String status = null;
        int i = 0;
        ArrayList<Item> items = Item.getTestingList();
        int jIndex = 0;
        if (jsonStr != null) {
            JSONObject reader = null;
            try {
                reader = new JSONObject(jsonStr);
                JSONArray array = reader.getJSONArray(ARUBA_SWITCHES);
                int lenArray = array.length();
                switchList = new ArrayList<>();
                if (lenArray > 0) {
                    for (; jIndex < lenArray; jIndex++) {
                        boolean is_product_match = false;
                        returnList = new HashMap<>();
                        /**
                         * Getting Inner Object from contacts array...
                         * and
                         * From that We will get Name of that Contact
                         *
                         */
                        JSONObject innerObject = array.getJSONObject(jIndex);
                        product_name = innerObject.getString("name");
                        product_number = innerObject.getString("model");
                        product_number = product_number.replaceFirst("^.*?Switch([^)]+).*$","$1");

                        product_number=product_number.replace("(","");
                        mac_address = innerObject.getString("macaddr");
                        serial_number = innerObject.getString("serial");
                        status = innerObject.getString("status");
                        if(product_number == null || product_name == null ||  mac_address == null || serial_number == null || status == null)
                            continue;
                        if (issantorinilist.contains(product_number)) {
                            is_product_match = true;
                            returnList.put("product_image", Integer.toString(swi_img[0]));
                        } else if (islavalist.contains(product_number)) {
                            is_product_match = true;
                            returnList.put("product_image", Integer.toString(swi_img[1]));
                        } else if (isbondlist.contains(product_number)) {
                            is_product_match = true;
                            returnList.put("product_image", Integer.toString(swi_img[2]));
                        } else if (isboltlist.contains(product_number)) {
                            is_product_match = true;
                            returnList.put("product_image", Integer.toString(swi_img[3]));
                        }
                        if(!is_product_match)
                            continue;
                        i++;
                        Log.d(TAG,"product_number"+product_number);
                        returnList.put("mac_address", mac_address);
                        returnList.put("faulttype", "");
                        returnList.put("product_name", product_name);
                        returnList.put("product_number", product_number);
                        returnList.put("status", status);
                        returnList.put("stack_or_not", "");
                        if (issantorinilist.contains(product_number)) {
                            returnList.put("product_image", Integer.toString(swi_img[0]));
                        } else if (islavalist.contains(product_number)) {
                            returnList.put("product_image", Integer.toString(swi_img[1]));
                        } else if (isbondlist.contains(product_number)) {
                            returnList.put("product_image", Integer.toString(swi_img[2]));
                        } else if (isboltlist.contains(product_number)) {
                            returnList.put("product_image", Integer.toString(swi_img[3]));
                        }
                        if(i%2==0)
                            returnList.put("location","woodstock");
                        else
                            returnList.put("location","midtown");
                        returnList.put("serial_number", serial_number);
                        returnList.put("stack_or_not", "");
                        switchList.add(returnList);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return switchList;
    }
    public static String get_token_from_sheet(String rsp) {
        JSONObject records = null ;
        String access_token = null;
        try {
            records = new JSONObject(rsp);

        Log.e("MASTER", "response recevied: " + rsp);
        Log.e("MASTER", "RECORDS recevied: " + records);
        JSONArray access_data = records.getJSONArray("records");

        JSONObject access = access_data.getJSONObject(0);

        Log.e("MASTER", "ACESSS recevied: " + access);
        access_token = access.getString("access_token");

        Log.e("MASTER", "ACCESS TOKEN: " + access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return access_token;
    }
    public static String hw_failures(String rsp) {

        JSONObject messagebody = null;
        try {
            messagebody = new JSONObject(rsp);

        Log.e("MASTER", "JSON Object= " + messagebody);

        //JSONObject events =  messagebody.getJSONObject("events");
        int event_count = messagebody.getInt("count");
        JSONArray events_array = (JSONArray) messagebody.getJSONArray("events");

        //Iterator events = events_array.iterator();
        int arrayLength = events_array.length();
        int jIndex;
        int event_number;
        String event_description;

        for(jIndex =0; jIndex< arrayLength ; jIndex++)
        {
            JSONObject event_obj = events_array.getJSONObject(jIndex);

            event_number = event_obj.getInt("number");

            if( event_number == 2793 || event_number == 2797 || event_number == 2798) {
                event_description = event_obj.getString("description");
                Log.e("MASTER", "EVENT# " + event_number + "Description: " + event_description);
                event_description = event_description.toUpperCase();

                if( event_description.contains("FAILURE")) {
                    Log.e("MASTER", "Found a faulted switches with below details: ");
                    Log.e("MASTER", "EVENT# " + event_number + "Description: " + event_description);
                    return "FAILURE";
                    // Call to write in excel sheet
                } else if( event_description.contains(" OK")) {
                    Log.e("MASTER", "Found a faulted switches with below details: ");
                    Log.e("MASTER", "EVENT# " + event_number + "Description: " + event_description);
                    // Call to excel sheet
                    return "OK";
                }
            }
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

