package de.uni_stuttgart.informatik.sopra.sopraapp.SNMP;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * SNMP client to monitor servers and devices.
 * Inspired by https://www.youtube.com/watch?v=Nw18dAnWn2U.
 */
public class SNMPClientTest {

    //snmp variable
    private Snmp snmpClient = null;

    //transport mapping variable
    private TransportMapping transportMapping = null;

    //consists of the snmp information
    private CommunityTarget communityTarget = null;

    public SNMPClientTest(String stringHost, int port, String stringCommunity, int version, int timeOut, int retry) throws Exception {
        //new transportMapping
        this.transportMapping = new DefaultUdpTransportMapping();

        //starting listen
        this.snmpClient = new Snmp(this.transportMapping);

        //creates a variable for the communityTarget
        this.communityTarget = new CommunityTarget();

        //setting the parameters
        this.communityTarget.setAddress(new UdpAddress(stringHost + "/" + port));
        this.communityTarget.setCommunity(new OctetString(stringCommunity));
        this.communityTarget.setRetries(retry);
        this.communityTarget.setTimeout(timeOut);
        this.communityTarget.setVersion(version - 1);
    }

    /**
     *
     * @param stringOID
     * @return
     */
    public String sendGet(String stringOID) {
        String stringResult = null;

        PDU pdu = new PDU();

        //add OID to PDU
        pdu.add(new VariableBinding(new OID(stringOID)));

        //setting type when sending request to the SNMP server
        pdu.setType(PDU.GET);

        ResponseEvent responseEvent;
        try {
            //sending the request
            responseEvent = this.snmpClient.send(pdu, this.communityTarget);

            if (responseEvent != null) {

                //get the PDU response
                PDU pduResult = responseEvent.getResponse();

                //checking if response throws an error
                if (pduResult.getErrorStatus() == PDU.noError) {

                    //gets the variable when there's no error
                    Variable variable = pduResult.getVariable(new OID(stringOID));

                    if (variable != null) {

                        //result
                        stringResult = variable.toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pdu.clear();
        return stringResult;
    }

    /**
     *
     * @param stringOID
     * @return
     */
    public String sendGetNext(String stringOID) {
        String stringResult = null;

        PDU pdu = new PDU();

        pdu.add(new VariableBinding(new OID(stringOID)));

        //gleich wie oben nur mit GETNEXT.
        pdu.setType(PDU.GETNEXT);
        ResponseEvent responseEvent;
        try {
            responseEvent = this.snmpClient.send(pdu, this.communityTarget);

            if (responseEvent != null) {
                PDU pduResult = responseEvent.getResponse();

                if (pduResult.getErrorStatus() == PDU.noError) {
                    VariableBinding variableBinding = pduResult.getVariableBindings().get(0);

                    if (variableBinding != null) {
                        stringResult = variableBinding.toValueString();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdu.clear();
        return stringResult;
    }

    /**
     * sends the request and receiver to an array as a result.
     * @param stringOID
     * @return
     */
    public Hashtable<String, String> sendGetBulk(String stringOID) {
        Hashtable<String, String> hashtableResult = null;

        PDU pdu = new PDU();

        pdu.add(new VariableBinding(new OID(stringOID)));

        //selbe wie oben aber mit GETBULK
        pdu.setType(PDU.GETBULK);
        pdu.setMaxRepetitions(20);

        ResponseEvent responseEvent;
        try {
            responseEvent= this.snmpClient.send(pdu, this.communityTarget);

            if (responseEvent != null) {
                hashtableResult = new Hashtable<>();
                PDU pduResult = responseEvent.getResponse();

                if (pduResult.getErrorStatus() == PDU.noError) {
                    Vector<? extends VariableBinding> vectorVariables;
                    vectorVariables = (Vector<? extends VariableBinding>) pduResult.getVariableBindings();

                    if (vectorVariables != null) {
                        for (VariableBinding variableBinding : vectorVariables) {
                            hashtableResult.put(variableBinding.getOid().toString(), variableBinding.toValueString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdu.clear();
        return hashtableResult;
    }

    /**
     * Schliesst transportMapping und oder snmpClient.
     */
    public void close() {
        try {
            if (this.transportMapping != null) {
                this.transportMapping.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (this.snmpClient != null) {
                this.snmpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
