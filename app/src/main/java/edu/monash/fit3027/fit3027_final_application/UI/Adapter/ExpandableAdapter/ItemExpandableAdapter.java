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
 * Created by Jack on 9/6/17.
 */

public class ItemExpandableAdapter extends ExpandableRecyclerAdapter<ColorTag,Item,ColorTagViewHolder, ItemViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    private DatabaseHelper DBHelper;
    public ItemExpandableAdapter(List<ColorTag> groups, Context context) {
        super(groups);
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        DBHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ColorTagViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_color_tag, parentViewGroup, false);
        return new ColorTagViewHolder(view);
    }

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
        childViewHolder.setOnLongClickOnCardView(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are Sure to delete this item?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper.removeItem(child);
                        updateView(new ArrayList<Item>(DBHelper.getAllItem().values()));
                    }
                }).setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }});
        childViewHolder.setOnClickOnCardView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(mContext, ItemDetail.class);
                newIntent.putExtra("item",child);
                ((Activity)mContext).startActivityForResult(newIntent, MainActivity.REQUEST_CONTENT_UPDATE);
            }
        });
    }

    public void updateView(ArrayList<Item> itemArrayList){
        setParentList(DBHelper.createGroups(itemArrayList),true);
    }



}
