package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.uni_stuttgart.informatik.sopra.sopraapp.Requests.RequestDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientV1AndV2c;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientv3;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SnmpTask;

/**
 * In dieser Klasse wird definiert, wie auf die QR-Codes reagiert werden soll.
 */
class ReactionController {

    /**
     * Konstruktor
     *
     * @param activity Der uebergebene activity.
     * @param qrCode   Ist der QR Code decoder.
     */
    ReactionController(Activity activity, String qrCode) {
        Log.d("Reacting To QR-Code", "QR-String = " + qrCode);
        //WIFI QR Code
        if (qrCode.contains("WIFI")) {
            Log.d("Reacting To QR-Code", "detected a WIFI QR-String");
            new WifiConnect().tryConnect(qrCode, activity);

            // SNMPv3 QR COde
        } else if (new ApplianceQrDecode(qrCode).getSnmpVersion().equals("3")) {
            Log.d("Reacting To QR-Code", "detected a Appliance QR-String V3");
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET}, 2);
            } else {
                RequestDbHelper dbHelper = new RequestDbHelper(activity);
                ArrayList<String> oiDsFrom = dbHelper.getOIDsFrom("Standardabfragen");
                if (oiDsFrom.size() == 0) {
                    Toast.makeText(activity, activity.getString(R.string.keine_OIDs), Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleSNMPClientv3 clientv3 = new SimpleSNMPClientv3(qrCode);
                String result1 = "";
                for (String oid :
                        oiDsFrom) {
                    SnmpTask snmpTaskV3 = new SnmpTask(clientv3);
                    Log.d("query snmp", oid);
                    snmpTaskV3.execute(oid);
                    try {
                        result1 += snmpTaskV3.get() + "\n";
                        Log.d("snmptest", "result: " + result1);
                        Toast.makeText(activity, result1, Toast.LENGTH_LONG).show();
                        if (result1.isEmpty()) {
                            Toast.makeText(activity, activity.getString(R.string.snmpAbfrageNichtfunktioniertText), Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    snmpTaskV3.cancel(true);
                }
                try {
                    clientv3.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //SNMPv1v2c QR Code.
        } else if (new ApplianceQrDecode(qrCode).getSnmpVersion().equals("1") || new ApplianceQrDecode(qrCode).getSnmpVersion().equals("2c")) {
            Log.d("React to QR-Code V1/V2c", "detected a Appliance QR-String V1/V2c");

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET}, 3);
            } else {
                RequestDbHelper dbHelper = new RequestDbHelper(activity);
                ArrayList<String> oiDsFrom = dbHelper.getOIDsFrom("Standardabfragen");
                if (oiDsFrom.size() == 0) {
                    Toast.makeText(activity, activity.getString(R.string.keine_OIDs), Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleSNMPClientV1AndV2c clientv2c = new SimpleSNMPClientV1AndV2c(qrCode);
                String result1 = "";
                for (String oid : oiDsFrom) {
                    SnmpTask snmpTask1 = new SnmpTask(clientv2c);
                    Log.d("query snmp", oid);
                    snmpTask1.execute(oid);
                    try {
                        result1 += snmpTask1.get() + "\n";
                        Log.d("snmptest", "result: " + result1);
                        Toast.makeText(activity, result1, Toast.LENGTH_LONG).show();
                        if (result1.isEmpty()) {
                            Toast.makeText(activity, activity.getString(R.string.snmpAbfrageNichtfunktioniertText), Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    snmpTask1.cancel(true);
                }
                try {
                    clientv2c.stop();
                } catch (IOException e) {
                    Log.e("snmpClose", e.getMessage());
                }
            }
        }
    }
}