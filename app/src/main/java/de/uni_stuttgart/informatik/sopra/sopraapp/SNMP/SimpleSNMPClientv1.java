package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import org.snmp4j.AbstractTarget;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv1;
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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.ApplianceQrDecode;

public class SimpleSNMPClientv1 {

    private String address;
    private Snmp snmp;
    private TransportMapping transportMapping;

    public SimpleSNMPClientv1(String qrCode) {
        snmp = new Snmp();
        ApplianceQrDecode decode = new ApplianceQrDecode(qrCode);
        this.address = decode.getAddress();
        try {
            transportMapping = new DefaultUdpTransportMapping();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SNMP4JSettings.setAllowSNMPv2InV1(true);
        SNMP4JSettings.setSnmp4jStatistics(SNMP4JSettings.Snmp4jStatistics.extended);

        getTarget();

        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
    }

    public void stop() throws IOException {
        snmp.close();
    }

    /**
     * Returns a target, which contains the information about to where and how the data should be fetched.
     *
     * @return Returns the given target.
     */
    private AbstractTarget getTarget() {
        Address targetAdress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAdress);
        target.setRetries(3);
        target.setTimeout(1500);
        target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        target.setVersion(SnmpConstants.version1);
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
        PDU pdu = DefaultPDUFactory.createPDU(1);
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GETNEXT);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if (event.getResponse() != null) {
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
