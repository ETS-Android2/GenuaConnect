package de.uni_stuttgart.informatik.sopra.sopraapp.Monitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientV1AndV2c;

public class MonitoringMainActivity extends AppCompatActivity {

    private ApplianceManager manager;
    private HashMap<SimpleSNMPClientV1AndV2c,String> appliances;
    private ArrayList<String> applianceNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_main);

        manager = ApplianceManager.getInstance(this);
        appliances = new HashMap<>();
        ArrayList<SimpleSNMPClientV1AndV2c> clients = manager.getClientList();

        int count = 0;
        for (SimpleSNMPClientV1AndV2c client: clients){
            appliances.put(client, "Gerät " + count);
        }

        applianceNames = (ArrayList<String>)appliances.values();



    }
}
