package de.uni_stuttgart.informatik.sopra.sopraapp.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.Objects;

import de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode.RotatingCaptureActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.monitoring.MonitoringMainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.requests.RequestMngActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.wifi.WifiStateActivity;

/**
 * The main-method where the 3 btns are listed.
 */
public class MainActivity extends AppCompatActivity {

    //Intent that can start the scan of qr codes
    public IntentIntegrator intentIntegrator;
    public boolean test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting back-button on actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialising the IntentIntegrator and setting a few options
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setCaptureActivity(RotatingCaptureActivity.class);
        intentIntegrator.initiateScan();
        test = true;
    }

    /**
     * The onClick methode for the wifi btn.
     *
     * @param view The View of the clicked Button (buttonWifiInf)
     */
    public void wifiInfReact(View view) {
        Intent intent = new Intent(this, WifiStateActivity.class);
        startActivity(intent);
    }

    /**
     *
     * The onClick methode for the device manager btn.
     *
     * @param view The View of the clicked Button (device_management).
     */
    public void onClickDeviceManager(View view) {
        Intent intent = new Intent(this, MonitoringMainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //back button functions as a button that re-initialises the IntentIntegrator
        if (id == android.R.id.home) {
            intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setBeepEnabled(false);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.setCaptureActivity(RotatingCaptureActivity.class);
            intentIntegrator.initiateScan();
        }else if(id ==R.id.preference) {
            Intent intent = new Intent(this, HelpActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * The onClick methode for the query manager btn.
     *
     * @param view The View of the clicked Button (request_management).
     */
    public void requestManagement(View view) {
        Intent intent = new Intent(this, RequestMngActivity.class);
        startActivity(intent);
    }
}
