package com.vuforia.samples.OnSwap;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import com.vuforia.samples.VuforiaSamples.R;

/**
 * Simple POJO model for example
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Item {

    private int switch_img;
    private String pledgePrice;
    private String switch_name;
    private String mac_address;
    private int requestsCount;
    private String date;
    private String jltype;
    private String faulttype;
    private String status;
    private String location;


    private View.OnClickListener requestBtnClickListener;
    private View.OnClickListener view3dBtnClickListener;
    private View.OnClickListener DefaultspecClickListener;

   public Item(int switch_img, String pledgePrice, String switch_name, String mac_address, int requestsCount, String date, String jltype , String faulttype, String status,String location){
        this.switch_img = switch_img;
        this.pledgePrice = pledgePrice;
        this.switch_name = switch_name;
        this.mac_address = mac_address;
        this.requestsCount = requestsCount;
        this.date = date;
        this.jltype = jltype;
        this.faulttype = faulttype;
        this.status = status;
        this.location = location;
    }

    public int getSwitch_img() {
        return switch_img;
    }

    public void setSwitch_img(int switch_img) {
        this.switch_img = switch_img;
    }

    public String getPledgePrice() {
        return pledgePrice;
    }

    public void setPledgePrice(String pledgePrice) {
        this.pledgePrice = pledgePrice;
    }

    public String getSwitch_name() {
        return switch_name;
    }

    public void setswitch_name(String switch_name) {
        this.switch_name = switch_name;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address =  mac_address;
    }

    public int getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(int requestsCount) {
        this.requestsCount = requestsCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJltype() {
        return jltype;
    }

    public void setJltype(String jltype) {
        this.jltype = jltype;
    }

    public String getFaulttype() {
        return faulttype;
    }
    public void setFaulttype(String faulttype) {
        this.faulttype = faulttype;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        this.location = location;
    }
    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }
    public View.OnClickListener getDefaultspecClickListener() {
        return DefaultspecClickListener;
    }
    public View.OnClickListener getDefaultview3dBtnClickListener() {
        return view3dBtnClickListener;
    }
    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
    public void setviewd3button(View.OnClickListener viewd3button) {
        this.view3dBtnClickListener = viewd3button;
    }
    public void setspecbutton(View.OnClickListener specbutton) {
        this.DefaultspecClickListener =DefaultspecClickListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (requestsCount != item.requestsCount) return false;
      //  if (price != null ? !price.equals(item.price) : item.price != null) return false;
        if (pledgePrice != null ? !pledgePrice.equals(item.pledgePrice) : item.pledgePrice != null)
            return false;
        if (switch_name != null ? !switch_name.equals(item.switch_name) : item.switch_name != null)
            return false;
        if (mac_address != null ? !mac_address.equals(item.mac_address) : item.mac_address != null)
            return false;
        if (date != null ? !date.equals(item.date) : item.date != null) return false;
        return !(jltype != null ? !jltype.equals(item.jltype) : item.jltype != null);

    }

    @Override
    public int hashCode() {
        int result = 0;
                //price != null ? price.hashCode() : 0;
        result = 31 * result + (pledgePrice != null ? pledgePrice.hashCode() : 0);
        result = 31 * result + (switch_name != null ? switch_name.hashCode() : 0);
        result = 31 * result + (mac_address != null ? mac_address.hashCode() : 0);
        result = 31 * result + requestsCount;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (jltype != null ? jltype.hashCode() : 0);
        return result;
    }

    /**
     * @return List of elements prepared for tests
     */
    public static ArrayList<Item> getTestingList() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(R.drawable.swi_3810m, "6 mins", "ARUBA 3810M 24G 1-SLOT SWITCH", "d06726-87c3c0", 0, "Aruba HPE 3810M", "JL074A", "fan_fault","open","woodstock"));
        items.add(new Item(R.drawable.swi_5400, "4 mins", "ARUBA 5406R ZL2 SWITCH", "d06726-9442c0", 8, "Aruba HPE 5400R", "J9821A", "fan_fault","open","woodstock"));
        items.add(new Item(R.drawable.swi_3810m, "8 mins", "ARUBA 3810M 48G 1-SLOT SWITCH", "ecebb8-66aa80", 0, "Aruba HPE 3810M", "JL073A","fan_fault","open","woodstock"));
        items.add(new Item(R.drawable.swi_2930f, "4 mins", "ARUBA 2930F 24G POE+ 4SFP SWITCH ", "3ca82a-2cc400", 3, "Aruba HPE 2930F", "JL257A","fan_fault","open","woodstock"));
        items.add(new Item(R.drawable.swi_2930m, "5 mins", "ARUBA 2930M 48G POE+ 1-SLOT", "3ca82a-418900", 10, "Aruba HPE 2930M", "JL322A","fan_fault","open","midtown"));
        items.add(new Item(R.drawable.swi_3810m, "6 mins", "ARUBA 3810M 24G 1-SLOT SWITCH", "d06726-87c3c0", 0, "Aruba HPE 3810M", "JL073A","fan_fault","open","midtown"));
        return items;
    }

}
