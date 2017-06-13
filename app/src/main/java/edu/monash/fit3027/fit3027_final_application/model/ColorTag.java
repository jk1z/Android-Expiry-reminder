package edu.monash.fit3027.fit3027_final_application.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 *
 * A class that represents ColorTag
 * A key component to the expandable recycler list
 */

public class ColorTag implements Parent<Item> {
    private String colorHex;
    private String description;
    private List<Item>items;

    //DataBase Constants
    public static final String TABLE_NAME = "colorTag";
    public static final String COLUMN_ID = "colorHex";
    public static final String COLUMN_DESCRIPTION = "description";

    //Table create statement
    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " TEXT PRIMARY KEY NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT NOT NULL" + ")";

    /**
     * Construct a colorTag object
     * @param colorHex colorTag's ID should be unique
     * @param items A list that contains all of the item that is this color
     * @param description A description of the colorTag
     */
    public ColorTag(String colorHex, List<Item> items, String description){
        this.colorHex = colorHex;
        this.description = description;
        this.items = items;
    }

    /**
     * Getters for color hex
     * @return A String starts with #xxxxx
     */
    public String getColorHex() {
        return colorHex;
    }

    /**
     * Getters for colorTag's description
     * @return A string for colorTag's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the list of this parent's child items.
     * <p>
     * If list is empty, the parent has no children.
     *
     * @return An List with items
     */
    @Override
    public List<Item> getChildList() {
        return this.items;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
