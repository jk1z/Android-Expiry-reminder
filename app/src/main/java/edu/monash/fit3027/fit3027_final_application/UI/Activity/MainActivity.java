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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.monash.fit3027.fit3027_final_application.Helper.DatabaseHelper;
import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.UI.Adapter.ExpandableAdapter.ColorTagCatAdapter;
import edu.monash.fit3027.fit3027_final_application.model.ColorTag;
import edu.monash.fit3027.fit3027_final_application.model.Item;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView itemRecycleView;
    private FloatingActionButton addItemFab;
    private FloatingActionButton addThroughCameraFab;
    private ArrayList<Item> itemArray;
    private DatabaseHelper DBHelper;
    //private ItemAdapter adapter;
    private ColorTagCatAdapter adapter;
    public final static int REQUEST_CONTENT_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper = new DatabaseHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        addItemFab = (FloatingActionButton) findViewById(R.id.addItemFab);
        addThroughCameraFab = (FloatingActionButton) findViewById(R.id.addThroughCameraFab);
        itemRecycleView = (RecyclerView) findViewById(R.id.itemRecyclerView);

        setSupportActionBar(toolbar);

        itemRecycleView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        itemRecycleView.setLayoutManager(llm);
        itemArray = new ArrayList<>(DBHelper.getAllItem().values());


        List colorGroup = createGroups(itemArray);
        adapter = new ColorTagCatAdapter(colorGroup,this);

        //adapter = new ItemAdapter(this, itemArray);
        itemRecycleView.setAdapter(adapter);
        addItemFab.setOnClickListener(this);
        addThroughCameraFab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        Intent newIntent;
        switch (v.getId()) {
            case R.id.addItemFab:
                newIntent = new Intent(this, ItemDetail.class);
                startActivityForResult(newIntent, REQUEST_CONTENT_UPDATE);
                break;
            case R.id.addThroughCameraFab:
                newIntent = new Intent(this, ItemDetail.class);
                newIntent.putExtra("addThroughCamera", true);
                startActivityForResult(newIntent, REQUEST_CONTENT_UPDATE);
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTENT_UPDATE) {
            if (resultCode == RESULT_OK) {
                if (DBHelper.getAllItem().size() != itemArray.size()) {
                    adapter.updateView(new ArrayList<>(DBHelper.getAllItem().values()));
                }
            }
        }
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
