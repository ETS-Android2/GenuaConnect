package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import java.util.ArrayList;

public class RequestMask {
    private String name;
    private ArrayList<String> requests;

    public RequestMask (){
        requests = new ArrayList<>();
    }

    public void addRequest(String oid){
        requests.add(oid);
    }

    public ArrayList<String> getRequests() {
        return requests;
    }
}
