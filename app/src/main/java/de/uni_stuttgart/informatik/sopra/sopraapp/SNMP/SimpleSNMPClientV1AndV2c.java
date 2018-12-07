package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.ApplianceQrDecode;

public class SimpleSNMPClientV1AndV2c {

    private String address;
    private Snmp snmp;
    private Activity activity;

    public SimpleSNMPClientV1AndV2c(String qrCode, Activity activity) {
        super();
        SNMP4JSettings.setAllowSNMPv2InV1(true);
        SNMP4JSettings.setSnmp4jStatistics(SNMP4JSettings.Snmp4jStatistics.extended);
        Log.d("allowSNMPv2InV1", "erfolgreich");
        ApplianceQrDecode decode = new ApplianceQrDecode(qrCode);
        this.address = decode.getAddress();
        try {
            start();
            Log.d("start()", "start ausgefuehrt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() throws IOException {
        snmp.close();
        Log.d("SNMP", "Interface for SNMP closed");
    }

    /**
     * Starts the SNMP Interface.
     *
     * @throws IOException
     */
    private void start() throws IOException {
        final TransportMapping transportMapping = new DefaultUdpTransportMapping();
        Log.d("AsyncTask", "asynchroner Nebenthread gestartet");
        AsyncTask<TransportMapping, Void, Snmp > task =new AsyncTask<TransportMapping, Void, Snmp>() {
            @Override
            protected Snmp doInBackground(TransportMapping[] transportMappings) {
                snmp = new Snmp(transportMapping);
                Log.d("snmpStart", "initialisiert");
                return snmp;
            }

            @Override
            protected void onPostExecute(Snmp o) {
                try {
                    transportMapping.listen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("listen", "listen erhalten");
                isCancelled();
                Log.d("Thread", "ThreadClosed");
            }
        };
        task.execute(transportMapping);
        Log.d("task", "taskExecuted");
    }

    /**
     * Returns a target, which contains the information about to where and how the data should be fetched.
     *
     * @return Returns the given target.
     */
    private Target getTarget() {
        Address targetAdress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAdress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        Log.d("getTarget", "getTarget erfolgreich");
        return target;
    }

    /**
     * Handles multiple OIDs.
     *
     * @param oids Array of OIDs.
     * @return Returns the ResponseEvent.
     * @throws IOException
     */
    public ResponseEvent get(OID oids[]) throws IOException {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if (event != null) {
            return event;
        }
        throw new RuntimeException("Zeitüberschreitung für GET");
    }

    /**
     * Returns the response of the OID as a string.
     *
     * @param oid Is the OID.
     * @return Returns the response.
     * @throws IOException
     */
    public String getAsString(OID oid) throws IOException {
        ResponseEvent event = get(new OID[]{oid});
        Log.d("getAsString ohne listener", "String bekommen");
        return event.getResponse().get(0).getVariable().toString();
    }

    public void getAsString(OID oids, ResponseEvent listener) {
        try {
            snmp.send(getPDU(new OID[]{oids}), getTarget(),null, (ResponseListener) listener);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.d("getAsString mit listener", "String erhalten");
    }

    /**
     * Gets the PDU.
     * @param oids Array of the OIDs.
     * @return Returns the getted PDU.
     */
    private PDU getPDU (OID oids[]) {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        Log.d("getPDU", "got the PDU");
        return pdu;
    }

    /**
     * Lists the informations of the OIDs as a table.
     * @param oids Array of OIDs.
     * @return Returns the List.
     */
    public List<List<String>> getTableAsStrings(OID[] oids) {
        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory());
        List<TableEvent> events = utils.getTable(getTarget(), oids, null, null);
        List<List<String>> list = new ArrayList<List<String>>();
        for (TableEvent event : events) {
            if (event.isError()) {
                throw new RuntimeException(event.getErrorMessage());
            }
            List<String> stringsList = new ArrayList<String>();
            list.add(stringsList);
            for (VariableBinding variableBinding : event.getColumns()) {
                stringsList.add(variableBinding.getVariable().toString());
            }
        }
        return list;
    }

    public static String extractSingleString(ResponseEvent event) {
        return event.getResponse().get(0).getVariable().toString();
    }
}