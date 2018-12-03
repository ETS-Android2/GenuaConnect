package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.ApplianceQrDecode;

public class SimpleSNMPClientv3 implements Serializable {

    protected String address;
    private Snmp snmp;

    public SimpleSNMPClientv3(String qrCode) {
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
    protected Target getTarget() {
        Address targetAdress = GenericAddress.parse(address);
        UserTarget target = new UserTarget();
        target.setAddress(targetAdress);
        target.setRetries(3);
        target.setTimeout(500);
        target.setVersion(SnmpConstants.version3);
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString("MD5DES"));
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
        ScopedPDU pdu = new ScopedPDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(ScopedPDU.GET);
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
    private ScopedPDU getPDU (OID oids[]) {
        ScopedPDU pdu = new ScopedPDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(ScopedPDU.GET);
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
