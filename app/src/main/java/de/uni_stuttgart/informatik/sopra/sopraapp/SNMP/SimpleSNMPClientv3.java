package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import android.util.Log;

import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.TSM;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
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
public class SimpleSNMPClientv3 {

    private String address;
    private static OctetString localEngineId;
    private volatile Snmp snmp;
    private UserTarget target;
    private TransportMapping<UdpAddress> transportMapping;
    private ApplianceQrDecode decode;
    private OctetString getAuth = null;
    private OctetString getPrivPasswort = null;
    private OctetString getPriv = null;
    private OID getAuthOID = null;
    private OID getPrivOID = null;

    public SimpleSNMPClientv3(String qrCode) {
        decode = new ApplianceQrDecode(qrCode);
        this.address = decode.getAddress();
        Log.d("StartAusfuehren", "Ausgefueht");
    }

    public void stop() throws IOException {
        snmp.close();
        Log.d("Stop", "Gestoppt");
    }

    /**
     * Starts the SNMP Interface.
     *
     * @throws IOException
     */
    protected void start() throws IOException {
        transportMapping = new DefaultUdpTransportMapping();
        Log.d("Snmp Connect", "asynchroner Nebenthread gestartet");

        snmp = new Snmp(transportMapping);
        userInformation();
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

    private void userInformation() {
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(snmp.getLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        OctetString getAuthPasswort = new OctetString(decode.getPassword());
        if (decode.getEncodeing().contains(":")) {
            String[] splittetEncodeing = decode.getEncodeing().split(":");
            for (int i = 0; i < splittetEncodeing.length; i++) {
                getAuth = new OctetString(splittetEncodeing[0]);
                getPrivPasswort = new OctetString(splittetEncodeing[1]);
                getPriv = new OctetString(splittetEncodeing[2]);
            }
            switch (getAuth.toString()) {
                case "SHA":
                    Log.d("getAuth: ", getAuth.toString());
                    getAuthOID = new OID(SnmpConstants.usmHMACSHAAuthProtocol);
                    break;
                //case "SHA2":
                //Toast.makeText(this, "SHA2 wird nicht unterstuetzt", Toast.LENGTH_LONG).show();
                //break;
                case "MD5":
                    Log.d("getAuth: ", getAuth.toString());
                    getAuthOID = new OID(SnmpConstants.usmHMACSHAAuthProtocol);
                    break;
                case "":
                    Log.d("getAuth: ", getAuth.toString());
                    getAuthOID = new OID(SnmpConstants.usmNoAuthProtocol);
                    break;
            }
            switch (getPriv.toString()) {
                case "DES":
                    Log.d("getPriv: ", getPriv.toString());
                    getPrivOID = new OID(SnmpConstants.usmDESPrivProtocol);
                    break;
                case "AES-128":
                    Log.d("getPriv: ", getPriv.toString());
                    getPrivOID = new OID(SnmpConstants.usmAesCfb128Protocol);
                    break;
                case "AES-192":
                    Log.d("getPriv: ", getPriv.toString());
                    getPrivOID = new OID(SnmpConstants.oosnmpUsmAesCfb192Protocol);
                    break;
                case "AES-256":
                    Log.d("getPriv: ", getPriv.toString());
                    getPrivOID = new OID(SnmpConstants.oosnmpUsmAesCfb256Protocol);
                case "3DES":
                    Log.d("getPriv: ", getPriv.toString());
                    getPrivOID = new OID(SnmpConstants.usm3DESEDEPrivProtocol);
                    break;
                case "":
                    Log.d("getPriv: ", getPriv.toString());
                    getPrivOID = new OID(SnmpConstants.usmNoPrivProtocol);
            }
            OctetString userName = new OctetString(decode.getUsername());
            Log.d("userName: ", decode.getUsername());
            Log.d("getAuthPasswort: ", getAuthPasswort.toString());
            Log.d("priv: ", getPriv.toString());
            Log.d("getPrivPasswort: ", getPrivPasswort.toString());
            Log.d("addUser: ", "Starten");
            snmp.getUSM().addUser(userName, new UsmUser(userName, getAuthOID, getAuthPasswort, getPrivOID, getPrivPasswort));
            Log.d("addUser: ", "Erfolgreich");
            if (localEngineId == null) {
                localEngineId = new OctetString(MPv3.createLocalEngineID());
            }
            SecurityModels.getInstance().addSecurityModel(new TSM(new OctetString(snmp.getLocalEngineID()), false));
            Log.d("SecurityModel: ", "Erfolgreich ausgefuehrt");
        }
    }

    protected Target getTarget() {
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
        Address targetAddress = GenericAddress.parse(address);
        target = new UserTarget();
        target.setAddress(targetAddress);
        target.setSecurityName(new OctetString(decode.getUsername()));
        target.setRetries(2);
        target.setTimeout(5000);
        target.setVersion(SnmpConstants.version3);
        if (getAuthOID == SnmpConstants.usmNoAuthProtocol) {
            Log.d("Welches SecurityLevel ?", "NOAUTH_NOPRIV erkannt.");
            target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        } else if (getPrivOID == SnmpConstants.usmNoPrivProtocol && (getAuthOID == SnmpConstants.usmHMACMD5AuthProtocol || getAuthOID ==
                SnmpConstants.usmHMACSHAAuthProtocol)) {
            Log.d("Welches SecurityLevel ?", "AUTH_NOPRIV erkannt.");
            target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
        } else {
            Log.d("Welches SecurityLevel ?", "AUTH_PRIV erkannt.");
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        }
        Log.d("getTarget", "gesettet");
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
    String getAsString(OID oid) throws IOException {
        Log.d("getAsString", "String bekommen: " + oid.toDottedString());
        return sendGet(oid.toString());
    }

    public void getAsString(OID oids, ResponseEvent listener) {
        try {
            snmp.send(getPDU(new OID[]{oids}), getTarget(), null, (ResponseListener) listener);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the PDU.
     *
     * @param oids Array of the OIDs.
     * @return Returns the getted PDU.
     */
    private ScopedPDU getPDU(OID oids[]) {
        ScopedPDU pdu = new ScopedPDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(ScopedPDU.GET);
        Log.d("getPDU", "got the PDU");
        return pdu;
    }

    /**
     * Lists the informations of the OIDs as a table.
     *
     * @param oids Array of OIDs.
     * @return Returns the List.
     */
    private List<List<String>> getTableAsStrings(OID[] oids) {
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

    /**
     * @param stringOID
     * @return
     */
    private String sendGet(String stringOID) {
        ScopedPDU scopedPDU = (ScopedPDU) DefaultPDUFactory.createPDU(1);

        //add OID to PDU
        scopedPDU.add(new VariableBinding(new OID(stringOID)));

        //setting type when sending request to the SNMP server
        scopedPDU.setType(ScopedPDU.GETBULK);

        ResponseEvent responseEvent;
        try {
            //sending the request
            responseEvent = snmp.get(scopedPDU, getTarget());

            if (responseEvent != null) {
                System.out.println(responseEvent);
                //get the PDU response
                ScopedPDU pduResult = (ScopedPDU) responseEvent.getResponse();
                System.out.println(pduResult);
                if (pduResult == null) {
                    return null;
                }

                for (VariableBinding varBind : pduResult.getVariableBindings()) {
                    return varBind.toValueString();
                }

                //checking if response throws an error
                if (pduResult.getErrorStatus() == ScopedPDU.noError) {

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

    private static String extractSingleString(ResponseEvent event) {
        return event.getResponse().get(0).getVariable().toString();
    }
}