package edu.monash.fit3027.fit3027_final_application.UI.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Jack on 06-Jun-17.
 */

public class colorButtonAdapter extends BaseAdapter {
    private Context mContext;
    private String[] colorHexArray;
    public colorButtonAdapter(Context context,String[] colorHexArray){
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
        return null;
    }
}
