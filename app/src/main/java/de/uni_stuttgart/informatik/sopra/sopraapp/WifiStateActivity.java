package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Formatter;

public class WifiStateActivity extends AppCompatActivity {
    WifiManager mng;
    DhcpInfo dhcpInfo;
    WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_state);
        mng = (WifiManager) getSystemService(WIFI_SERVICE);
        dhcpInfo = mng.getDhcpInfo();
        wifiInfo = mng.getConnectionInfo();

        //creating the entries 1 to the
        ArrayList<String> entrys = new ArrayList<>();
        int ip = dhcpInfo.ipAddress;
        String ipString = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
        entrys.add("WifiIp: " + ipString);
        entrys.add("Mac : " + wifiInfo.getMacAddress());


        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, entrys);
        ListView listView = findViewById(R.id.content_list);
        listView.setAdapter(itemsAdapter);
    }


}
