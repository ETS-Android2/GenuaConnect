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

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
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
        entrys.add("Wifi IPv4: " + ipString);
        try {
            int i = 0;
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet6Address && i == 5) {
                        entrys.add("Wifi IPv6: " + inetAddress.getHostAddress());

                    }
                    i++;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        int serverAddress = dhcpInfo.serverAddress;
        int dns1 = dhcpInfo.dns1;
        int dns2 = dhcpInfo.dns2;
        int gateway = dhcpInfo.gateway;
        int mask = dhcpInfo.netmask;

        String maskString = String.format("%d.%d.%d.%d", (mask & 0xff), (mask >> 8 & 0xff), (mask >> 16 & 0xff), (mask >> 24 & 0xff));
        entrys.add("Netzmaske: "+ maskString);
        String dns1String = String.format("%d.%d.%d.%d", (dns1 & 0xff), (dns1 >> 8 & 0xff), (dns1 >> 16 & 0xff), (dns1 >> 24 & 0xff));
        entrys.add("DNS1: " + dns1String);
        String dns2String = String.format("%d.%d.%d.%d", (dns2 & 0xff), (dns2 >> 8 & 0xff), (dns2 >> 16 & 0xff), (dns2 >> 24 & 0xff));
        entrys.add("DNS2: " + dns2String);
        String gatewayString = String.format("%d.%d.%d.%d", (gateway & 0xff), (gateway >> 8 & 0xff), (gateway >> 16 & 0xff), (gateway >> 24 & 0xff));
        entrys.add("Gateway IP: " + gatewayString);
        String serverString = String.format("%d.%d.%d.%d", (serverAddress & 0xff), (serverAddress >> 8 & 0xff), (serverAddress >> 16 & 0xff), (serverAddress >> 24 & 0xff));
        entrys.add("Serveraddresse: "+ serverString);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entrys);
        ListView listView = findViewById(R.id.content_list);
        listView.setAdapter(itemsAdapter);
    }
}
