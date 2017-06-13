package edu.monash.fit3027.fit3027_final_application.UI.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import edu.monash.fit3027.fit3027_final_application.Helper.DatabaseHelper;
import edu.monash.fit3027.fit3027_final_application.Helper.NetworkHelper.GetItemByBarcode;
import edu.monash.fit3027.fit3027_final_application.Helper.NotifyHelper;
import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.Helper.NetworkHelper.SubmitBarcode;
import edu.monash.fit3027.fit3027_final_application.model.Item;

import static edu.monash.fit3027.fit3027_final_application.UI.Activity.SettingPage.DEFAULT_NOTIFY_OFF;
import static edu.monash.fit3027.fit3027_final_application.UI.Activity.SettingPage.DEFAULT_NOTIFY_ON;
import static edu.monash.fit3027.fit3027_final_application.UI.Activity.SettingPage.DEFAULT_NOTIFY_SETTING;
import static edu.monash.fit3027.fit3027_final_application.UI.Activity.SettingPage.DEFAULT_NOTIFY_TIME_EVENING;
import static edu.monash.fit3027.fit3027_final_application.UI.Activity.SettingPage.DEFAULT_NOTIFY_TIME_MORNING;
import static edu.monash.fit3027.fit3027_final_application.UI.Activity.SettingPage.DEFAULT_NOTIFY_TIME_SETTING;

/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 * Activity controller for item_detail
 */

public class ItemDetail extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    private EditText itemNameEditText;
    private EditText amountEditText;
    private EditText barcodeEditText;
    private EditText itemTypeEditText;
    private ImageButton colorTagImageButton;
    private ImageButton calenderImageButton;
    private ImageButton clearExpiryDateImageButton;
    private ImageButton barcodeScanImageButton;
    private Spinner notifyChoiceSpinner;
    private Button clearAmountButton;
    private Button cancelButton;
    private Button okButton;
    private TextView expiryDateTextView;
    private Item targetItem;

    private DatabaseHelper DBHelper;
    private int spinnerChoice = 0;
    private Calendar itemExpiryDate;
    private GetItemByBarcode getItemByBarcode;
    private String itemColorTag = "#ef5350";

    public final static int REQUEST_BARCODE = 3;
    public final static int REQUEST_COLOR_TAG = 4;

    /**
     * When the activity has been created
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        // Grab all of the information from the interface
        itemNameEditText = (EditText) findViewById(R.id.itemNameEditText);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        colorTagImageButton = (ImageButton) findViewById(R.id.colorTagImageButton);
        calenderImageButton = (ImageButton) findViewById(R.id.calenderImageButton);
        clearExpiryDateImageButton = (ImageButton) findViewById(R.id.clearExpiryDateImageButton);
        notifyChoiceSpinner = (Spinner) findViewById(R.id.NotifyChoiceSpinner);
        clearAmountButton = (Button) findViewById(R.id.clearAmountButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        okButton = (Button) findViewById(R.id.okButton);
        barcodeEditText = (EditText) findViewById(R.id.barcodeEditText);
        barcodeScanImageButton = (ImageButton) findViewById((R.id.barcodeScanImageButton));
        expiryDateTextView = (TextView) findViewById((R.id.expiryDateTextView));
        itemTypeEditText = (EditText) findViewById(R.id.itemTypeEditText);
        DBHelper = new DatabaseHelper(getApplicationContext());
        colorTagImageButton.setBackgroundColor(Color.parseColor(itemColorTag));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reminder_choice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notifyChoiceSpinner.setAdapter(adapter);

        calenderImageButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        clearExpiryDateImageButton.setOnClickListener(this);
        clearAmountButton.setOnClickListener(this);
        barcodeScanImageButton.setOnClickListener(this);
        colorTagImageButton.setOnClickListener(this);
        notifyChoiceSpinner.setOnItemSelectedListener(this);

        Intent intent = getIntent();

        //If it's a update
        Item item = intent.getParcelableExtra("item");
        if (item != null){
            this.targetItem = item;
            barcodeEditText.setText(String.valueOf(item.getItemBarcode()));
            itemNameEditText.setText(item.getItemName());
            colorTagImageButton.setBackgroundColor(Color.parseColor(item.getItemColorTag()));
            itemColorTag = item.getItemColorTag();
            itemExpiryDate = item.getItemExpiryDate();
            expiryDateTextView.setText(new SimpleDateFormat("dd-MMM-yy", Locale.getDefault()).format(itemExpiryDate.getTime()).replace(".", ""));
            amountEditText.setText(String.valueOf(item.getItemQuantity()));
            itemTypeEditText.setText(item.getItemType());
            okButton.setText("Update");
            changeSpinnerChoice();
        }

        //If user clicked addThroughCamera from the main Activity
        if (intent.getBooleanExtra("addThroughCamera", false)) {
            Intent newIntent = new Intent(this, ItemScanner.class);
            startActivityForResult(newIntent, REQUEST_BARCODE);
        }




    }

    @Override
    public void onClick(View v) {
        Intent newIntent;
        switch (v.getId()) {
            case R.id.clearAmountButton: //If the clear button next to item amount has been clicked
                amountEditText.setText("");
                break;
            case R.id.cancelButton: //If the cancel button has been clicked
                finish();
                break;
            case R.id.clearExpiryDateImageButton: //If the clear button next to expiry date is clicked
                expiryDateTextView.setText("Not Set");
                itemExpiryDate = null; //Clear the calendar object
                break;
            case R.id.okButton:
                try {


                    //Always hide the Keyboard interface first
                    InputMethodManager inputManager =
                            (InputMethodManager) getApplicationContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    boolean barcodeNull = true;
                    long itemBarcode = 0;//Initialise the itemBarcode
                    String itemBarcodeString = barcodeEditText.getText().toString();
                    if (!itemBarcodeString.matches("")) { //Test if the item barcode is null
                        barcodeNull = false;
                        itemBarcode = Long.parseLong(itemBarcodeString);
                    }

                    String itemName = itemNameEditText.getText().toString();
                    if (itemName.matches("")) { //Test if itemName is null
                        Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (itemExpiryDate == null) { //Test if expiry calendar object is null
                        Toast.makeText(this, "Please set a expiry date", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String itemQuantityString = amountEditText.getText().toString();
                    if (itemQuantityString.matches("")) { //Test if itemQuality is null
                        Toast.makeText(this, "Please enter item amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int itemQuantity = Integer.parseInt(itemQuantityString);

                    String itemType = itemTypeEditText.getText().toString();
                    if (itemType.matches("")) { //Test if item unit is null
                        Toast.makeText(this, "Please enter item unit", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int notifyDay = spinnerChoice;
                    Item item;
                    //Submit item to the network, and save it to database
                    if (this.targetItem == null) {//User wants to create a new item
                        item = new Item(UUID.randomUUID().toString(), itemName, itemQuantity, itemType, itemExpiryDate, itemColorTag, notifyDay, itemBarcode);
                        DBHelper.addItem(item);
                        if (!barcodeNull) {
                            if (this.getItemByBarcode != null) {
                                if (!getItemByBarcode.isFromServer()) {
                                    new SubmitBarcode(String.valueOf(itemBarcode), itemName).execute();
                                }
                            }
                        }
                    }else{//User wants to update the Item
                        targetItem.setItemBarcode(itemBarcode);
                        targetItem.setItemName(itemName);
                        targetItem.setItemQuantity(itemQuantity);
                        targetItem.setItemType(itemType);
                        targetItem.setItemExpiryDate(itemExpiryDate);
                        targetItem.setItemColorTag(itemColorTag);
                        targetItem.setNotifyDay(notifyDay);
                        DBHelper.updateItem(targetItem);
                        item = targetItem;
                    }

                    if (notifyDay > 0 & targetItem == null){//If user choose additional reminder
                        alarmMethod(item,notifyDay,createID());
                    }
                    if (item.daysLeftInt() >= 0 & targetItem == null) {//Remind the user on the expiry date
                        SharedPreferences sharedPref = getSharedPreferences("Settings",Context.MODE_PRIVATE);
                        switch (sharedPref.getInt(DEFAULT_NOTIFY_SETTING,DEFAULT_NOTIFY_ON)){
                            case DEFAULT_NOTIFY_ON:
                                alarmMethod(item, 0,createID());//If notification is turned on, notify the user
                                break;
                            default:
                                break;
                        }
                    }

                    newIntent = new Intent();
                    setResult(RESULT_OK, newIntent);
                    finish();//Return back to main activity

                } catch (Exception ex) {
                    Toast errorMessage = Toast.makeText(ItemDetail.this, ex.getMessage(), Toast.LENGTH_SHORT);
                    errorMessage.show();
                }
                break;
            case R.id.calenderImageButton:
                Calendar now = new GregorianCalendar();
                new DatePickerDialog(this, this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE)).show();//Popup a calendar picker fragment
                break;
            case R.id.barcodeScanImageButton: //User wants to scan the item
                newIntent = new Intent(this, ItemScanner.class);
                startActivityForResult(newIntent, REQUEST_BARCODE);
                break;
            case R.id.colorTagImageButton: //User wants to choose a color tag
                newIntent = new Intent(this, ColorSelectionGridView.class);
                startActivityForResult(newIntent, REQUEST_COLOR_TAG);
                break;
            default:
                break;
        }
    }

    /**
     * @param view the picker associated with the dialog
     * @param year the selected year
     * @param month the selected month
     * @param dayOfMonth the selected day of the month (1-31, depending on
     *                   month)
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        itemExpiryDate = new GregorianCalendar(year, month, dayOfMonth);
        itemExpiryDate.set(Calendar.MILLISECOND, 0);
        expiryDateTextView.setText(new SimpleDateFormat("dd-MMM-yy", Locale.getDefault()).format(itemExpiryDate.getTime()).replace(".", ""));
        changeSpinnerChoice();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).equals("2 Days Before")) {
            spinnerChoice = 2;
        } else if (parent.getItemAtPosition(position).equals("5 Days Before")) {
            spinnerChoice = 5;
        } else if (parent.getItemAtPosition(position).equals("10 Days Before")) {
            spinnerChoice = 10;
        } else if (parent.getItemAtPosition(position).equals("No Option")){
            spinnerChoice = 0;
        }
    }

    /**
     * If nothing is selected, then set spinnerChoice as 0
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spinnerChoice = 0;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_BARCODE: //The result is from ItemScanner
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String barcode = data.getStringExtra("barcode");
                        if (!barcode.equals("")) {
                            getItemByBarcode = new GetItemByBarcode(this, itemNameEditText, barcode);
                            getItemByBarcode.execute();
                            barcodeEditText.setText(barcode);
                        } else {
                            Toast errorMessage = Toast.makeText(ItemDetail.this, "Barcode not found", Toast.LENGTH_SHORT);
                            errorMessage.show();
                        }
                    }
                }else{
                    Toast errorMessage = Toast.makeText(ItemDetail.this, "Please allow camera permission", Toast.LENGTH_SHORT);
                    errorMessage.show();
                }
                break;
            case REQUEST_COLOR_TAG://The result is from ColorSelectionGridView
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String colorTag = data.getStringExtra("colorTag");
                        if (!colorTag.equals("")) {
                            colorTagImageButton.setBackgroundColor(Color.parseColor(colorTag));
                            this.itemColorTag = colorTag;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Set the alarm
     * @param item the item object
     * @param notificationDay When to notify
     * @param alertID A unique alertID
     */
    private void alarmMethod(Item item, int notificationDay, int alertID) {
        int dayOrNight;
        int notificationHour;
        SharedPreferences sharedPref = getSharedPreferences("Settings",Context.MODE_PRIVATE);
        switch (sharedPref.getInt(DEFAULT_NOTIFY_TIME_SETTING,DEFAULT_NOTIFY_TIME_MORNING)){
            case DEFAULT_NOTIFY_TIME_MORNING:
                dayOrNight = Calendar.AM;
                notificationHour = 8;
                break;
            case DEFAULT_NOTIFY_TIME_EVENING:
                dayOrNight = Calendar.PM;
                notificationHour = 5;
                break;
            default:
                notificationHour = 8;
                dayOrNight = Calendar.AM;
                break;
        }
        Intent alarmIntent = new Intent(this, NotifyHelper.class);
        alarmIntent.putExtra("daysLeft",notificationDay);
        alarmIntent.putExtra("itemName",item.getItemName());
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alertID, alarmIntent, 0);
        Calendar expiryDate = (Calendar) item.getItemExpiryDate().clone();
        expiryDate.add(Calendar.DAY_OF_MONTH, -notificationDay);
        expiryDate.set(Calendar.HOUR, notificationHour);
        expiryDate.set(Calendar.MINUTE, 0);
        expiryDate.set(Calendar.SECOND, 0);
        expiryDate.set(Calendar.AM_PM, dayOrNight);
        long alertTime = expiryDate.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
    }

    /**
     * Restricting the spinner option
     */
    private void changeSpinnerChoice(){
        //Get the days left between expiry date and now
        Calendar now = new GregorianCalendar();
        int startDate = now.get(Calendar.DAY_OF_MONTH);
        int endDate = itemExpiryDate.get(Calendar.DAY_OF_MONTH);
        int diffDay = endDate - startDate;
        //Limit the option
        Resources res = getResources();
        List<String> choices = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.reminder_choice_array)));
        if (diffDay < 2) {
            choices.remove(choices.size()-1);
            choices.remove(choices.size()-1);
            choices.remove(choices.size()-1);
        } else if (diffDay < 5) {
            choices.remove(choices.size()-1);
            choices.remove(choices.size()-1);
        } else if (diffDay < 10) {
            choices.remove(choices.size()-1);
        }
        spinnerChoice = 0;

        //Refresh the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notifyChoiceSpinner.setAdapter(adapter);
    }

    /**
     * Create a random ID for the alert ID
     * @return a random ID
     */
    public int createID(){
        Date now = new Date();
        return Integer.parseInt(new SimpleDateFormat("ddHHmmssSS",  Locale.getDefault()).format(now))% Integer.MAX_VALUE;//User day hour month second millsecond to construct the number
        //The number has to be within Integer.Max
    }

}
