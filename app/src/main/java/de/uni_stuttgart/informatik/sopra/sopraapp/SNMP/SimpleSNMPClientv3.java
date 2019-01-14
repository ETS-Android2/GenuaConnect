package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import android.util.Log;

import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
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

import java.io.IOException;

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
    private ApplianceQrDecode decode;
    private OID getAuthOID = null;
    private OID getPrivOID = null;

    /**
     * Konstruktor
     *
     * @param qrCode Der QR-Code decoder
     */
    public SimpleSNMPClientv3(String qrCode) {
        decode = new ApplianceQrDecode(qrCode);
        this.address = decode.getAddress();
        Log.d("StartAusfuehren", "Ausgefueht");
    }

    /**
     * Stopt die SNMP Instanz
     *
     * @throws IOException If a transport mapping cannot be closed successfully
     */
    public void stop() throws IOException {
        snmp.close();
        Log.d("Stop", "Gestoppt");
    }

    /**
     * Starts the SNMP Interface.
     *
     * @throws IOException If an IO operation exception occurs while starting the listener.
     */
    protected void start() throws IOException {
        TransportMapping<UdpAddress> transportMapping = new DefaultUdpTransportMapping();
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
     * Hier werden die Informationen wie authProtocoll, privProtocoll, privPasswort, authPasswort, userName initialisiert.
     */
    private void userInformation() {
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(snmp.getLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        OctetString getAuthPasswort = new OctetString(decode.getPassword());
        if (decode.getEncodeing().contains(":")) {
            String[] splittetEncodeing = decode.getEncodeing().split(":");
            OctetString getAuth = new OctetString(splittetEncodeing[0]);
            OctetString getPrivPasswort = new OctetString(splittetEncodeing[1]);
            OctetString getPriv = new OctetString(splittetEncodeing[2]);
            switch (getAuth.toString()) {
                case "SHA":
                    Log.d("getAuth: ", getAuth.toString());
                    getAuthOID = new OID(SnmpConstants.usmHMACSHAAuthProtocol);
                    break;
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
        //Address targetAddress = GenericAddress.parse(address);
        target = new UserTarget();
        target.setAddress(targetAdress);
        target.setSecurityName(new OctetString(decode.getUsername()));
        target.setRetries(3);
        target.setTimeout(10000);
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
     * Returns the response of the OID as a string.
     *
     * @param oid Is the OID.
     * @return Returns the response.
     */
    String getAsString(OID oid) {
        Log.d("getAsString", "String bekommen: " + oid.toDottedString());
        return sendGet(oid.toString());
    }

    /**
     * Die Getmethode fuer die Abfragen. Auch wird hier der PDU gesettet und geaddet.
     *
     * @param stringOID Die OID.
     * @return Returned den OID als String und returned null wenn PDU null ist.
     */
    private String sendGet(String stringOID) {
        ScopedPDU scopedPDU = (ScopedPDU) DefaultPDUFactory.createPDU(3);

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
}