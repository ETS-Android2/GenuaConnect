package de.uni_stuttgart.informatik.sopra.sopraapp.monitoring;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.requests.RequestDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SimpleSNMPClientV1AndV2c;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SimpleSNMPClientv3;

/**
 * This class is used for the Device Manager.
 */
public class MonitoringMainActivity extends AppCompatActivity {

    SnmpAdapter snmpAdapter;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_main);


        ListView allApplianceView = findViewById(R.id.all_appl_list);
        ApplianceManager manager = ApplianceManager.getInstance(this);
        HashMap<SimpleSNMPClientV1AndV2c, String> appliances = new HashMap<>();
        ArrayList<SimpleSNMPClientV1AndV2c> clients = manager.getClientList();

        int count = 0;
        for (SimpleSNMPClientV1AndV2c client : clients) {
            appliances.put(client, "Gerät " + count);
        }

        ArrayList<String> applianceNames = new ArrayList<>();
        applianceNames.addAll(appliances.values());
        List<SimpleSNMPClientV1AndV2c> list = new ArrayList<>();
        list.addAll(appliances.keySet());
        snmpAdapter = new SnmpAdapter(this);
        allApplianceView.setAdapter(snmpAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        snmpAdapter.cancelAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        snmpAdapter.restartTimer();
    }
}
