package edu.monash.fit3027.fit3027_final_application.UI.Adapter.ExpandableAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.monash.fit3027.fit3027_final_application.Helper.DatabaseHelper;
import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.UI.Activity.ItemDetail;
import edu.monash.fit3027.fit3027_final_application.UI.Activity.MainActivity;
import edu.monash.fit3027.fit3027_final_application.model.ColorTag;
import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 * Adapter for recycler view
 */

public class ItemExpandableAdapter extends ExpandableRecyclerAdapter<ColorTag,Item,ColorTagViewHolder, ItemViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private DatabaseHelper DBHelper;

    /**
     * Construct a expandableAdapter
     * @param groups A list that contains ColorTag Objects
     * @param context Application context
     */
    public ItemExpandableAdapter(List<ColorTag> groups, Context context) {
        super(groups);
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        DBHelper = new DatabaseHelper(context);
    }

    /**
     * Create parent view holder
     */
    @NonNull
    @Override
    public ColorTagViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_color_tag, parentViewGroup, false);
        return new ColorTagViewHolder(view);
    }

    /**
     * Create child view holder
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_item, childViewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull ColorTagViewHolder parentViewHolder, int parentPosition, @NonNull ColorTag parent) {
        parentViewHolder.onBind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull ItemViewHolder childViewHolder, final int parentPosition, final int childPosition, @NonNull final Item child) {
        childViewHolder.onBind(child);
        //We need the activity context, that's why I use anonymous function
        childViewHolder.setOnLongClickOnCardView(new View.OnLongClickListener() {
            public boolean onLongClick(View v) { //Delete the item
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are Sure to delete this item?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper.removeItem(child);
                        updateView(DBHelper.getAllItem());
                    }
                }).setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }});
        childViewHolder.setOnClickOnCardView(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Edit the item
                Intent newIntent = new Intent(mContext, ItemDetail.class);
                newIntent.putExtra("item",child);
                ((Activity)mContext).startActivityForResult(newIntent, MainActivity.REQUEST_CONTENT_UPDATE);
            }
        });
    }


    public void updateView(ArrayList<Item> itemArrayList){
        setParentList(DBHelper.createGroups(itemArrayList),true);//A convenient function to update the view
        //NOTE notifyDataSetChange does not work
    }



}
