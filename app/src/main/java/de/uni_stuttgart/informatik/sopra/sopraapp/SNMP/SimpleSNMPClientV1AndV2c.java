package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import android.util.Log;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
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
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.ApplianceQrDecode;

/**
 * Inspired by https://blog.jayway.com/2010/05/21/introduction-to-snmp4j
 * A client for the SNMP management.
 */
public class SimpleSNMPClientV1AndV2c {

    private String address;
    private volatile Snmp snmp;
    private CommunityTarget target;
    private TransportMapping<UdpAddress> transportMapping;
    private ApplianceQrDecode decode;

    public SimpleSNMPClientV1AndV2c(String qrCode) {
        SNMP4JSettings.setAllowSNMPv2InV1(true);
        SNMP4JSettings.setSnmp4jStatistics(SNMP4JSettings.Snmp4jStatistics.extended);
        Log.d("allowSNMPv2InV1", "erfolgreich");
        decode = new ApplianceQrDecode(qrCode);
        this.address = decode.getAddress();
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
    public void start() throws IOException {
        transportMapping = new DefaultUdpTransportMapping();
        Log.d("Snmp Connect", "asynchroner Nebenthread gestartet");

        snmp = new Snmp(transportMapping);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        try {
            transportMapping.listen();
        } catch (IOException e) {
            Log.e("connect", e.getMessage());
        }
        Log.d("Snmp Connect", "isListening: " + transportMapping.isListening());
    }

    /**
     * Returns a target, which contains the information about to where and how the data should be fetched.
     *
     * @return Returns the given target.
     */
    private Target getTarget() {
        if (target != null) {
            return target;
        }
        String addrToUse = address;
        String port = null;
        Log.d("address", "received address: " + addrToUse);
        Address targetAdress = null;
        if (decode.getAddress().contains("/")) {
            Log.d("port", "port nicht null");
            Log.d("port '/' oder ':'?", "port '/' erkannt");
            addrToUse = address.substring(0, address.lastIndexOf('/'));
            port = address.substring(address.lastIndexOf("/") + 1);
            Log.d("port", port);
            targetAdress = GenericAddress.parse("udp:" + addrToUse + "/" + port);
        } else if (decode.getAddress().contains(":")) {
            Log.d("port", "port nicht null");
            Log.d("port '/' oder ':'?", "port ':' erkannt");
            addrToUse = address.substring(0, address.lastIndexOf(':'));
            port = address.substring(address.lastIndexOf(":") + 1);
            Log.d("port", port);
            targetAdress = GenericAddress.parse("udp:" + addrToUse + "/" + port);
        }
        if (port == null) {
            Log.d("port", "port null");
            targetAdress = GenericAddress.parse("udp:" + addrToUse + "/" + "161");
        }
        System.out.println(targetAdress);
        target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        target.setAddress(targetAdress);
        target.setRetries(2);
        target.setTimeout(5000);
        target.setVersion(SnmpConstants.version1);
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
        ResponseEvent event = snmp.send(pdu, getTarget(), transportMapping);
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
        Log.d("getAsString", "String bekommen: " + oid.toDottedString());
        return sendGet(oid.toString());
    }

    public void getAsString(OID oids, ResponseEvent listener) {
        try {
            snmp.send(getPDU(new OID[]{oids}), getTarget(), null, (ResponseListener) listener);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.d("getAsString", "String erhalten mit listener");
    }

    /**
     * Gets the PDU.
     *
     * @param oids Array of the OIDs.
     * @return Returns the getted PDU.
     */
    private PDU getPDU(OID oids[]) {
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
     *
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


    /**
     * @param stringOID
     * @return
     */
    private String sendGet(String stringOID) {
        PDU pdu = DefaultPDUFactory.createPDU(1);

        //add OID to PDU
        pdu.add(new VariableBinding(new OID(stringOID)));

        //setting type when sending request to the SNMP server
        pdu.setType(PDU.GETNEXT);

        ResponseEvent responseEvent;
        try {
            //sending the request
            responseEvent = snmp.get(pdu, getTarget());

            if (responseEvent != null) {
                System.out.println(responseEvent);
                //get the PDU response
                PDU pduResult = responseEvent.getResponse();
                System.out.println(pduResult);
                if (pduResult == null) {
                    return null;
                }

                for (VariableBinding varBind : pduResult.getVariableBindings()) {
                    return varBind.toValueString();
                }

                //checking if response throws an error
                if (pduResult.getErrorStatus() == PDU.noError) {

                    //gets the variable when there's no error
                    Variable variable = pduResult.getVariable(new OID(stringOID));

                    if (variable != null) {
                        //result
                        return variable.toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}