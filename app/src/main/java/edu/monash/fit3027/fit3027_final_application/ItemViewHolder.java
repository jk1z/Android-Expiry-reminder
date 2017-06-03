package edu.monash.fit3027.fit3027_final_application;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by Jack on 03-Jun-17.
 */

public class ItemViewHolder extends ViewHolder{

    private CardView itemCardView;
    private TextView expiryDateTextView;
    private TextView itemNameTextView;
    private TextView itemQuantityTextView;
    private TextView daysLeftTextView;
    private ImageView colorTagImageView;

    public Item getItem() {
        return item;
    }

    private Item item;

    public ItemViewHolder(View itemView) {
        super(itemView);
        itemCardView = (CardView) itemView.findViewById(R.id.itemCardView);
        expiryDateTextView = (TextView) itemView.findViewById(R.id.expiryDateTextView);
        itemNameTextView = (TextView) itemView.findViewById(R.id.itemNameTextView);
        itemQuantityTextView = (TextView) itemView.findViewById(R.id.itemQuantityTextView);
        daysLeftTextView = (TextView) itemView.findViewById(R.id.daysLeftTextView);
        colorTagImageView = (ImageView) itemView.findViewById(R.id.colorTagImageView);
    }

    public void bindViewHolder(Item item){
                this.item = item;
                DateFormat displayDayOfWeek = new SimpleDateFormat("EEE");
                expiryDateTextView.setText(item.getItemExpiryDate().get(Calendar.DATE) + "/" + item.getItemExpiryDate().get(Calendar.MONTH) + " " + displayDayOfWeek.format(item.getItemExpiryDate().get(Calendar.DAY_OF_WEEK)));
                colorTagImageView.setBackgroundColor(Color.parseColor(item.getItemColorTag()));
                itemQuantityTextView.setText("x" + item.getItemQuantity());
                daysLeftTextView.setText(item.daysLeft() + " days left");
                itemNameTextView.setText(item.getItemName());
    }

    public void setOnLongClickOnCardView(View.OnLongClickListener listener){
        itemCardView.setOnLongClickListener(listener);
    }

}
