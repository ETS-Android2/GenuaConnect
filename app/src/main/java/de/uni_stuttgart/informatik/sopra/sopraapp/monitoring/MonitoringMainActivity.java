package de.uni_stuttgart.informatik.sopra.sopraapp.monitoring;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

    private ApplianceManager manager;
    private HashMap<SimpleSNMPClientV1AndV2c, String> appliances;
    private ArrayList<String> applianceNames;
    private RequestDbHelper dbHelper;
    private RecyclerView recyclerView;

    private static String qrCode = "{\"snmpVersion\":\"3\"," + "\"user\": \"root\"," + "\"target\": \"private\","
            + "\"pw\": \"asdf212!\"," + "\"enc\": {" + "  \"auth\": \"SHA\"," + "  \"priv\": \"AES-256\","
            + "  \"privKey\": \"asdf121!\"" + "}," + "\"naddr\": {" + " \"IPv4\": \"192.168.0.25\","
            + " \"IPv6\": \"2a56:0:1\"" + "}" + "}";


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_main);


        ListView allApplianceView = findViewById(R.id.all_appl_list);
        dbHelper = new RequestDbHelper(this);
        manager = ApplianceManager.getInstance(this);
        manager.addClient(new SimpleSNMPClientv3(qrCode));
        appliances = new HashMap<>();
        ArrayList<SimpleSNMPClientV1AndV2c> clients = manager.getClientList();

        int count = 0;
        for (SimpleSNMPClientV1AndV2c client : clients) {
            appliances.put(client, "Gerät " + count);
        }

        applianceNames = new ArrayList<>();
        applianceNames.addAll(appliances.values());
        List<SimpleSNMPClientV1AndV2c> list = new ArrayList<>();
        list.addAll(appliances.keySet());
        SnmpAdapter snmpAdapter = new SnmpAdapter(this);
        allApplianceView.setAdapter(snmpAdapter);


    }
}
