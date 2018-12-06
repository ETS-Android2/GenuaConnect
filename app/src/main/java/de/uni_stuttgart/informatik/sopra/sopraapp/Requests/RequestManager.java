package de.uni_stuttgart.informatik.sopra.sopraapp.Requests;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RequestManager {

    //Singleton Pattern
    private static RequestManager instance;
    private RequestManager(){}
    public static RequestManager getInstance(){
        if(instance == null){
            instance = new RequestManager();
        }
        return instance;
    }

    private ConcurrentHashMap<String, RequestMask> allRequests;

    public boolean addNewMask(String name){
        if(allRequests.containsKey(name)){
            return false;
        }
        allRequests.put(name, new RequestMask());
        return true;
    }

    public void deleteMask(String name){
        allRequests.remove(name);
    }

    public RequestMask getMask(String name){
        if(allRequests.containsKey(name)){
            return allRequests.get(name);
        }
        return null;
    }

    public Set<String> getAllMaks(){
        return allRequests.keySet();
    }
}
