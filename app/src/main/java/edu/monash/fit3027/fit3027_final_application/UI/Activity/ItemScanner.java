package edu.monash.fit3027.fit3027_final_application.UI.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import edu.monash.fit3027.fit3027_final_application.R;


/**
 * Created by Jack on 07-May-17.
 */

public class ItemScanner extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_scanner);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            startScanning();
        }
    }

    private void startScanning(){
        IntentIntegrator barcodeScanner = new IntentIntegrator(this);
        barcodeScanner.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        barcodeScanner.setOrientationLocked(false);
        barcodeScanner.setBeepEnabled(true);
        barcodeScanner.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Intent newIntent = new Intent();
                newIntent.putExtra("barcode", result.getContents());
                setResult(RESULT_OK, newIntent);
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanning();
                } else {
                    finish();
                }
                break;
            }
        }
    }
}
