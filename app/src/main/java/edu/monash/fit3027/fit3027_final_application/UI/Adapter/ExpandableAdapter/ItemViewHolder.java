package edu.monash.fit3027.fit3027_final_application.UI.Adapter.ExpandableAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.monash.fit3027.fit3027_final_application.Helper.DatabaseHelper;
import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.UI.Activity.ItemDetail;
import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by Jack on 9/6/17.
 */

public class ItemViewHolder extends ChildViewHolder {

    public ItemViewHolder(View itemView) {
        super(itemView);
        itemCardView = (CardView) itemView.findViewById(R.id.itemCardView);
        expiryDateTextView = (TextView) itemView.findViewById(R.id.expiryDateTextView);
        itemNameTextView = (TextView) itemView.findViewById(R.id.itemNameTextView);
        itemQuantityTextView = (TextView) itemView.findViewById(R.id.itemQuantityTextView);
        daysLeftTextView = (TextView) itemView.findViewById(R.id.daysLeftTextView);
        itemTypeTextView = (TextView) itemView.findViewById(R.id.itemTypeTextView);
        mContext = itemView.getContext();
    }

    private CardView itemCardView;
    private TextView expiryDateTextView;
    private TextView itemNameTextView;
    private TextView itemQuantityTextView;
    private TextView daysLeftTextView;
    private TextView itemTypeTextView;
    private Item item;
    private Context mContext;
    private DatabaseHelper DBHelper;

    public Item getItem() {
        return item;
    }

    public void onBind(Item item) {
        this.item = item;
        DateFormat displayDayOfWeek = new SimpleDateFormat("EEE", Locale.getDefault());
        expiryDateTextView.setText(item.getItemExpiryDate().get(Calendar.DATE) + "/" + item.getItemExpiryDate().get(Calendar.MONTH) + " " + displayDayOfWeek.format(item.getItemExpiryDate().get(Calendar.DAY_OF_WEEK)));
        itemQuantityTextView.setText(String.valueOf(item.getItemQuantity()));
        itemTypeTextView.setText(item.getItemType());
        int daysLeft = item.daysLeftInt();
        switch (daysLeft) {
            case 0:
            case 1:
                daysLeftTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAboutRot));
                break;
            case 2:
            case 3:
            case 4:
                daysLeftTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorOk));
                break;
            default:
                if (daysLeft < 0) {
                    daysLeftTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorRot));
                } else {
                    daysLeftTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorFresh));
                }
                break;
        }
        daysLeftTextView.setText(item.daysLeftString());
        itemNameTextView.setText(item.getItemName());
    }

    public void setOnLongClickOnCardView(View.OnLongClickListener listener) {
        itemCardView.setOnLongClickListener(listener);
    }

    public void setOnClickOnCardView(View.OnClickListener listener){
        itemCardView.setOnClickListener(listener);
    }

}
