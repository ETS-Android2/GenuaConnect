package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.uni_stuttgart.informatik.sopra.sopraapp.Requests.RequestDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.Requests.RequestsContract;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientV1AndV2c;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientv3;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SnmpTask;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SnmpTaskV3;

class ReactionController {

    ReactionController(Activity activity, String qrCode) {
        Log.d("Reacting To QR-Code", "QR-String = " + qrCode);
        if (qrCode.contains("WIFI")) {
            Log.d("Reacting To QR-Code", "detected a WIFI QR-String");
            new WifiConnect().tryConnect(qrCode, activity);
        } else if (!new ApplianceQrDecode(qrCode).getPassword().equals("")) {
            Log.d("Reacting To QR-Code", "detected a Appliance QR-String");

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET}, 2);
            } else {
                SimpleSNMPClientv3 clientv3 = new SimpleSNMPClientv3(qrCode);
                RequestDbHelper dbHelper = new RequestDbHelper(activity);
                SQLiteDatabase readable = dbHelper.getReadableDatabase();
                Cursor cursor = readable.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME + " where " + RequestsContract.COLUMN_REQ_NAME + " = 'Standardabfrage' ",null);
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_ID));

                cursor = readable.rawQuery("select * from " + RequestsContract.OID_TABLE_NAME + " where "+ RequestsContract.COLUMN_OID_REQ + " = " + id, null);
                String result1 = "";
                while (cursor.moveToNext()) {
                    SnmpTaskV3 snmpTaskV3 = new SnmpTaskV3(clientv3, activity);
                    Log.d("query snmp", cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_OID_STRING)));
                    snmpTaskV3.execute(cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_OID_STRING)));
                    try {
                        result1 += snmpTaskV3.get() + "\n";
                        Log.d("snmptest", "result: " + result1);
                        Toast.makeText(activity, result1, Toast.LENGTH_LONG).show();
                        if (result1.equals("")) {
                            Toast.makeText(activity, activity.getString(R.string.snmpAbfrageNichtfunktioniertTextDeutsch), Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
                try {
                    clientv3.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(activity, "SNMPv3 wird nicht unterstuetzt.", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("React to QR-Code V1/V2c", "detected a Appliance QR-String V1/V2c");

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET}, 3);
            } else {
                SimpleSNMPClientV1AndV2c clientv2c = new SimpleSNMPClientV1AndV2c(qrCode);
                RequestDbHelper dbHelper = new RequestDbHelper(activity);
                SQLiteDatabase readable = dbHelper.getReadableDatabase();
                Cursor cursor = readable.rawQuery("select * from " + RequestsContract.REQ_TABLE_NAME + " where " + RequestsContract.COLUMN_REQ_NAME + " = 'Standardabfrage' ",null);
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(RequestsContract.COLUMN_REQ_ID));

                cursor = readable.rawQuery("select * from " + RequestsContract.OID_TABLE_NAME + " where "+ RequestsContract.COLUMN_OID_REQ + " = " + id, null);
                String result1 = "";
                while (cursor.moveToNext()) {
                    SnmpTask snmpTask1 = new SnmpTask(clientv2c, activity);
                    Log.d("query snmp", cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_OID_STRING)));
                    snmpTask1.execute(cursor.getString(cursor.getColumnIndex(RequestsContract.COLUMN_OID_STRING)));
                    try {
                        result1 += snmpTask1.get() + "\n";
                        Log.d("snmptest", "result: " + result1);
                        Toast.makeText(activity, result1, Toast.LENGTH_LONG).show();
                        if (result1.equals("")) {
                            Toast.makeText(activity, activity.getString(R.string.snmpAbfrageNichtfunktioniertTextDeutsch), Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
                try {
                    clientv2c.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}