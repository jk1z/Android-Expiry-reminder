package edu.monash.fit3027.fit3027_final_application.UI.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import edu.monash.fit3027.fit3027_final_application.Helper.DatabaseHelper;
import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.UI.Adapter.ExpandableAdapter.ItemExpandableAdapter;
import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 * Activity controller for activity_main
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView itemRecycleView;
    private FloatingActionButton addItemFab;
    private FloatingActionButton addThroughCameraFab;

    private DatabaseHelper DBHelper;
    private ItemExpandableAdapter adapter;
    public final static int REQUEST_CONTENT_UPDATE = 1;

    /**
     * When the activity has been created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab all of the information from the interface
        addItemFab = (FloatingActionButton) findViewById(R.id.addItemFab);
        addThroughCameraFab = (FloatingActionButton) findViewById(R.id.addThroughCameraFab);
        itemRecycleView = (RecyclerView) findViewById(R.id.itemRecyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //Set up toolbar

        // Initialise database helper
        DBHelper = new DatabaseHelper(getApplicationContext());

        // Initialise recycler view
        itemRecycleView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);// Giving it layout
        itemRecycleView.setLayoutManager(llm);
        adapter = new ItemExpandableAdapter(DBHelper.createGroups(DBHelper.getAllItem()), this);


        itemRecycleView.setAdapter(adapter);
        //Setting button connection to the floating button
        addItemFab.setOnClickListener(this);
        addThroughCameraFab.setOnClickListener(this);
    }

    /**
     * When user come back from the notification, recycler view should update itself
     */
    protected void onResume() {
        super.onResume();
        adapter.updateView(DBHelper.getAllItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This initialise the option menu
     * @return Display the options menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent newIntent = new Intent(this, SettingPage.class);
            startActivity(newIntent);
            return true;
        } else if (id == R.id.action_about) {
            Intent newIntent = new Intent(this, AboutPage.class);
            startActivity(newIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent newIntent;
        switch (v.getId()) {
            case R.id.addItemFab: //When user clicked plus button
                newIntent = new Intent(this, ItemDetail.class);
                startActivityForResult(newIntent, REQUEST_CONTENT_UPDATE);
                break;
            case R.id.addThroughCameraFab://When user clicked camera button
                newIntent = new Intent(this, ItemDetail.class);
                newIntent.putExtra("addThroughCamera", true);
                startActivityForResult(newIntent, REQUEST_CONTENT_UPDATE);
                break;
            default:
                break;
        }
    }
    /**
     * Handling the result from other view and update the list view accrodingly
     * @param requestCode: What action it request to do
     * @param resultCode: What result it send back
     * @param data data inside of intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTENT_UPDATE) {
            if (resultCode == RESULT_OK) { //Update the view if database has been updated
                adapter.updateView(DBHelper.getAllItem()); //TODO: Still try to find a efficient way to update a particular view
            }
        }
    }


}
