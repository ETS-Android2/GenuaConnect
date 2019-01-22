package de.uni_stuttgart.informatik.sopra.sopraapp.snmp;

import android.util.Log;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
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

import java.io.IOException;

import de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode.ApplianceQrDecode;

/**
 * Inspired by https://blog.jayway.com/2010/05/21/introduction-to-snmp4j
 * A client for the SNMP management.
 */
public class SimpleSNMPClientV1AndV2c {

    String address;
    volatile Snmp snmp;
    private CommunityTarget target;
    ApplianceQrDecode decode;
    /**
     * constructor
     *
     * @param qrCode the QR-Code decoder.
     */
    public SimpleSNMPClientV1AndV2c(String qrCode) {
        SNMP4JSettings.setAllowSNMPv2InV1(true);
        Log.d("allowSNMPv2InV1", "erfolgreich");
        decode = new ApplianceQrDecode(qrCode);
        this.address = decode.getAddress();
    }

    /**
     * defaultconstructor for inheriting classe.
     */
    SimpleSNMPClientV1AndV2c() {
    }

    public String getAddress() {
        return address;
    }

    /**
     * stoppt den SNMP Client.
     *
     * @throws IOException If a transport mapping cannot be closed successfully
     */
    public void stop() throws IOException {
        snmp.close();
        Log.d("SNMP", "Interface for SNMP closed");
    }

    /**
     * Starts the SNMP Interface.
     *
     * @throws IOException If an IO operation exception occurs while starting the listener.
     */
    void start() throws IOException, IllegalArgumentException {
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

    protected void userInformation() {
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
    }

    /**
     * Returns a target, which contains the information about to where and how the data should be fetched.
     *
     * @return Returns the given target.
     */
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
        target = new CommunityTarget();
        target.setCommunity(new OctetString(decode.getTarget()));
        target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        target.setAddress(targetAdress);
        target.setRetries(3);
        target.setTimeout(10000);
        target.setVersion(SnmpConstants.version1);
        Log.d("getTarget", "getTarget erfolgreich");
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
    protected String sendGet(String stringOID) {
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