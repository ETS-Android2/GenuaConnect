package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

import android.os.AsyncTask;
import android.util.Log;

import org.snmp4j.smi.OID;

import java.io.IOException;

/**
 * Seperate thread for networking.
 */
public class SnmpTask extends AsyncTask<String, Void, String> {

    private static final String TAG = SnmpTask.class.getName();
    private SimpleSNMPClientV1AndV2c snmpClient;
    private String oid;

    /**
     * constructor
     *
     * @param snmpClient Der SNMPv1v2 Manager.
     */
    public SnmpTask(SimpleSNMPClientV1AndV2c snmpClient) {
        this.snmpClient = snmpClient;
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
        oid = oids[0];
        try {
            snmpClient.start();
        } catch (IOException e) {
            Log.e("Exception", e.getMessage());
        }
        Log.d(TAG, "initialisiert");
        return snmpClient.getAsString(new OID(oids[0]));
    }

    public String getOid() {
        return oid;
    }
}
