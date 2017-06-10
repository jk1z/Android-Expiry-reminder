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
 * Created by Jack on 06-Jun-17.
 */

public class ColorSelectionGridView extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String[] colorSelection;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_color);

        setTitle("Select Color");

        //Setup back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Resources res = getResources();
        colorSelection = res.getStringArray(R.array.colorTagHex);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ColorButtonAdapter(this,colorSelection));
        gridview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent newIntent = new Intent();
        newIntent.putExtra("colorTag",colorSelection[position]);
        setResult(RESULT_OK, newIntent);
        finish();
    }
}
