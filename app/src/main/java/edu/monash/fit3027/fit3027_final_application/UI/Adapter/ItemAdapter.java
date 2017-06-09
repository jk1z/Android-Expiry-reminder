package edu.monash.fit3027.fit3027_final_application.UI.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.monash.fit3027.fit3027_final_application.Helper.DatabaseHelper;
import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by Jack on 08-May-17.
 */

public class ItemAdapter extends RecyclerView.Adapter {

    private List<Item> itemList;
    private Context mContext;
    private DatabaseHelper DBHelper;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.itemList = itemList;
        this.mContext = context;
        DBHelper = new DatabaseHelper(mContext);
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View vBanner = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_banner, parent, false);
                return new ItemViewHolder(vBanner);
            default:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item, parent, false);
                return new ItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                break;
            case 1:
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.bindViewHolder(this.itemList.get(position - 1),mContext);
                itemViewHolder.setOnLongClickOnCardView(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Are Sure to delete this item?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper.removeItem(itemViewHolder.getItem());
                                updateMonsters();
                            }
                        }).setNegativeButton("Cancel", null);
                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;
                    }
                });
                break;
            default:
                break;
        }
    }


    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return itemList.size() + 1;
    }

    public void updateMonsters() {
        this.itemList = new ArrayList<>(DBHelper.getAllItem().values());
        notifyDataSetChanged();
    }

}
