package edu.monash.fit3027.fit3027_final_application.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by Jack on 9/6/17.
 */

public class ColorTag implements Parent<Item> {
    private String colorHex;
    private String description;
    private List<Item>items;

    public static final String TABLE_NAME = "colorTag";
    public static final String COLUMN_ID = "colorHex";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " TEXT PRIMARY KEY NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT NOT NULL" + ")";

    public ColorTag(String colorHex, List<Item> items, String description){
        this.colorHex = colorHex;
        this.description = description;
        this.items = items;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<Item> getChildList() {
        return this.items;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
