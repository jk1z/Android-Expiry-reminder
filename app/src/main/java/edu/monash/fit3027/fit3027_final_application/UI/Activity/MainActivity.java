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
import android.widget.Toast;

import java.util.ArrayList;

import edu.monash.fit3027.fit3027_final_application.Helper.DatabaseHelper;
import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.UI.Adapter.ItemAdapter;
import edu.monash.fit3027.fit3027_final_application.model.Item;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView itemRecycleView;
    private FloatingActionButton addItemFab;
    private FloatingActionButton addThroughCameraFab;
    private ArrayList<Item> itemArray;
    private DatabaseHelper DBHelper;
    private ItemAdapter adapter;
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
        try {
            itemArray = new ArrayList<>(DBHelper.getAllItem().values());
        } catch (Exception ex) {
            Toast errorMessage = Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT);
            errorMessage.show();
        }
        adapter = new ItemAdapter(this, itemArray);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialiseData() {
        //demoItem = new ArrayList<>();
        /*demoItem.add(new Item(UUID.randomUUID().toString(),"Egg",1,new GregorianCalendar(2017,5,17),"#FFFF00",1));
        demoItem.add(new Item(UUID.randomUUID().toString(),"Milk",1,new GregorianCalendar(2017,5,17),"#99CC66",1));
        demoItem.add(new Item(UUID.randomUUID().toString(),"Bread",1,new GregorianCalendar(2017,5,17),"#CC9999",1));
        demoItem.add(new Item(UUID.randomUUID().toString(),"Bacon",1,new GregorianCalendar(2017,5,17),"#CC9999",1));
        demoItem.add(new Item(UUID.randomUUID().toString(),"Apple",1,new GregorianCalendar(2017,5,17),"#CC9999",1));
        demoItem.add(new Item(UUID.randomUUID().toString(),"Orange",1,new GregorianCalendar(2017,5,17),"#CC9999",1));
        demoItem.add(new Item(UUID.randomUUID().toString(),"Butter",1,new GregorianCalendar(2017,5,17),"#CC9999",1));*/
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
                newIntent.putExtra("addThroughCamera",true);
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
                    adapter.updateMonsters();
                }
            }
        }
    }


}
