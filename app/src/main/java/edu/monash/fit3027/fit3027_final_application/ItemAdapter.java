package edu.monash.fit3027.fit3027_final_application;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by Jack on 08-May-17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        CardView itemCardView;
        TextView expiryDateTextView;
        TextView itemNameTextView;
        TextView itemQuantityTextView;
        TextView daysLeftTextView;
        ImageView colorTagImageView;

        ItemViewHolder(View itemView){
            super(itemView);
            itemCardView = (CardView)itemView.findViewById(R.id.itemCardView);
            expiryDateTextView=(TextView)itemView.findViewById(R.id.expiryDateTextView);
            itemNameTextView = (TextView)itemView.findViewById(R.id.itemNameTextView);
            itemQuantityTextView = (TextView)itemView.findViewById(R.id.itemQuantityTextView);
            daysLeftTextView = (TextView)itemView.findViewById(R.id.daysLeftTextView);
            colorTagImageView = (ImageView)itemView.findViewById(R.id.colorTagImageView);
        }
    }
    private List<Item> itemList;
    public ItemAdapter(List<Item> itemList){
        this.itemList = itemList;
    }

    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item,parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(v);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ItemViewHolder holder, int position) {
        holder.expiryDateTextView.setText("5/6 may");
        holder.colorTagImageView.setBackgroundColor(Color.parseColor(itemList.get(position).getItemColorTag()));
        holder.itemQuantityTextView.setText("1 pcs");
        holder.daysLeftTextView.setText("1 day");
        holder.itemNameTextView.setText(itemList.get(position).getItemName());
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
