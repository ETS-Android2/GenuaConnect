package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import org.snmp4j.smi.OID;

import java.util.ArrayList;

public class RequestMask {
    private ArrayList<OID> requests;

    public RequestMask (){
        requests = new ArrayList<>();
    }

    public void addRequest(OID oid){
        requests.add(oid);
    }

    public void addRequest(String oid){
        requests.add(new OID(oid));
    }




}
