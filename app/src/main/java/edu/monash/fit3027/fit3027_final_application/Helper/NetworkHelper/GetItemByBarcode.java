package edu.monash.fit3027.fit3027_final_application.Helper.NetworkHelper;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 * A customised network helper to get barcode in the background
 */

public class GetItemByBarcode extends NetworkHelper {
    private EditText editText;
    private String barcode;
    private Context mContext;

    public boolean isFromServer() {
        return isFromServer;
    }

    private boolean isFromServer = false;

    /**
     * Construct a getItemByBarcode Object
     * @param mContext Application context
     * @param editText EditText that is going to be filled
     * @param barcode Barcode in string
     */
    public GetItemByBarcode(Context mContext, EditText editText, String barcode) {
        this.mContext = mContext;
        this.editText = editText;
        this.barcode = barcode;
    }


    @Override
    protected String doInBackground(String... strings) {
        //Get the barcode from server
        try {
            URL url = new URL(BARCODE_QUERY_SERVER_URL + "getItemByBarcode.py?" + "barcode=" + barcode + "&" + "apiKey=" + this.apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream input = connection.getInputStream();
            String result;
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            while ((result = reader.readLine()) != null) {
                sb.append(result);
            }
            return sb.toString();
        } catch (Exception e) {
            Log.d("E", "GetItemByBarcode.java The server is down?");
        }
        return null;
    }

    /**
     *
     * Execute when doInBackground return a string
     * @param result The result of the operation
     */
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject barcodeContents = new JSONObject(result);
                String status = barcodeContents.getString("Status");
                if (status.equals("0")) { //If status equal 0 means query syntax is right
                    String itemName = barcodeContents.getString("item_name");
                    if (!itemName.equals("")) { //If return is not null, then the barcode is in database
                        this.editText.setText(itemName);
                        this.isFromServer = true;
                    } else {
                        this.isFromServer = false;
                        Toast errorMessage = Toast.makeText(this.mContext, "No matching result from the server", Toast.LENGTH_SHORT);
                        errorMessage.show();
                    }
                } else {
                    Log.d("W", "GetItemByBarcode.java Failed");
                }
            } catch (Exception e) {
                Log.d("W", "GetItemByBarcode.java Failed");
            }
        }
    }
}

