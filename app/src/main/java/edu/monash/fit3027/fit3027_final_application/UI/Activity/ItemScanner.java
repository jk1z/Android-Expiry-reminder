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
 * Created by YunHao Zhang
 * Student ID: 26956047
 * Activity controller for item scanner
 */


public class ItemScanner extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_scanner);

        // Test if we have camera permission
        // Reference from: https://developer.android.com/training/permissions/requesting.html
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }else{
                finish();
            }
        }else{
            startScanning(); //Start scanning intent
        }
    }

    private void startScanning(){
        IntentIntegrator barcodeScanner = new IntentIntegrator(this);
        barcodeScanner.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);//Only accept 1-D code
        barcodeScanner.setOrientationLocked(true);
        barcodeScanner.setBeepEnabled(true); //Set beep sound
        barcodeScanner.initiateScan(); //Start scanning
    }

    /**
     * Dispatch incoming result to the correct activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data); //Getting the result
        if (result != null) {
            if (result.getContents() != null) { //Return to the activity who called it
                Intent newIntent = new Intent();
                newIntent.putExtra("barcode", result.getContents());
                setResult(RESULT_OK, newIntent);
            }

        }
        finish(); //User might cancel the request
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     * @see #requestPermissions(String[], int)
     */
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
