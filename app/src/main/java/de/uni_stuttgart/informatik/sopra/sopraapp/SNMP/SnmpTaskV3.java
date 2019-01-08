package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.snmp4j.smi.OID;

import java.io.IOException;

/**
 * Seperate thread for networking.
 */
public class SnmpTaskV3 extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private static final String TAG = SnmpTask.class.getName();
    private SimpleSNMPClientv3 snmpClientv3;
    private Context context;

    public SnmpTaskV3(SimpleSNMPClientv3 clientv3, Context context) {
        this.snmpClientv3 = clientv3;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String o) {
        progressDialog.dismiss();
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