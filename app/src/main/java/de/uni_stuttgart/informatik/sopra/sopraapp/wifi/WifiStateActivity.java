package de.uni_stuttgart.informatik.sopra.sopraapp.wifi;

import android.annotation.SuppressLint;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * This class shows the wifi/DHCP information in the activitiy.
 */
public class WifiStateActivity extends AppCompatActivity {
    WifiManager mng;
    DhcpInfo dhcpInfo;
    WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_state);

        mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        dhcpInfo = mng.getDhcpInfo();
        wifiInfo = mng.getConnectionInfo();


        //creating the entries 1 to the
        ArrayList<String> entrys = new ArrayList<>();
        int ip = dhcpInfo.ipAddress;
        @SuppressLint("DefaultLocale") String ipString = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
        entrys.add("IPv4: " + ipString);
        InetAddress inetAddress;
        try {
            int i = 0;
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet6Address && i == 5) {
                        entrys.add("IPv6: " + inetAddress.getHostAddress());
                    }
                    i++;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        int dns1 = dhcpInfo.dns1;
        int dns2 = dhcpInfo.dns2;
        int gateway = dhcpInfo.gateway;
        int mask = dhcpInfo.netmask;

        @SuppressLint("DefaultLocale") String maskString = String.format("%d.%d.%d.%d", (mask & 0xff), (mask >> 8 & 0xff), (mask >> 16 & 0xff), (mask >> 24 & 0xff));
        entrys.add(getString(R.string.netzmaskeText) + maskString);
        @SuppressLint("DefaultLocale") String dns1String = String.format("%d.%d.%d.%d", (dns1 & 0xff), (dns1 >> 8 & 0xff), (dns1 >> 16 & 0xff), (dns1 >> 24 & 0xff));
        entrys.add("DNS1: " + dns1String);
        @SuppressLint("DefaultLocale") String dns2String = String.format("%d.%d.%d.%d", (dns2 & 0xff), (dns2 >> 8 & 0xff), (dns2 >> 16 & 0xff), (dns2 >> 24 & 0xff));
        entrys.add("DNS2: " + dns2String);
        @SuppressLint("DefaultLocale") String gatewayString = String.format("%d.%d.%d.%d", (gateway & 0xff), (gateway >> 8 & 0xff), (gateway >> 16 & 0xff), (gateway >> 24 & 0xff));
        entrys.add("Gateway IP: " + gatewayString);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entrys);
        ListView listView = findViewById(R.id.content_list);
        listView.setAdapter(itemsAdapter);
    }
}
