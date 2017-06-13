package edu.monash.fit3027.fit3027_final_application.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.model.ColorTag;
import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 * A customised database helper for this application
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ItemDB";
    public static final int DATABASE_VERSION = 1;
    private Context mContext;

    /**
     * Takes the context and construct the database
     * @param context a context of the activity who initialised it
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /**
     * When the database first time boots up, it will run create table statement
     * And populate pre-defined color
     * @param db a database object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Item.CREATE_STATEMENT);
        db.execSQL(ColorTag.CREATE_STATEMENT);
        populateColorTag(db);
    }

    /**
     * Called when the database needs to be upgraded. It will drop the existing table
     * @param db The database
     * @param oldVersion The old database version
     * @param newVersion The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Item.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ColorTag.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Populate the pre-defined database
     * @param db The database
     */
    private void populateColorTag(SQLiteDatabase db){
        Resources res = mContext.getResources();
        String[] colorSelection = res.getStringArray(R.array.colorTagHex);
        for (String colorHex: colorSelection){
            ContentValues values = new ContentValues();
            values.put(ColorTag.COLUMN_ID, colorHex);
            values.put(ColorTag.COLUMN_DESCRIPTION, "");
            db.insert(ColorTag.TABLE_NAME,null,values);
        }
    }

    /**
     * Add the item into the database
     * @param item An Item object
     */
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Item.COLUMN_ID, item.getItemID());
        values.put(Item.COLUMN_NAME, item.getItemName());
        values.put(Item.COLUMN_QUANTITY, item.getItemQuantity());
        values.put(Item.COLUMN_ITEM_TYPE, item.getItemType());
        values.put(Item.COLUMN_DATE, encodeDate(item.getItemExpiryDate()));
        values.put(Item.COLUMN_COLOR_TAG, item.getItemColorTag());
        values.put(Item.COLUMN_NOTIFY_DAY, item.getNotifyDay());
        values.put(Item.COLUMN_BARCODE, item.getItemBarcode());
        db.insert(Item.TABLE_NAME, null, values);
    }

    /**
     * Get all of the item from database
     * @return An arrayList contains all of the Item objects
     */
    public ArrayList<Item> getAllItem(){
        //ArrayList is sufficient, don't need hashmap
        ArrayList<Item> itemArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Item.TABLE_NAME, null);
        // Add each item to arrayList (Each row has 1 item)
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        decodeDate(cursor.getString(4)),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getLong(7));
                itemArrayList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemArrayList;
    }

    /**
     * Get all of the colorTag from database
     * @return An arrayList contains all of the Item objects
     */
    private HashMap<String, String> getAllColorTag(){
        HashMap<String, String> colorTagHashMap = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ColorTag.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                colorTagHashMap.put(cursor.getString(0), cursor.getString(1));//Color hex code and its description, it is used for categorise the item
            } while (cursor.moveToNext());
        }
        cursor.close();
        return colorTagHashMap;
    }

    /**
     * Update a particular color
     * @param colorTagID Color hex code
     * @param colorDescription Color's comment
     */
    public void updateColor(String colorTagID, String colorDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ColorTag.COLUMN_DESCRIPTION, colorDescription);
        int affect = db.update(ColorTag.TABLE_NAME, values, ColorTag.COLUMN_ID + " = ?", new String[]{colorTagID});
        Log.v("I","Updated color: "+String.valueOf(affect)); //Used to test if database has update the colorTag
    }

    /**
     * Update a particular item
     * @param item An item object
     */
    public void updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Item.COLUMN_NAME, item.getItemName());
        values.put(Item.COLUMN_QUANTITY, item.getItemQuantity());
        values.put(Item.COLUMN_ITEM_TYPE,item.getItemType());
        values.put(Item.COLUMN_DATE, encodeDate(item.getItemExpiryDate()));
        values.put(Item.COLUMN_COLOR_TAG, item.getItemColorTag());
        values.put(Item.COLUMN_NOTIFY_DAY, item.getNotifyDay());
        values.put(Item.COLUMN_BARCODE, item.getItemBarcode());
        int affect = db.update(Item.TABLE_NAME, values, Item.COLUMN_ID + " = ?", new String[]{item.getItemID()});
        Log.v("I","Updated item: "+String.valueOf(affect)); //Used to test if database has update the colorTag
    }

    /**
     * Remove an item from the database
     * @param item An item object
     */
    public void removeItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Item.TABLE_NAME,
                Item.COLUMN_ID + " = ?",
                new String[]{item.getItemID()});
    }

    /**
     * A convenient method to decode date object
     * @param calender An calender object
     * @return A String that represents calender
     */
    private String encodeDate(Calendar calender) {
        return calender.get(Calendar.YEAR) + "-" + calender.get(Calendar.MONTH) + "-" + calender.get(Calendar.DATE);
    }

    /**
     * A convenient method to encode date object
     * @param dateString A String that represents calender
     * @return An calender object
     */
    private Calendar decodeDate(String dateString) {
        String[] calendarString = dateString.split("-");
        return new GregorianCalendar(Integer.parseInt(calendarString[0]), Integer.parseInt(calendarString[1]), Integer.parseInt(calendarString[2]));
    }

    /**
     * An method for creating expandable adapter
     * @param itemArray An arrayList contains item objects
     * @return A list contains colorTag objects
     *
     * Each colorTags that only contain item/s object will be added to the list
     */
    public List<ColorTag> createGroups(ArrayList<Item> itemArray) {
        HashMap<String, List<Item>> colorItemHashMap = new HashMap<>();
        //Construct a hash map of ColorTag hex -> ArrayList {item, item, ...}
        for (Item item : itemArray) {
            if (!colorItemHashMap.containsKey(item.getItemColorTag())) {
                List<Item> itemList = new ArrayList<>();
                itemList.add(item);
                colorItemHashMap.put(item.getItemColorTag(), itemList);
            } else {
                colorItemHashMap.get(item.getItemColorTag()).add(item);
            }
        }

        HashMap<String, String> colorTagHashMap = this.getAllColorTag();

        List<ColorTag> colorTagWithItems = new ArrayList<>();

        Iterator<String> keySetIterator = colorItemHashMap.keySet().iterator();
        while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            ColorTag colorTag = new ColorTag(key,colorItemHashMap.get(key),colorTagHashMap.get(key));//Get the color hex, item associated with this color, and that color tag's description
            colorTagWithItems.add(colorTag);
        }
        return colorTagWithItems;
    }

}
