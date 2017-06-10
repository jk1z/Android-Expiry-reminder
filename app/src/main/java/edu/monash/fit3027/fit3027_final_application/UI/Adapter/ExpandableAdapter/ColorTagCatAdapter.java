package edu.monash.fit3027.fit3027_final_application.UI.Adapter.ExpandableAdapter;

import android.content.Context;
import android.content.DialogInterface;
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
import edu.monash.fit3027.fit3027_final_application.model.ColorTag;
import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by Jack on 9/6/17.
 */

public class ColorTagCatAdapter extends ExpandableRecyclerAdapter<ColorTag,Item,ColorTagViewHolder, ItemViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private DatabaseHelper DBHelper;
    public ColorTagCatAdapter(List<ColorTag> groups, Context context) {
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
    }

    public void updateView(ArrayList<Item> itemArrayList){
        setParentList(createGroups(itemArrayList),true);
    }

    private List<ColorTag> createGroups(ArrayList<Item> itemArray) {
        HashMap<String, List<Item>> colorItemHashMap = new HashMap<>();
        for (Item item : itemArray) {
            if (!colorItemHashMap.containsKey(item.getItemColorTag())) {
                List<Item> itemList = new ArrayList<>();
                itemList.add(item);
                colorItemHashMap.put(item.getItemColorTag(), itemList);
            } else {
                colorItemHashMap.get(item.getItemColorTag()).add(item);
            }
        }

        HashMap<String, String> colorTagHashMap = DBHelper.getAllColorTag();

        List<ColorTag> colorTagWithItems = new ArrayList<>();

        Iterator<String> keySetIterator = colorItemHashMap.keySet().iterator();
        while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            ColorTag colorTag = new ColorTag(key,colorItemHashMap.get(key),colorTagHashMap.get(key));
            colorTagWithItems.add(colorTag);
        }
        return colorTagWithItems;
    }


}
