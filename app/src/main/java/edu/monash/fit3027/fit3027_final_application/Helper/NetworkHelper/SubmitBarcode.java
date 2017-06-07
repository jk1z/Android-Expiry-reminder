package edu.monash.fit3027.fit3027_final_application.Helper.NetworkHelper;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jack on 06-Jun-17.
 */

public class SubmitBarcode extends NetworkHelper {
    private String barcode;
    private String itemName;

    public SubmitBarcode(String barcode, String itemName) {
        this.barcode = barcode;
        this.itemName = itemName;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(BARCODE_QUERY_SERVER_URL + "submitBarcode.py?" + "barcode=" + barcode + "&" + "item_name=" + itemName + "&" + "apiKey=" + this.apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream input = connection.getInputStream();
            String result = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            while ((result = reader.readLine()) != null) {
                sb.append(result);
            }
            return sb.toString();
        } catch (Exception e) {
            Log.d("E", "SubmitBarcode.java The server is down?");
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject barcodeContents = new JSONObject(result);
                String status = barcodeContents.getString("Status");
                if (status.equals("1")) {
                    Log.d("W", "SubmitBarcode.java Submit failed");
                }
            } catch (Exception e) {
                Log.d("W", "SubmitBarcode.java Submit failed");
            }
        }
    }
}
