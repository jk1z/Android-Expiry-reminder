package edu.monash.fit3027.fit3027_final_application.UI.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.UI.Adapter.ColorButtonAdapter;


/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 * Activity controller for select_color
 */

public class ColorSelectionGridView extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String[] colorSelection;

    /**
     * When the activity has been created
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_color);

        setTitle("Select Color"); //Change the title to select color

        //Setup back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Get pre-entered color hex code from String.xml
        Resources res = getResources();
        colorSelection = res.getStringArray(R.array.colorTagHex);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ColorButtonAdapter(this,colorSelection));//The order of color hex code shouldn't change
        gridview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent newIntent = new Intent();
        newIntent.putExtra("colorTag",colorSelection[position]);//Get which position it clicked
        setResult(RESULT_OK, newIntent);
        finish();
    }
}
