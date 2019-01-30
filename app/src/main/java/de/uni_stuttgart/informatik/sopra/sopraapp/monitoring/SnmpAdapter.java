package de.uni_stuttgart.informatik.sopra.sopraapp.monitoring;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.uni_stuttgart.informatik.sopra.sopraapp.*;
import de.uni_stuttgart.informatik.sopra.sopraapp.requests.RequestDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SimpleSNMPClientV1AndV2c;
import de.uni_stuttgart.informatik.sopra.sopraapp.snmp.SimpleSNMPClientv3;

import static android.support.constraint.Constraints.TAG;

/**
 * Dies ist der SNMP Manager. Hiermit können die beiden SNMP Klassen mit Ihrem Task gemanaged werden.
 */
public class SnmpAdapter extends ArrayAdapter<SimpleSNMPClientV1AndV2c> {
    private static final String TAG = "SnmpAdapter";

    private ArrayList<SimpleSNMPClientV1AndV2c> elements;
    private Context context;
    private RequestDbHelper dbHelper;
    private ApplianceManager manager;

    private ArrayList<Timer> timer;
    private int period = 5000;

    /**
     * Konstruktor.
     *
     * @param context Context der Klasse.
     */
    public SnmpAdapter(Context context) {
        super(context, 0, ApplianceManager.getInstance(context).getClientList());
        manager = ApplianceManager.getInstance(context);
        elements = manager.getClientList();
        this.context = context;
        dbHelper = new RequestDbHelper(context);
        timer = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.snmp_appl_item, parent, false);
        }

        final SimpleSNMPClientV1AndV2c client = elements.get(position);

        final Switch req_switch = listItem.findViewById(R.id.switch1);
        req_switch.setOnClickListener(v -> {
            Timer timers = new Timer();
            timers.schedule(new PeriodTask(client, req_switch), 0, period);
            timer.add(timers);
            notifyDataSetChanged();
        });


        Spinner spinner = listItem.findViewById(R.id.request_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        adapter.add("NONE");
        adapter.addAll(dbHelper.getAllMasks());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    req_switch.setChecked(false);
                    req_switch.setEnabled(false);
                    return;
                }
                req_switch.setEnabled(true);
                String req = (String) parent.getSelectedItem();
                manager.setRequestFor(client, req);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setSelection(manager.getRequestMaskFrom(client) != null ? dbHelper.getAllMasks().indexOf(manager.getRequestMaskFrom(client)) + 1 : 0);
        TextView textView = listItem.findViewById(R.id.appl_name_field);
        textView.setText("Gerät " + position);

        ListView listView = listItem.findViewById(R.id.appl_info_list);
        ArrayList<String> infos = new ArrayList<>();
        infos.add("IPv4-Addresse:\t" + client.getAddress());
        if (client instanceof SimpleSNMPClientv3) {
            infos.add("Benutzer:\t" + client.getTarget().getSecurityName().toString());
        } else {
            infos.add("Community Target:\t" + client.getTarget().getSecurityName().toString());
        }

        infos.addAll(manager.getResults(client));
        listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, infos));

        return listItem;
    }

    private class ResultTask extends AsyncTask<SimpleSNMPClientV1AndV2c, Void, ArrayList<String>> {
        SimpleSNMPClientV1AndV2c client;

        @Override
        protected ArrayList<String> doInBackground(SimpleSNMPClientV1AndV2c... simpleSNMPClientV1AndV2cs) {
            client = simpleSNMPClientV1AndV2cs[0];
            return manager.tryGetResults(simpleSNMPClientV1AndV2cs[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            manager.setResultsFor(client, strings);
            notifyDataSetChanged();
        }
    }

    public void cancelAll(){
        for (Timer timers: timer) {
            timers.cancel();
        }
    }

    public void restartTimer(){
        timer = new ArrayList<>();
    }

    private class PeriodTask extends TimerTask {
        private SimpleSNMPClientV1AndV2c client;
        private Switch onSwitch;

        private PeriodTask(SimpleSNMPClientV1AndV2c client, Switch onSwitch) {
            this.client = client;
            this.onSwitch = onSwitch;
        }

        @Override
        public void run() {
            if (!onSwitch.isChecked()) {
                cancel();
                for (Timer timers: timer) {
                    timers.purge();
                }
                return;
            }
            manager.startRequestFor(client);
            new ResultTask().execute(client);
            Log.d(TAG, "run: Timer Task");
        }
    }
}
