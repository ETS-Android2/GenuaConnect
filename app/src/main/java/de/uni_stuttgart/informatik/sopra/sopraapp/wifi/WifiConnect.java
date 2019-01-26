package de.uni_stuttgart.informatik.sopra.sopraapp.wifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

public class WifiConnect {

    /**
     * WIFI Parameters
     */
    private String ssid;
    private String authentification;
    private String password;

    /**
     * connects to WIFI that was coded in a QR-Code
     *
     * @param qrString QR String for Wifi String
     * @param context  Context of the class
     */

    public void tryConnect(String qrString, Context context) {
        //setting the WIFI Parameters from the qrString if its in correct Form
        if (!setWifiParamsFrom(qrString)) {
            Toast.makeText(context, context.getString(R.string.keinWifiQrCode), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("WIFI Params setted", context.getClass() + "\nauthentification: " + authentification + "\nssid: " + ssid + "\npass: " + password);

        //enable WIFI
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()) {
            Toast.makeText(context, context.getString(R.string.wifiNichtAnText), Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";   // Please note the quotes. String should contain ssid in quotes


        //switching between the different authentification and if there is a security
        switch (authentification) {
            case "WEP":
                conf.wepKeys[0] = "\"" + password + "\"";
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            case "WPA":
                conf.preSharedKey = "\"" + password + "\"";
                break;
            case "WPA2":
                conf.preSharedKey = "\"" + password + "\"";
                break;
            default:
                Toast.makeText(context, context.getString(R.string.keineSichereWlanVerbindungText), Toast.LENGTH_SHORT).show();
                return;
        }

        //connecting to WIFI
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        wifiManager.disconnect();
        wifiManager.enableNetwork(conf.networkId, true);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            wifiManager.removeNetwork(i.networkId);
        }
        wifiManager.addNetwork(conf);
        if (wifiInfo.getNetworkId() == -1) {
            wifiManager.reconnect();
            wifiManager.reassociate();
        } else {
            wifiManager.reconnect();
            wifiManager.reassociate();
        }
        Toast.makeText(context, context.getString(R.string.verbindungZuText) + " " + ssid + " " + context.getString(R.string.wirdAufgebautText), Toast.LENGTH_SHORT).show();
    }

    /**
     * extracting ssid, authentification and password from a qrString for WIFI
     * and setting them to the local variables
     *
     * @param qrString String that was scanned
     * @return true if qrString represented a correct WIFI QR Code
     */
    private boolean setWifiParamsFrom(String qrString) {

        if (!qrString.substring(0, 4).equals("WIFI")) {
            return false;
        }

        qrString = qrString.substring(5);
        StringBuilder builder = new StringBuilder();
        if (qrString.matches("S:.*;T:.*;P:.*;;")) {
            for (int i = 2; !qrString.substring(i).matches(";T:.*;P:.*;;"); i++) {
                builder.append(qrString.charAt(i));
            }
            ssid = builder.toString();
            qrString = qrString.substring(builder.length() + 3);
            builder.delete(0, builder.length());

            for (int i = 2; !qrString.substring(i).matches(";P:.*;;"); i++) {
                builder.append(qrString.charAt(i));
            }
            authentification = builder.toString();
            qrString = qrString.substring(builder.length() + 3);
            builder.delete(0, builder.length());

        } else if (qrString.matches("T:.*;S:.*;P:.*;;")) {
            for (int i = 2; !qrString.substring(i).matches(";S:.*;P:.*;;"); i++) {
                builder.append(qrString.charAt(i));
            }
            authentification = builder.toString();
            qrString = qrString.substring(builder.length() + 3);
            builder.delete(0, builder.length());

            for (int i = 2; !qrString.substring(i).matches(";P:.*;;"); i++) {
                builder.append(qrString.charAt(i));
            }
            ssid = builder.toString();
            qrString = qrString.substring(builder.length() + 3);
            builder.delete(0, builder.length());

        } else {
            return false;
        }

        for (int i = 2; i < qrString.length() - 2; i++)
            builder.append(qrString.charAt(i));
        password = builder.toString();

        return true;
    }
}