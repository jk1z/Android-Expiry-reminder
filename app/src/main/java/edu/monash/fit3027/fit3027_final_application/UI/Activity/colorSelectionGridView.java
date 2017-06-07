package edu.monash.fit3027.fit3027_final_application.UI.Activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.UI.Adapter.colorButtonAdapter;

/**
 * Created by Jack on 06-Jun-17.
 */

public class colorSelectionGridView extends AppCompatActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_color);

        Resources res = getResources();
        String[] colorSelection = res.getStringArray(R.array.colorTagHex);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new colorButtonAdapter(this,colorSelection));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(colorSelectionGridView.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
