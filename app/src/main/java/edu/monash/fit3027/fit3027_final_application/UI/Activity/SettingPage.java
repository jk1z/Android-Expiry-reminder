package edu.monash.fit3027.fit3027_final_application.UI.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import edu.monash.fit3027.fit3027_final_application.R;

/**
 * Created by Jack on 10-Jun-17.
 */

public class SettingPage extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,AdapterView.OnItemSelectedListener{
    private CheckBox onTheDayCheckBox;
    private Spinner whenToNotifyspinner;
    private SharedPreferences sharedPref;
    public final static int DEFAULT_NOTIFY_ON = 1;
    public final static int DEFAULT_NOTIFY_OFF = 0;
    public final static int DEFAULT_NOTIFY_TIME_MORNING = 0;
    public final static int DEFAULT_NOTIFY_TIME_EVENING = 1;
    public final static String DEFAULT_NOTIFY_SETTING = "DefaultNotificationPref";
    public final static String DEFAULT_NOTIFY_TIME_SETTING = "DefaultNotificationTimePref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);

        //Assume all set to default
        onTheDayCheckBox = (CheckBox) findViewById(R.id.onTheDayCheckBox);
        whenToNotifyspinner = (Spinner) findViewById(R.id.whenToNotifyspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminder_default_choice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        whenToNotifyspinner.setAdapter(adapter);

        sharedPref = getSharedPreferences("Settings",Context.MODE_PRIVATE);
        switch (sharedPref.getInt(DEFAULT_NOTIFY_SETTING,DEFAULT_NOTIFY_ON)){
            case DEFAULT_NOTIFY_ON:
                onTheDayCheckBox.setChecked(true);
                break;
            case DEFAULT_NOTIFY_OFF:
                onTheDayCheckBox.setChecked(false);
                break;
        }

        switch (sharedPref.getInt(DEFAULT_NOTIFY_TIME_SETTING,DEFAULT_NOTIFY_TIME_MORNING )){
            case DEFAULT_NOTIFY_TIME_MORNING:
                whenToNotifyspinner.setSelection(0);
                break;
            case DEFAULT_NOTIFY_TIME_EVENING:
                whenToNotifyspinner.setSelection(1);
                break;
        }

        onTheDayCheckBox.setOnCheckedChangeListener(this);
        whenToNotifyspinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPref.edit();
        if (isChecked){
            editor.putInt(DEFAULT_NOTIFY_SETTING,DEFAULT_NOTIFY_ON);
        }else{
            editor.putInt(DEFAULT_NOTIFY_SETTING,DEFAULT_NOTIFY_OFF);
        }
        editor.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor editor = sharedPref.edit();
        if (position == 0){
            editor.putInt(DEFAULT_NOTIFY_TIME_SETTING,DEFAULT_NOTIFY_TIME_MORNING);
        }else{
            editor.putInt(DEFAULT_NOTIFY_TIME_SETTING,DEFAULT_NOTIFY_TIME_EVENING);
        }
        editor.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
