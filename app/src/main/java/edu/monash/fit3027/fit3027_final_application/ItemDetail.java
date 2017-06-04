package edu.monash.fit3027.fit3027_final_application;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
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

import com.google.android.gms.vision.barcode.Barcode;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by Jack on 07-May-17.
 */

public class ItemDetail extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    private EditText itemNameEditText;
    private EditText amountEditText;
    private EditText barcodeEditText;
    private ImageButton colorTagImageButton;
    private ImageButton calenderImageButton;
    private ImageButton clearExpiryDateImageButton;
    private ImageButton barcodeScanImageButton;
    private Spinner notifyChoiceSpinner;
    private Button clearAmountButton;
    private Button cancelButton;
    private Button okButton;
    private TextView expiryDateTextView;
    private DatabaseHelper DBHelper;
    private int spinnerChoice = 2;
    private int year;
    private int month;
    private int date;
    private boolean isFetchFromServer = false;
    private NetworkHelper networkHelper;
    private Calendar itemExpiryDate;

    public final static int REQUEST_BARCODE = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

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

        DBHelper = new DatabaseHelper(getApplicationContext());

        networkHelper = new NetworkHelper("FIT3027");
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
        Intent intent = getIntent();
        if (intent.getBooleanExtra("addThroughCamera",false)){
            Intent newIntent = new Intent(this, ItemScanner.class);
            startActivityForResult(newIntent, REQUEST_BARCODE);
        }
    }
    @Override
    public void onClick(View v) {
        Intent newIntent;
        switch (v.getId()) {
            case R.id.clearAmountButton:
                amountEditText.setText("");
                break;
            case R.id.cancelButton:
                finish();
                break;
            case R.id.clearExpiryDateImageButton:
                expiryDateTextView.setText("Not Set");
                break;
            case R.id.okButton:
                try {
                    boolean barcodeNull = true;

                    //Always hide the Keyboard interface first
                    InputMethodManager inputManager =
                            (InputMethodManager) getApplicationContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    long itemBarcode = 0;
                    String itemBarcodeString = barcodeEditText.getText().toString();
                    if (!itemBarcodeString.matches("")){
                        barcodeNull = false;
                        itemBarcode = Long.parseLong(itemBarcodeString);
                    }

                    String itemName = itemNameEditText.getText().toString();
                    if (itemName.matches("")){
                        Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String itemColorTag = "#FF6666";

                    String itemQuantityString = amountEditText.getText().toString();
                    if (itemQuantityString.matches("")){
                        Toast.makeText(this, "Please enter item amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int itemQuantity = Integer.parseInt(itemQuantityString);

                    int notifyDay = spinnerChoice;


                    DBHelper.addItem(new Item(UUID.randomUUID().toString(), itemName, itemQuantity, itemExpiryDate, itemColorTag, notifyDay, itemBarcode));

                    if (!isFetchFromServer & !barcodeNull) {
                        networkHelper.submitBarcode(String.valueOf(itemBarcode), itemName);
                    }

                    alarmMethod(itemName, notifyDay, itemExpiryDate);

                    newIntent = new Intent();
                    setResult(RESULT_OK, newIntent);
                    finish();
                } catch (Exception ex) {
                    Toast errorMessage = Toast.makeText(ItemDetail.this, ex.getMessage(), Toast.LENGTH_SHORT);
                    errorMessage.show();
                }
                break;
            case R.id.calenderImageButton:
                Calendar now = new GregorianCalendar();
                new DatePickerDialog(this, this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE)).show();
                break;
            case R.id.barcodeScanImageButton:
                newIntent = new Intent(this, ItemScanner.class);
                startActivityForResult(newIntent, REQUEST_BARCODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        itemExpiryDate = new GregorianCalendar(year, month, dayOfMonth);
        itemExpiryDate.set(Calendar.MILLISECOND, 0);
        expiryDateTextView.setText(new SimpleDateFormat("dd-MMM-yy",Locale.getDefault()).format(itemExpiryDate.getTime()).replace(".",""));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position) == "2 Days") {
            spinnerChoice = 2;
        } else if (parent.getItemAtPosition(position) == "5 Days") {
            spinnerChoice = 5;
        } else if (parent.getItemAtPosition(position) == "10 Days") {
            spinnerChoice = 10;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spinnerChoice = 2;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BARCODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
//                    Barcode barcode = data.getParcelableExtra("barcode");
                    String barcode = data.getStringExtra("barcode");
                    String itemName = data.getStringExtra("itemName");
                    barcodeEditText.setText(barcode);
                    if (!itemName.equals("")) {
                        itemNameEditText.setText(itemName);
                        this.isFetchFromServer = true;
                    } else {
                        Toast errorMessage = Toast.makeText(ItemDetail.this, "No matching result from the server", Toast.LENGTH_SHORT);
                        errorMessage.show();
                    }
                } else {
                    Toast errorMessage = Toast.makeText(ItemDetail.this, "Barcode not found", Toast.LENGTH_SHORT);
                    errorMessage.show();
                }
            }
        }
    }

    private void alarmMethod(String itemName, int daysBefore, Calendar expiryDate) {
        Intent alarmIntent = new Intent(this, NotifyHelper.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, alarmIntent, 0);
        expiryDate.add(Calendar.DAY_OF_MONTH, -daysBefore);
        expiryDate.set(Calendar.HOUR, 8);
        expiryDate.set(Calendar.MINUTE, 0);
        expiryDate.set(Calendar.SECOND, 0);
        expiryDate.set(Calendar.AM_PM, Calendar.AM);
        long alertTime = expiryDate.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
    }

}
