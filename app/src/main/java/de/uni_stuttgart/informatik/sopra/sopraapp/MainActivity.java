package de.uni_stuttgart.informatik.sopra.sopraapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {

    //Intent that can start the scan of qr codes
    IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent = new Intent(this, RotatingCaptureActivity.class);
        //startActivity(intent);

        //initialising the IntentIntegrator and setting a few options
         intentIntegrator = new IntentIntegrator(this);
         intentIntegrator.setBeepEnabled(false);
         intentIntegrator.setOrientationLocked(false);
         intentIntegrator.setCaptureActivity(RotatingCaptureActivity.class);
         intentIntegrator.initiateScan();
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                intentIntegrator.initiateScan();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
