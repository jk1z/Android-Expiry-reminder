package edu.monash.fit3027.fit3027_final_application;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    private int spinnerChoice;
    private int year;
    private int month;
    private int date;
    private boolean isFetchFromServer = false;
    private NetworkHelper networkHelper;

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
                    String itemName;
                    int itemQuantity;
                    Calendar itemExpiryDate;
                    String itemColorTag;
                    int notifyDay;
                    long itemBarcode;
                    //Always hide the Keyboard interface first
                    InputMethodManager inputManager =
                            (InputMethodManager) getApplicationContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    itemName = itemNameEditText.getText().toString();
                    itemQuantity = Integer.parseInt(amountEditText.getText().toString());
                    itemExpiryDate = new GregorianCalendar(year, month, date);
                    //itemColorTag = colorTagImageButton.getBackground().toString();
                    itemColorTag = "@android:color/holo_orange_dark";
                    notifyDay = spinnerChoice;
                    itemBarcode = Long.parseLong(barcodeEditText.getText().toString());
                    DBHelper.addItem(new Item(UUID.randomUUID().toString(), itemName, itemQuantity, itemExpiryDate, itemColorTag, notifyDay, itemBarcode));
                    networkHelper.submitBarcode(String.valueOf(itemBarcode),itemName);
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
        this.year = year;
        this.month = month;
        this.date = dayOfMonth;
        expiryDateTextView.setText(dayOfMonth + "-" + month + "-" + year);
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
                    Barcode barcode = data.getParcelableExtra("barcode");
                    String itemName = data.getStringExtra("itemName");
                    barcodeEditText.setText(barcode.displayValue);
                    if (!itemName.equals("")){
                        itemNameEditText.setText(itemName);
                        this.isFetchFromServer = true;
                    }else{
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
}
