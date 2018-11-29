package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class wifiConnect extends AppCompatActivity {

    public class WifiReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            stringBuilder = new StringBuilder();
            wifiList = wifiManager.getScanResults();
            stringBuilder.append("\n WLAN Verbindung :" +wifiList.size()+"\n\n");

            for(int i=0; i<wifiList.size(); i++){
                stringBuilder.append(new Integer(i+1).toString()+"");
                stringBuilder.append(wifiList.get(i).toString());
                stringBuilder.append("\n\n");
            }
            textView.setText(stringBuilder);
        }
    }



    private WifiManager wifiManager;
    private List<ScanResult> wifiList;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private TextView textView;
    WifiReceiver wifiReceiver;

    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.mainConstraint);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "wianfoawfioawnf", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);

        }
        wifiReceiver = new WifiReceiver();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        textView.setText("Starte Scan...");

    }

    @Override
    protected void onResume() {
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
    unregisterReceiver(null);
        super.onPause();
    }
}