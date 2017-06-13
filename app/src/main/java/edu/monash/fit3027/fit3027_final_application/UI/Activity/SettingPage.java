package edu.monash.fit3027.fit3027_final_application.UI.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import edu.monash.fit3027.fit3027_final_application.R;

/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 * Activity controller for setting page
 */

public class SettingPage extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private CheckBox onTheDayCheckBox;
    private Spinner whenToNotifySpinner;
    private SharedPreferences sharedPref;
    public final static int DEFAULT_NOTIFY_OFF = 0;
    public final static int DEFAULT_NOTIFY_ON = 1;
    public final static int DEFAULT_NOTIFY_TIME_MORNING = 0;
    public final static int DEFAULT_NOTIFY_TIME_EVENING = 1;
    public final static String DEFAULT_NOTIFY_SETTING = "DefaultNotificationPref";
    public final static String DEFAULT_NOTIFY_TIME_SETTING = "DefaultNotificationTimePref";

    /**
     * When the activity has been created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);

        // Grab all of the information from the interface
        onTheDayCheckBox = (CheckBox) findViewById(R.id.onTheDayCheckBox);
        whenToNotifySpinner = (Spinner) findViewById(R.id.whenToNotifyspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminder_default_choice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        whenToNotifySpinner.setAdapter(adapter);

        // Grab all settings from the shared preferences, If it's user's first time run this app,
        // then make it default value (Notify me on the date it expiry. All notification should be shown in the morning)

        sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        switch (sharedPref.getInt(DEFAULT_NOTIFY_SETTING, DEFAULT_NOTIFY_ON)) { //Setting about notify me on the date it expiry or not
            case DEFAULT_NOTIFY_ON:
                onTheDayCheckBox.setChecked(true);
                break;
            case DEFAULT_NOTIFY_OFF:
                onTheDayCheckBox.setChecked(false);
                break;
        }

        switch (sharedPref.getInt(DEFAULT_NOTIFY_TIME_SETTING, DEFAULT_NOTIFY_TIME_MORNING)) { //Setting about show notification preferences
            case DEFAULT_NOTIFY_TIME_MORNING:
                whenToNotifySpinner.setSelection(0);
                break;
            case DEFAULT_NOTIFY_TIME_EVENING:
                whenToNotifySpinner.setSelection(1);
                break;
        }

        //Set these on listener
        onTheDayCheckBox.setOnCheckedChangeListener(this);
        whenToNotifySpinner.setOnItemSelectedListener(this);


    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //Edit the sharedPreferences depends on the input, write the setting to the backend
        SharedPreferences.Editor editor = sharedPref.edit();
        if (isChecked) {
            editor.putInt(DEFAULT_NOTIFY_SETTING, DEFAULT_NOTIFY_ON);
        } else {
            editor.putInt(DEFAULT_NOTIFY_SETTING, DEFAULT_NOTIFY_OFF);
        }
        editor.apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Edit the sharedPreferences depends on the input, write the setting to the backend
        SharedPreferences.Editor editor = sharedPref.edit();
        if (position == 0) {
            editor.putInt(DEFAULT_NOTIFY_TIME_SETTING, DEFAULT_NOTIFY_TIME_MORNING);
        } else {
            editor.putInt(DEFAULT_NOTIFY_TIME_SETTING, DEFAULT_NOTIFY_TIME_EVENING);
        }
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
