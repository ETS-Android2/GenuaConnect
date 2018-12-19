package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;


import android.app.Activity;

import org.snmp4j.smi.OID;

import java.io.IOException;

public class InetClass extends Thread {
    SimpleSNMPClientV1AndV2c clientV1AndV2c;
    String qr_Code;
    Activity activity;

        String result;

    public InetClass(String qrCode, Activity activity){
        this.qr_Code = qrCode;
        this.activity = activity;
    }

    @Override
    public void run() {
        SimpleSNMPClientV1AndV2c clientv2c = new SimpleSNMPClientV1AndV2c(qr_Code, activity);
        String s = null;
        try {
            s = clientv2c.getAsString(new OID(".1.3.6.1.2"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = s;
    }

    public String getResult() {
        return result;
    }


}
