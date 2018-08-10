package com.vuforia.samples.OnSwap;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import com.vuforia.samples.VuforiaSamples.R;

public class Operator_Custom_Adapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView select_icon;
        TextView secondLine;
        TextView firstLine;
    }



    public Operator_Custom_Adapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.operator_row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }


    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;
        switch (v.getId())
        {
            case R.id.firstLine:
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.operator_row_item, parent, false);
            viewHolder.select_icon = (ImageView) convertView.findViewById(R.id.select_icon);
            viewHolder.secondLine = (TextView) convertView.findViewById(R.id.secondLine);
            viewHolder.firstLine = (TextView) convertView.findViewById(R.id.firstLine);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        viewHolder.select_icon.setImageResource(dataModel.getImage());
        viewHolder.firstLine.setText(dataModel.getFirstLine());
        viewHolder.secondLine.setText(dataModel.getSecondLine());
        if(!Operator_fix_order.scanned_data.isEmpty() && (position == 1)) {
            viewHolder.select_icon.setImageResource(R.drawable.done);
        }
        viewHolder.firstLine.setTag(position);
        viewHolder.firstLine.setOnClickListener(this);
        // Return the completed view to render on screen
        return convertView;
    }


}
