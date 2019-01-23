package de.uni_stuttgart.informatik.sopra.sopraapp.monitoring;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.uni_stuttgart.informatik.sopra.sopraapp.requests.OidElement;
import de.uni_stuttgart.informatik.sopra.sopraapp.requests.RequestDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SimpleSNMPClientV1AndV2c;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SnmpTask;

/**
 * Manages all appliances that were scanned by the Main Activitys QR-Scanner
 */
public class ApplianceManager {

    private static ApplianceManager ourInstance;

    /**
     * Konstruktor
     *
     * @param context Context of class.
     * @return Returned die gebrauchte Instanz des ApplianceManager.
     */
    public static ApplianceManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ApplianceManager();
            ourInstance.clientV1AndV2List = new ArrayList<>();
            ourInstance.requestTable = new HashMap<>();
            ourInstance.requestDbHelper = new RequestDbHelper(context);
            ourInstance.taskTable = new HashMap<>();
            ourInstance.resultTable = new HashMap<>();
        }
        return ourInstance;
    }

    private ApplianceManager() {
    }

    private ArrayList<SimpleSNMPClientV1AndV2c> clientV1AndV2List;
    private RequestDbHelper requestDbHelper;
    private HashMap<SimpleSNMPClientV1AndV2c, String> requestTable;
    private HashMap<SimpleSNMPClientV1AndV2c, SnmpTask[]> taskTable;
    private HashMap<SimpleSNMPClientV1AndV2c, ArrayList<String>> resultTable;


    /**
     * Die Gettermethode für den SNMP Client.
     *
     * @return Returned den Client.
     */
    ArrayList<SimpleSNMPClientV1AndV2c> getClientList() {
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
    void setRequestFor(SimpleSNMPClientV1AndV2c client, String request) {
        requestTable.put(client, request);
    }


    /**
     * Starts request for all oids in requestmask that was specified for this client
     *
     * @param client client to send the requests
     */
    void startRequestFor(SimpleSNMPClientV1AndV2c client) {
        String request = requestTable.get(client);
        ArrayList<OidElement> oidElements = requestDbHelper.getOIDsFrom(request);
        ArrayList<String> oids = new ArrayList<>();
        for (OidElement element : oidElements) {
            oids.add(element.getOidString());
        }
        SnmpTask[] tasks = new SnmpTask[oids.size()];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new SnmpTask(client);
            tasks[i].execute(oids.get(i));
        }
        taskTable.put(client, tasks);
    }

    ArrayList<String> tryGetResults(SimpleSNMPClientV1AndV2c client) {
        ArrayList<String> results = new ArrayList<>();
        if (taskTable.get(client) == null) {
            return results;
        }
        for (SnmpTask task : Objects.requireNonNull(taskTable.get(client))) {
            try {
                results.add(task.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        startRequestFor(client);

        return results;
    }

    String getRequestMaskFrom(SimpleSNMPClientV1AndV2c client) {
        return requestTable.get(client);
    }

    ArrayList<String> getResults(SimpleSNMPClientV1AndV2c client) {
        return resultTable.get(client) == null ? new ArrayList<>() : resultTable.get(client);
    }

    void setResultsFor(SimpleSNMPClientV1AndV2c client, ArrayList<String> results) {
        resultTable.put(client, results);
    }

}
