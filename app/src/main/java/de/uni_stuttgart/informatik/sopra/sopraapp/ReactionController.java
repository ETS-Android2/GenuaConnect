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

import de.uni_stuttgart.informatik.sopra.sopraapp.Monitoring.ApplianceManager;
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
            ApplianceManager applianceManager = ApplianceManager.getInstance(activity);
            applianceManager.addClient(new SimpleSNMPClientv3(qrCode));

            //SNMPv1v2c QR Code.
        } else if (new ApplianceQrDecode(qrCode).getSnmpVersion().equals("1") || new ApplianceQrDecode(qrCode).getSnmpVersion().equals("2c")) {
            ApplianceManager applianceManager = ApplianceManager.getInstance(activity);
            applianceManager.addClient(new SimpleSNMPClientV1AndV2c(qrCode));

        }
    }
}