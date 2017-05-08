package edu.monash.fit3027.fit3027_final_application;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by Jack on 08-May-17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView itemCardView;
        TextView expiryDateTextView;
        TextView itemNameTextView;
        TextView itemQuantityTextView;
        TextView daysLeftTextView;
        ImageView colorTagImageView;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemCardView = (CardView) itemView.findViewById(R.id.itemCardView);
            expiryDateTextView = (TextView) itemView.findViewById(R.id.expiryDateTextView);
            itemNameTextView = (TextView) itemView.findViewById(R.id.itemNameTextView);
            itemQuantityTextView = (TextView) itemView.findViewById(R.id.itemQuantityTextView);
            daysLeftTextView = (TextView) itemView.findViewById(R.id.daysLeftTextView);
            colorTagImageView = (ImageView) itemView.findViewById(R.id.colorTagImageView);
        }
    }

    private List<Item> itemList;
    private Context mContext;
    private DatabaseHelper DBHelper;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.itemList = itemList;
        this.mContext = context;
        DBHelper = new DatabaseHelper(mContext);
    }


    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(v);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ItemViewHolder holder, final int position) {
        Item item = itemList.get(position);
        DateFormat displayDayOfWeek = new SimpleDateFormat("EEE");
        holder.expiryDateTextView.setText(item.getItemExpiryDate().get(Calendar.DATE) + "/" + item.getItemExpiryDate().get(Calendar.MONTH) + " " + displayDayOfWeek.format(item.getItemExpiryDate().get(Calendar.DAY_OF_WEEK)));
        holder.colorTagImageView.setBackgroundColor(0);
        holder.itemQuantityTextView.setText("x" + item.getItemQuantity());
        holder.daysLeftTextView.setText(item.daysLeft() + " days left");
        holder.itemNameTextView.setText(item.getItemName());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are Sure to delete this item?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper.removeItem(itemList.get(position));
                        try {
                            ArrayList<Item> itemArray = new ArrayList<>(DBHelper.getAllItem().values());
                            updateMonsters(itemArray);
                        } catch (Exception ex) {
                        }
                    }
                }).setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateMonsters(List<Item> items) {
        this.itemList = items;
        notifyDataSetChanged();
    }

}
