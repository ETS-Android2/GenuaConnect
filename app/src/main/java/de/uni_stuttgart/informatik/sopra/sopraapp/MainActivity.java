package de.uni_stuttgart.informatik.sopra.sopraapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.Objects;

import de.uni_stuttgart.informatik.sopra.sopraapp.Monitoring.MonitoringMainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.Requests.RequestMngActivity;

/**
 * Die Mainmethode wo die 3 Btns gelistet sind.
 */
public class MainActivity extends AppCompatActivity {

    //Intent that can start the scan of qr codes
    IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Locale.getDefault();
        //Locale.setDefault();
        setContentView(R.layout.activity_main);
        //Intent intent = new Intent(this, RotatingCaptureActivity.class);
        //startActivity(intent);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialising the IntentIntegrator and setting a few options
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setCaptureActivity(RotatingCaptureActivity.class);
        intentIntegrator.initiateScan();
    }

    /**
     * Die onClick methode für den WIFI Btn.
     *
     * @param view Die View für den Intend.
     */
    public void wifiInfReact(View view) {
        Intent intent = new Intent(this, WifiStateActivity.class);
        startActivity(intent);
    }

    /**
     * Die onClick Methode für den Gerätemanager Btn.
     *
     * @param view Die View für den Intend.
     */
    public void onClickDeviceManager(View view) {
        Intent intent = new Intent(this, MonitoringMainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Die onClick Methode für den Abfragenmanager Btn.
     *
     * @param view Die View für den Intend.
     */
    public void requestManagement(View view) {
        Intent intent = new Intent(this, RequestMngActivity.class);
        startActivity(intent);
    }
}
