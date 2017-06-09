package edu.monash.fit3027.fit3027_final_application.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jack on 07-May-17.
 */

public class Item implements Parcelable {

    private String itemID;
    private String itemName;
    private int itemQuantity;
    private Calendar itemExpiryDate;
    private String itemColorTag;
    private int notifyDay;
    private long itemBarcode;
    private String itemType;

    public static final String TABLE_NAME = "item";
    public static final String COLUMN_ID = "itemID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_ITEM_TYPE = "item_type";
    public static final String COLUMN_DATE = "expiry_date";
    public static final String COLUMN_COLOR_TAG = "color_tag";
    public static final String COLUMN_NOTIFY_DAY = "notify_day";
    public static final String COLUMN_BARCODE = "barcode";

    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " TEXT PRIMARY KEY NOT NULL, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_QUANTITY + " INTEGER NOT NULL, "
            + COLUMN_ITEM_TYPE + " TEXT NOT NULL, "
            + COLUMN_DATE + " TEXT NOT NULL, "
            + COLUMN_COLOR_TAG + " TEXT NOT NULL, "
            + COLUMN_NOTIFY_DAY + " INTEGER NOT NULL, "
            + COLUMN_BARCODE + " INTEGER" + ")";

    public Item(String itemID, String itemName, int itemQuantity, String itemType, Calendar itemExpiryDate, String itemColorTag, int notifyDay, long itemBarcode) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemExpiryDate = itemExpiryDate;
        this.itemColorTag = itemColorTag;
        this.notifyDay = notifyDay;
        this.itemBarcode = itemBarcode;
        this.itemType = itemType;
    }

    protected Item(Parcel in) {
        itemID = in.readString();
        itemName = in.readString();
        itemQuantity = in.readInt();
        itemType = in.readString();
        itemExpiryDate = (Calendar) in.readSerializable();
        itemColorTag = in.readString();
        notifyDay = in.readInt();
        itemBarcode = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemID);
        dest.writeString(itemName);
        dest.writeInt(itemQuantity);
        dest.writeString(itemType);
        dest.writeSerializable(itemExpiryDate);
        dest.writeString(itemColorTag);
        dest.writeInt(notifyDay);
        dest.writeLong(itemBarcode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Calendar getItemExpiryDate() {
        return itemExpiryDate;
    }

    public void setItemExpiryDate(Calendar itemExpiryDate) {
        this.itemExpiryDate = itemExpiryDate;
    }

    public String getItemColorTag() {
        return itemColorTag;
    }

    public void setItemColorTag(String itemColorTag) {
        this.itemColorTag = itemColorTag;
    }

    public int getNotifyDay() {
        return notifyDay;
    }

    public void setNotifyDay(int notifyDay) {
        this.notifyDay = notifyDay;
    }


    public long getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(long itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int daysLeftInt() {
        Calendar now = new GregorianCalendar();
        int startDate = now.get(Calendar.DAY_OF_MONTH);
        int endDate = itemExpiryDate.get(Calendar.DAY_OF_MONTH);
        return endDate - startDate;
    }

    public String daysLeftString(){
        int daysLeft = daysLeftInt();
        if (daysLeft < 0){
            return ("Expired");
        }else if (daysLeft == 0){
            return ("Expire Today");
        }else {
            return (daysLeft + " Days Left");
        }
    }
}
