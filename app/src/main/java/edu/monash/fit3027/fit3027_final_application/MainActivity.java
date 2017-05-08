package edu.monash.fit3027.fit3027_final_application;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.UUID;

import edu.monash.fit3027.fit3027_final_application.model.Item;

public class MainActivity extends AppCompatActivity {

    private RecyclerView itemrecycleView;
    private ArrayList<Item>demoItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addItemFab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        itemrecycleView = (RecyclerView)findViewById(R.id.itemRecyclerView);
        itemrecycleView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        itemrecycleView.setLayoutManager(llm);
        ItemAdapter adapter = new ItemAdapter(demoItem);
        itemrecycleView.setAdapter(adapter);

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

    private void initialiseData(){
        demoItem = new ArrayList<>();
        demoItem.add(new Item(UUID.randomUUID().toString(),"Egg",1,new GregorianCalendar(2017,5,17),"#FFFF00",1));
        demoItem.add(new Item(UUID.randomUUID().toString(),"Milk",1,new GregorianCalendar(2017,5,17),"#FFFF00",1));
        demoItem.add(new Item(UUID.randomUUID().toString(),"Bread",1,new GregorianCalendar(2017,5,17),"#FFFF00",1));
    }
}
