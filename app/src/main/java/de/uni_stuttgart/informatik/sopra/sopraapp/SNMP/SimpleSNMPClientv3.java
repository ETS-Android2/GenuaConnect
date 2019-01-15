package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

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

import de.uni_stuttgart.informatik.sopra.sopraapp.ApplianceQrDecode;

/**
 * Inspired by https://blog.jayway.com/2010/05/21/introduction-to-snmp4j
 * A client for the SNMP management.
 */
public class SimpleSNMPClientv3 extends SimpleSNMPClientV1AndV2c {

    private String address;
    private static OctetString localEngineId;
    private UserTarget target;
    private ApplianceQrDecode decode;
    private OctetString getAuth = null;
    private OctetString getPrivPasswort = null;
    private OctetString getPriv = null;
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
     * Hier werden die Informationen wie authProtocoll, privProtocoll, privPasswort, authPasswort, userName initialisiert.
     */
    @Override
    protected void userInformation() {
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(snmp.getLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        OctetString getAuthPasswort = new OctetString(decode.getPassword());
        if (decode.getEncodeing().contains(":")) {
            String[] splittetEncodeing = decode.getEncodeing().split(":");
            getAuth = new OctetString(splittetEncodeing[0]);
            getPrivPasswort = new OctetString(splittetEncodeing[1]);
            getPriv = new OctetString(splittetEncodeing[2]);
        } else if (decode.getEncodeing().equals("SHA")){
            getAuth = new OctetString("SHA");
        }
        if (getAuth != null) {
            switch (getAuth.toString()) {
                case "SHA":
                    Log.d("getAuth: ", getAuth.toString());
                    getAuthOID = new OID(SnmpConstants.usmHMACSHAAuthProtocol);
                    break;
                case "MD5":
                    Log.d("getAuth: ", getAuth.toString());
                    getAuthOID = new OID(SnmpConstants.usmHMACSHAAuthProtocol);
                    break;
                default:
                    Log.d("getAuth: ", getAuth.toString());
                    getAuthOID = null;
                    getAuthPasswort = null;
            }
        }
            if (getPriv != null) {
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
                    default:
                        Log.d("getPriv: ", getPriv.toString());
                        getPrivOID = null;
                        getPrivPasswort = null;
                }
            }
            OctetString userName = new OctetString(decode.getUsername());
            Log.d("userName: ", decode.getUsername());
            if (getAuthPasswort != null) {
                Log.d("getAuthPasswort: ", getAuthPasswort.toString());
            } else {
                Log.d("getAuthPasswort", "null");
            }
            if (getPriv != null) {
                Log.d("priv: ", getPriv.toString());
            } else {
                Log.d("priv", "null");
            }
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
     * Die Getmethode fuer die Abfragen. Auch wird hier der PDU gesettet und geaddet.
     *
     * @param stringOID Die OID.
     * @return Returned den OID als String und returned null wenn PDU null ist.
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