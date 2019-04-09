package com.app.admin.sellah.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scanBarcodeCustomLayout();

    }



    public void scanBarcodeCustomLayout() {
        IntentIntegrator integrator = new IntentIntegrator(this);
      //  integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
       // integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scanning");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
              //  toast = "Cancelled from fragment";
            } else {
             //   toast = "Scanned from fragment: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
         //   displayToast();
        }
    }
}
