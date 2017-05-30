package edu.monash.fit3027.fit3027_final_application;

import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jack on 22-May-17.
 */

public class NetworkHelper {
    private String apiKey;
    private static final String BARCODE_QUERY_SERVER_URL = "http://54.66.221.2/cgi-bin/";

    public NetworkHelper(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getItemByBarcode(String barcode) {
        try {
            URL url = new URL(BARCODE_QUERY_SERVER_URL + "getItemByBarcode.py?" + "barcode=" + barcode + "&" + "apiKey=" + this.apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream input = connection.getInputStream();

            String result = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();

            while ((result = reader.readLine()) != null) {
                sb.append(result);
            }
            return this.translateToItemName(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String translateToItemName(String result) {
        String itemResult = "";
        if (result != null) {
            try {
                JSONObject barcodeContents = new JSONObject(result);
                String status = barcodeContents.getString("Status");

                if (status.equals("0")) {
                    itemResult = barcodeContents.getString("item_name");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return itemResult;
    }

    public void submitBarcode(String barcode, String itemName){
        try{
            URL url = new URL(BARCODE_QUERY_SERVER_URL + "submitBarcode.py?" + "barcode=" + barcode + "&"+"item_name="+itemName +"&"+"apiKey="+this.apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream input = connection.getInputStream();
            String result = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();

            while ((result = reader.readLine()) != null) {
                sb.append(result);
            }
            JSONObject barcodeContents = new JSONObject(result);
            barcodeContents.toString();
        }catch (Exception e){
            Log.d("myapp",Log.getStackTraceString(new Exception()));
            e.printStackTrace();
        }

    }
}
