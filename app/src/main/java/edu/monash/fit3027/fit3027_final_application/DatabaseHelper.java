package edu.monash.fit3027.fit3027_final_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;


import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by Jack on 07-May-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ItemDB";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Item.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Item.TABLE_NAME); //TODO: Probably need to move user data to the new database
        onCreate(db);
    }

    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Item.COLUMN_ID, item.getItemID());
        values.put(Item.COLUMN_NAME, item.getItemName());
        values.put(Item.COLUMN_QUANTITY, item.getItemQuantity());
        values.put(Item.COLUMN_DATE, encodeDate(item.getItemExpiryDate()));
        values.put(Item.COLUMN_COLOR_TAG, item.getItemColorTag());
        values.put(Item.COLUMN_NOTIFY_DAY, item.getNotifyDay());
        values.put(Item.COLUMN_BARCODE, item.getItemBarcode());
        db.insert(Item.TABLE_NAME, null, values);
        db.close();
    }

    public HashMap<String, Item> getAllItem(){
        HashMap<String, Item> itemHashMap = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Item.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        decodeDate(cursor.getString(3)),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getLong(6));
                itemHashMap.put(item.getItemID(), item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemHashMap;
    }

    public void updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Item.COLUMN_ID, item.getItemID());
        values.put(Item.COLUMN_NAME, item.getItemName());
        values.put(Item.COLUMN_QUANTITY, item.getItemQuantity());
        values.put(Item.COLUMN_DATE, encodeDate(item.getItemExpiryDate()));
        values.put(Item.COLUMN_COLOR_TAG, item.getItemColorTag());
        values.put(Item.COLUMN_NOTIFY_DAY, item.getNotifyDay());
        values.put(Item.COLUMN_BARCODE, item.getItemBarcode());
        db.update(Item.TABLE_NAME, values, Item.COLUMN_ID + " = ?", new String[]{item.getItemID()});
    }

    public void removeItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Item.TABLE_NAME,
                Item.COLUMN_ID + " = ?",
                new String[]{item.getItemID()});
        db.close();
    }

    //TODO: What data type to store the image
    public String encodeDate(Calendar calender) {
        return calender.get(Calendar.YEAR) + "-" + calender.get(Calendar.MONTH) + "-" + calender.get(Calendar.DATE);
    }

    public Calendar decodeDate(String dateString) {
        String[] calendarString = dateString.split("-");
        return new GregorianCalendar(Integer.parseInt(calendarString[0]), Integer.parseInt(calendarString[1]), Integer.parseInt(calendarString[2]));
    }

}
