package edu.monash.fit3027.fit3027_final_application.Helper.NetworkHelper;

import android.os.AsyncTask;

/**
 * Created by Jack on 22-May-17.
 */

public abstract class NetworkHelper extends AsyncTask<String, Void, String> {
    String apiKey = "FIT3027";
    String BARCODE_QUERY_SERVER_URL = "http://52.65.177.140/cgi-bin/";

}
