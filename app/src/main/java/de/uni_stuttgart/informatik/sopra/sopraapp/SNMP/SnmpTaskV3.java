package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import android.os.AsyncTask;
import android.util.Log;

import org.snmp4j.smi.OID;

import java.io.IOException;

/**
 * Seperate thread for networking.
 */
public class SnmpTaskV3 extends AsyncTask<String, Void, String> {

    private static final String TAG = SnmpTask.class.getName();
    private SimpleSNMPClientv3 snmpClientv3;

    /**
     * Konstruktor
     * @param clientv3 Der SNMPv3 Manager.
     */
    public SnmpTaskV3(SimpleSNMPClientv3 clientv3) {
        this.snmpClientv3 = clientv3;
    }

    @Override
    protected void onPostExecute(String o) {
        if (o != null) {
            Log.d(TAG, "query result: " + o);
        }
        isCancelled();
        Log.d("Thread", "ThreadClosed");
    }

    @Override
    protected String doInBackground(String... oids) {
        try {
            snmpClientv3.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "initialisiert");
        try {
            return snmpClientv3.getAsString(new OID(oids[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}