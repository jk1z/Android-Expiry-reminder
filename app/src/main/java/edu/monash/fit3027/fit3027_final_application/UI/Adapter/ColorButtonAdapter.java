package edu.monash.fit3027.fit3027_final_application.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import edu.monash.fit3027.fit3027_final_application.R;

/**
 * Created by Jack on 06-Jun-17.
 */

public class ColorButtonAdapter extends BaseAdapter {
    private Context mContext;
    private String[] colorHexArray;
    public ColorButtonAdapter(Context context, String[] colorHexArray){
        this.mContext = context;
        this.colorHexArray = colorHexArray;
    }
    @Override
    public int getCount() {
        return this.colorHexArray.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(270, 270));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setBackgroundColor(Color.parseColor(colorHexArray[position]));
        return imageView;
    }
}
