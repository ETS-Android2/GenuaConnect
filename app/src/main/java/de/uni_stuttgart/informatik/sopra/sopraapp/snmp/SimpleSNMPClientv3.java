package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

import android.util.Log;

import org.snmp4j.ScopedPDU;
import org.snmp4j.Target;
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
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;

import de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode.ApplianceQrDecode;

/**
 * Inspired by https://blog.jayway.com/2010/05/21/introduction-to-snmp4j
 * A client for the SNMP management.
 */
public class SimpleSNMPClientv3 extends SimpleSNMPClientV1AndV2c {

    private static OctetString localEngineId;
    private UserTarget target;
    private OID getAuthOID = null;
    private OID getPrivOID = null;

    /**
     * constructor
     *
     * @param qrCode the QR-Code decoder
     */
    public SimpleSNMPClientv3(String qrCode) {
        decode = new ApplianceQrDecode(qrCode);
        this.address = decode.getAddress();
        Log.d("StartAusfuehren", "Ausgefueht");
    }

    public String getAddress() {
        return address;
    }

    /**
     * authProtocoll, privProtocoll, privPasswort, authPasswort, userName information are initialized
     */
    @Override
    protected void userInformation() throws IllegalArgumentException {
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(snmp.getLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        OctetString getAuthPasswort = new OctetString(decode.getPassword());
        OctetString getAuth = new OctetString(decode.getAuthProtocol());
        OctetString getPriv = new OctetString(decode.getPrivProtocol());
        OctetString getPrivPasswort = new OctetString(decode.getPrivKey());
        if (getAuth.toString().isEmpty()) {
            getAuthOID = null;
            getAuthPasswort = null;
        } else {
            switch (getAuth.toString()) {
                case "SHA":
                    getAuthOID = new OID(SnmpConstants.usmHMACSHAAuthProtocol);
                    break;
                case "MD5":
                    getAuthOID = new OID(SnmpConstants.usmHMACMD5AuthProtocol);
                    break;
                default:
                    if (!getAuth.toString().isEmpty()) {
                        throw new IllegalArgumentException();
                    }
            }
        }
        if (getPriv.toString().isEmpty()) {
            getPrivOID = null;
            getPrivPasswort = null;
        } else {
            switch (getPriv.toString()) {
                case "DES":
                    getPrivOID = new OID(SnmpConstants.usmDESPrivProtocol);
                    break;
                case "AES-128":
                    getPrivOID = new OID(SnmpConstants.usmAesCfb128Protocol);
                    break;
                case "AES-192":
                    getPrivOID = new OID(SnmpConstants.oosnmpUsmAesCfb192Protocol);
                    break;
                case "AES-256":
                    getPrivOID = new OID(SnmpConstants.oosnmpUsmAesCfb256Protocol);
                case "3DES":
                    getPrivOID = new OID(SnmpConstants.usm3DESEDEPrivProtocol);
                    break;
                default:
                    if (!getPriv.toString().isEmpty()) {
                        Log.d("privProtocol", getPriv.toString());
                        throw new IllegalArgumentException();
                    }
            }
        }
        OctetString userName = new OctetString(decode.getUsername());
        Log.d("userName: ", decode.getUsername());
        if (getAuthPasswort != null) {
            Log.d("getAuthPasswort: ", getAuthPasswort.toString());
        } else {
            Log.d("getAuthPasswort", "null");
        }
        Log.d("getAuth: ", getAuth.toString());
        Log.d("priv: ", getPriv.toString());
        if (getPrivPasswort != null) {
            Log.d("getPrivPasswort: ", getPrivPasswort.toString());
        } else {
            Log.d("getPrivPasswort", "null");
        }
        Log.d("addUser: ", "Starten");
        snmp.getUSM().addUser(userName, new UsmUser(userName, getAuthOID, getAuthPasswort, getPrivOID, getPrivPasswort));
        Log.d("addUser: ", "Erfolgreich");
        if (localEngineId == null) {
            localEngineId = new OctetString(MPv3.createLocalEngineID());
        }
        SecurityModels.getInstance().addSecurityModel(new TSM(new OctetString(snmp.getLocalEngineID()), false));
        Log.d("SecurityModel: ", "Erfolgreich ausgefuehrt");
    }

    /**
     * Returns a target, which contains the information about to where and how the data should be fetched.
     *
     * @return Returns the given target.
     */
    @Override
    public Target getTarget() {
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
        target = new UserTarget();
        target.setAddress(targetAdress);
        target.setSecurityName(new OctetString(decode.getUsername()));
        target.setRetries(3);
        target.setTimeout(10000);
        target.setVersion(SnmpConstants.version3);
        if (getAuthOID == null) {
            Log.d("Welches SecurityLevel ?", "NOAUTH_NOPRIV erkannt.");
            target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        } else if (getPrivOID == null) {
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
     * PDU is added and setted.
     * get-method for the query.
     *
     * @param stringOID the OID.
     * @return returns the OID as a String and returns null if PDU is null.
     */
    @Override
    protected String sendGet(String stringOID) {
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