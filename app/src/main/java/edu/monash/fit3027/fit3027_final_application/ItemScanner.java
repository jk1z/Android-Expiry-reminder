package edu.monash.fit3027.fit3027_final_application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by Jack on 07-May-17.
 */

public class ItemScanner extends AppCompatActivity {

    //
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//                final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();//TODO:Need to crop the image and then feed in to the detector
//                if (barcodeSparseArray.size() > 0) {
//                    Intent newIntent = new Intent();
//                    String itemName = networkHelper.getItemByBarcode(barcodeSparseArray.valueAt(0).displayValue);
//                    newIntent.putExtra("itemName", itemName);
//                    newIntent.putExtra("barcode", barcodeSparseArray.valueAt(0));
//                    setResult(RESULT_OK, newIntent);
//                    finish();
//                }
//            }
//        });
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_scanner);
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
                newIntent.putExtra("itemName", "");
                newIntent.putExtra("barcode", result.getContents());
                setResult(RESULT_OK, newIntent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
