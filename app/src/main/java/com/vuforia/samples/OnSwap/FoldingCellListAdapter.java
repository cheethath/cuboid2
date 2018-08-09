package com.vuforia.samples.OnSwap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
//import com.ramotion.foldingcell.examples.R;

import java.util.HashSet;
import java.util.List;

import com.vuforia.samples.VuforiaSamples.R;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<Item> {
    private String TAG = "OnSwap: "+FoldingCellListAdapter.class.getSimpleName();
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private View.OnClickListener Defaultview3dBtnClickListener;
    private View.OnClickListener DefaultspecClickListener;
    private ViewHolder viewHolder;
    public FoldingCellListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;

        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.status_view = cell.findViewById(R.id.status_view);
            viewHolder.switch_img = cell.findViewById(R.id.switch_imgView);
            viewHolder.jltype = cell.findViewById(R.id.title_jltype_label);
            viewHolder.head_image = cell.findViewById(R.id.head_image);
            viewHolder.swi_num =  cell.findViewById(R.id.swi_num);
            viewHolder.content_name_view = cell.findViewById(R.id.content_name_view);
            viewHolder.content_to_address_1 = cell.findViewById(R.id.content_to_address_1);
            viewHolder.switch_name = cell.findViewById(R.id.title_switch_name);
            viewHolder.mac_address = cell.findViewById(R.id.title_mac_address);
            viewHolder.requestsCount = cell.findViewById(R.id.title_requests_count);
            viewHolder.head_image_left_text = cell.findViewById(R.id.head_image_left_text);
            viewHolder.head_image_center_text = cell.findViewById(R.id.head_image_center_text);
            viewHolder.pledgePrice = cell.findViewById(R.id.title_pledge);
            viewHolder.contentRequestBtn = cell.findViewById(R.id.content_request_btn);
            viewHolder.status_img = cell.findViewById(R.id.status);
            viewHolder.head_image_right_text = cell.findViewById(R.id.head_image_right_text);
            viewHolder.title_weight = cell.findViewById(R.id.title_weight);
            viewHolder.content_to_address_1.setTag(position);
            viewHolder.contentRequestBtn.setTag(position);
            viewHolder.content_name_view.setTag(position);

            cell.setTag(viewHolder);

        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        if (!item.getFaulttype().isEmpty()) {
             viewHolder.status_view.setBackgroundColor(Color.RED);
        } else {
             viewHolder.status_view.setBackgroundColor(Color.argb(255, 89, 70, 145));
        }
        // bind data from selected element to view through view holder
        viewHolder.switch_img.setImageResource(item.getSwitch_img());
        viewHolder.head_image.setImageResource(item.getSwitch_img());
        //viewHolder.price.setText(item.getPrice());
        viewHolder.jltype.setText(item.getJltype());

        String swi_family = "Explore the Switch in 3d view";

        viewHolder.content_to_address_1.setText(swi_family);
        viewHolder.swi_num.setText(item.getJltype());
        viewHolder.head_image_right_text.setText(item.getLocation());
        viewHolder.title_weight.setText(item.getLocation());
        viewHolder.content_name_view.setText(item.getSwitch_name());
        viewHolder.switch_name.setText(item.getSwitch_name());
        viewHolder.mac_address.setText(item.getMac_address());
        String mac = item.getMac_address();
        viewHolder.requestsCount.setText(mac);
        viewHolder.head_image_left_text.setText(mac);
        viewHolder.head_image_center_text.setText(item.getPledgePrice());
        viewHolder.pledgePrice.setText(item.getPledgePrice());
     //   if(Homescreen.User_name.contains("oper"))
           viewHolder.contentRequestBtn.setText("Menu");
      //  else
      //      viewHolder.contentRequestBtn.setText("Send");
        // set custom btn handler for list item from that item
        if(item.getStatus().contains("Up")) {
            viewHolder.status_img.setImageResource(R.drawable.on_perfect);
        }else {
            viewHolder.status_img.setImageResource(R.drawable.off_perfect);
        }

        if (item.getRequestBtnClickListener() != null) {

            viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
            viewHolder.content_to_address_1.setOnClickListener(item.getDefaultview3dBtnClickListener());
            viewHolder.content_name_view.setOnClickListener(item.getDefaultspecClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
            viewHolder.content_to_address_1.setOnClickListener(Defaultview3dBtnClickListener);
            viewHolder.content_name_view.setOnClickListener(DefaultspecClickListener);
        }

        return cell;
    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position)) {
            registerFold(position);
            Log.d(TAG,"Item Position :"+position);
        }
        else {
            registerUnfold(position);
            Log.d(TAG,"Item Position :"+position);
        }
    }
    public int getPositionview(int position) {
        return position;
    }
    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }
    public View.OnClickListener getDefaultview3dBtnClickListener() {
        return Defaultview3dBtnClickListener;
    }
    public View.OnClickListener getDefaultspecClickListener() {
        return DefaultspecClickListener;
    }
    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }
    public void setDefaultview3dBtnClickListener(View.OnClickListener Defaultview3dBtnClickListener) {
        this.Defaultview3dBtnClickListener = Defaultview3dBtnClickListener;
    }
    public void setDefaultspecClickListener(View.OnClickListener DefaultspecClickListener) {
        this.DefaultspecClickListener = DefaultspecClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        RelativeLayout head_layout;
        ImageView switch_img;
        TextView contentRequestBtn;
        TextView pledgePrice;
        TextView switch_name;
        TextView mac_address;
        TextView requestsCount;
        TextView head_image_left_text;
        ImageView head_image;
        TextView swi_num;
        TextView content_to_address_1;
        TextView content_name_view;
        ImageView viewd3;
        TextView jltype;
        TextView head_image_center_text;
        RelativeLayout status_view;
        ImageView status_img;
        TextView head_image_right_text;
        TextView title_weight;
    }
}
