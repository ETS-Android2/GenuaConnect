package de.uni_stuttgart.informatik.sopra.sopraapp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
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

public class SimpleSnmpClient {
    private String address;
    private Snmp snmp;

    public SimpleSnmpClient(String qrCode) {
        super();
        ApplianceQrDecode decode = new ApplianceQrDecode(qrCode);
        this.address = decode.getAddress();
        try {
            start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() throws IOException {
        snmp.close();
    }

    /**
     * Starts the SNMP Interface.
     *
     * @throws IOException
     */
    private void start() throws IOException {
        TransportMapping transportMapping = new DefaultUdpTransportMapping();
        snmp = new Snmp(transportMapping);
        transportMapping.listen();
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
        target.setVersion(SnmpConstants.version3);
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
        return event.getResponse().get(0).getVariable().toString();
    }

    public void getAsString(OID oids, ResponseEvent listener) {
        try {
            snmp.send(getPDU(new OID[]{oids}), getTarget(),null, (ResponseListener) listener);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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