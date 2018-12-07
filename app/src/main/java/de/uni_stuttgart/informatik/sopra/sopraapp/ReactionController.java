package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientV1AndV2c;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientv3;

public class ReactionController {
    private String qrCode;
    private Activity activity;

    public ReactionController(Activity activity, String qrCode) {
        this.qrCode = qrCode;
        this.activity = activity;

        Log.d("Reacting To QR-Code", "QR-String = " + qrCode);
        if (qrCode.contains("WIFI")) {
            Log.d("Reacting To QR-Code", "detected a WIFI QR-String");
            new WifiConnect().tryConnect(qrCode, activity);
        } else if (!new ApplianceQrDecode(qrCode).getPassword().equals("")) {
            Log.d("Reacting To QR-Code", "detected a Appliance QR-String");

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET}, 2);
            } else {
                //V3 maybe TODO in next Sprint.(Right now unused code)
                SimpleSNMPClientv3 client = new SimpleSNMPClientv3(qrCode, activity);
                Toast.makeText(activity, "Gerät hinzugefügt V3", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("Reacting to QR-Code V1/V2c", "detected a Appliance QR-String V1/V2c");

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET}, 3);
            } else {
                SimpleSNMPClientV1AndV2c clientv2c = new SimpleSNMPClientV1AndV2c(qrCode, activity);
                Toast.makeText(activity, "Geraet hinzugefuegtV2", Toast.LENGTH_SHORT).show();
            }
        }
    }
}