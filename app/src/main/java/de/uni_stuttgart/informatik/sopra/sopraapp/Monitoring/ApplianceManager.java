package de.uni_stuttgart.informatik.sopra.sopraapp.Monitoring;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import de.uni_stuttgart.informatik.sopra.sopraapp.Requests.RequestDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SimpleSNMPClientV1AndV2c;
import de.uni_stuttgart.informatik.sopra.sopraapp.SNMP.SnmpTask;

/**
 * Manages all appliances that were scanned by the Main Activitys QR-Scanner
 */
public class ApplianceManager {

    private static ApplianceManager ourInstance;

    /**
     * Konstruktor
     *
     * @param context Context der Klasse.
     * @return Returned die gebrauchte Instanz aus der Datenbank.
     */
    public static ApplianceManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ApplianceManager();
            ourInstance.clientV1AndV2List = new ArrayList<>();
            ourInstance.requestTable = new HashMap<>();
            ourInstance.requestDbHelper = dbHelper;
        }
        return ourInstance;
    }

    private ApplianceManager() {
    }

    private ArrayList<SimpleSNMPClientV1AndV2c> clientV1AndV2List;
    private RequestDbHelper requestDbHelper;
    private HashMap<SimpleSNMPClientV1AndV2c, String> requestTable;
    private HashMap<SimpleSNMPClientV1AndV2c, SnmpTask[]> taskTable;


    /**
     * Die Gettermethode für den SNMP Client.
     *
     * @return Returned den Client.
     */
    public ArrayList<SimpleSNMPClientV1AndV2c> getClientList() {
        return clientV1AndV2List;
    }

    /**
     * Hiermit können clients geaddet werden.
     *
     * @param appliance Durch diese Variable werden die Clients geaddet.
     */
    public void addClient(SimpleSNMPClientV1AndV2c appliance) {
        clientV1AndV2List.add(appliance);
    }

    /**
     * Settet die Abfrage aus dem Client.
     *
     * @param client  Der SNMP Client.
     * @param request Die Abfrage.
     */
    public void setRequestFor(SimpleSNMPClientV1AndV2c client, String request) {
        requestTable.put(client, request);
    }


    /**
     * Starts request for all oids in requestmask that was specified for this client
     * @param client client to send the requests
     */
    public void startRequestFor(SimpleSNMPClientV1AndV2c client) {
        if (taskTable == null) {
            taskTable = new HashMap<>();
        }

        String request = requestTable.get(client);
        ArrayList<String> oids = requestDbHelper.getOIDsFrom(request);
        SnmpTask[] tasks = new SnmpTask[oids.size()];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new SnmpTask(client);
            tasks[i].execute(oids.get(i));
        }
        taskTable.put(client, tasks);
    }

}
